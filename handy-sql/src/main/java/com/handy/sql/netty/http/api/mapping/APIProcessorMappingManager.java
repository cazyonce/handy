package com.handy.sql.netty.http.api.mapping;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.initializer.Initializer;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.api.processor.dynamic.GetSQLDynamicProcessor;
import com.handy.sql.netty.http.api.processor.system.GetRegisterListAPIProcessor;
import com.handy.sql.netty.http.api.processor.system.PostRegisterAPIProcessor;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;

import io.netty.handler.codec.http.HttpMethod;

public final class APIProcessorMappingManager implements Initializer {

	private final static PathMappingComparator PATH_MAPPING_COMPARARTOR = new PathMappingComparator();

	Class<?>[] SYSTEM_PROCESSOR = { PostRegisterAPIProcessor.class, GetRegisterListAPIProcessor.class };

	public final PathMapping root = new PathMapping(PATH_MAPPING_COMPARARTOR);

	public AbstractHttpProcessor get(String path, HttpMethod method) throws CustomException {
		APIInfo apiInfo = getPathMapping(path).getAPIInfo(method);
		Class<? extends AbstractHttpProcessor> executeProcessorClass = apiInfo.getExecuteProcessorClass();
		AbstractHttpProcessor processor;
		try {
			processor = executeProcessorClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			// TODO: 记录日志
			throw new CustomException("创建API处理器实例失败，错误 > " + e.getCause().getMessage());
		}
		processor.setApiInfo(apiInfo);
		return processor;
	}

	public void register(APIInfo apiInfo) throws CustomException {
		ArrayList<String> pathVariableNames = new ArrayList<String>();
		PathMapping mapping = registerPathMapping(apiInfo.getMapping().getPath(), pathVariableNames);
		apiInfo.setPathVariableNames(pathVariableNames);
		mapping.addAPIInfo(apiInfo);
	}
//	public void register(AbstractHttpProcessor processor) throws CustomException {
//		ArrayList<String> pathVariableNames = new ArrayList<String>();
//		PathMapping mapping = registerPathMapping(processor.getPath(), pathVariableNames);
//		processor.getApiInfo().setPathVariableNames(pathVariableNames);
//		mapping.addProcessor(processor);
//	}

	public List<APIInfo> getAPIInfoList() {
		return list(new ArrayList<APIInfo>(), root);
	}

	private List<APIInfo> list(List<APIInfo> list, PathMapping root) {
		for (PathMapping pathMapping : root.values()) {
			Map<HttpMethod, APIInfo> apiInfos = pathMapping.apiInfos;
			if (apiInfos != null) {
				for (APIInfo apiInfo : apiInfos.values()) {
					list.add(apiInfo);
				}
			}
			if (!pathMapping.isEmpty()) {
				list(list, pathMapping);
			}
		}
		return list;
	}

	@Override
	public void init() throws CustomException {

		for (Class<?> processorClass : SYSTEM_PROCESSOR) {

			Object obj;
			try {
				obj = processorClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				// TODO: 记录日志
				throw new CustomException("创建API处理器实例失败，错误 > " + e.getCause().getMessage());
			}

			if (!(obj instanceof AbstractHttpProcessor)) {
				throw new CustomException(String.format("class【%s】不是class【%s】的实现！", obj.getClass().getName(),
						AbstractHttpProcessor.class.getName()));
			}

			AbstractHttpProcessor processor = (AbstractHttpProcessor) obj;
			register(processor.newAPIInfoInstance());
		}
		
//		TODO: 处理数据库的初始化
//		List<Map<String, Object>> aa = GlobalProvide.JDBC_TEMPLATE.queryForList("select `path`, `method`, `name`, `describe`, `response_code`, `status` from api_mapping", new  MapSqlParameterSource());
//		SQLAPIInfo[] ss;
//		try {
//			ss = GlobalProvide.DB_OBJECT_MAPPER.readValue(GlobalProvide.DB_OBJECT_MAPPER.writeValueAsBytes(aa), SQLAPIInfo[].class);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new CustomException(e.getMessage());
//		}
//		for (SQLAPIInfo info : ss) {
//			if (HttpMethod.GET.name().equals(info.getMapping().getMethod().name())) {
//				info.setExecuteProcessorClass(GetSQLDynamicProcessor.class);
//			}
//			register(info);
//		}
	}

	private PathMapping getPathMapping(String path) throws CustomException {
		return process(root, path, new PathProcessor() {

			@Override
			protected PathMapping process(PathMapping parent, String currentKey, String path, String fragment)
					throws CustomException {
				if (currentKey == null) { // key为null 表示不需要或已经找到了
					return parent;
				}
				PathMapping mapping = parent.get(currentKey);
				if (mapping == null) {
					throw new CustomException("path: " + path + ", 未找到");
				}
				return mapping;
			}
		});
	}

	private PathMapping registerPathMapping(String path, ArrayList<String> pathVariableNames) throws CustomException {
		return process(root, path, new PathProcessor() {

			@Override
			protected PathMapping process(PathMapping parent, String currentKey, String path, String fragment)
					throws CustomException {
				if (currentKey == null) { // 如果key为null 也表示 path mapping注入完成
					return parent;
				}

				// 是变量位就将变量名处理并存储
				if ("*".equals(currentKey)) {
					String variableName = fragment.substring(1, fragment.length() - 1);
					if (pathVariableNames.contains(variableName)) {
						throw new CustomException("重复的path变量名称：" + variableName);
					}
					pathVariableNames.add(variableName);
				}

				synchronized (parent) {
					PathMapping mapping = parent.get(currentKey);
					if (Objects.isNull(mapping)) {
						parent.put(currentKey, mapping = new PathMapping(PATH_MAPPING_COMPARARTOR));
					}
					return mapping;
				}
			}
		});
	}

	private PathMapping process(PathMapping root, String path, PathProcessor processor) throws CustomException {
		String key = null;
		String[] fragments = path.split("\\/");
		boolean slashSufix = path.endsWith("/"); // 是否斜杠结尾
		for (String fragment : fragments) {
			if (fragment.isEmpty()) { // 表示第一级片段
				key = "/";
				fragment = "/";
			} else if (fragment.startsWith("{") && fragment.endsWith("}") && fragment.length() > 2) { // 参数
				key = "*";
			} else {
				key = fragment;
			}
			root = processor.process(root, key, path, fragment);
		}

		// path是以/结尾
		if (slashSufix) {
			key = "/";
			return processor.process(root, key, path, key);
		}
		// 只有当path为"/aa"时，代码才会执行到这里
		key = null;
		return processor.process(root, key, path, null);
	}

	private abstract class PathProcessor {
		protected abstract PathMapping process(PathMapping parent, String currentKey, String path, String fragment)
				throws CustomException;
	}

	private final class PathMapping extends TreeMap<String, PathMapping> {

		private static final long serialVersionUID = 1L;

		private Map<HttpMethod, APIInfo> apiInfos;

		public void addAPIInfo(APIInfo apiInfo) throws CustomException {
			HttpMethod method = apiInfo.getMapping().getMethod();
			if (apiInfos == null) {
				synchronized (this) {
					if (apiInfos == null) {
						apiInfos = new TreeMap<HttpMethod, APIInfo>();
						apiInfos.put(method, apiInfo);
						return;
					}
				}
			}

			synchronized (apiInfos) {
				if (apiInfos.get(method) != null) {
					throw new CustomException("重复的method: " + method);
				}
				apiInfos.put(method, apiInfo);
			}
		}

		public APIInfo getAPIInfo(HttpMethod method) throws CustomException {
			if (apiInfos == null) {
				throw new CustomException("Method map is empty!");
			}
			APIInfo apiInfo = apiInfos.get(method);
			if (apiInfo == null) {
				throw new NullPointerException("Method ：" + method + "， 未找到!");
			}
			return apiInfo;
		}

		public PathMapping(Comparator<? super String> comparator) {
//			TODO: 未正常使用比较器会导致TreeMap get方法获取的错乱
//			super(comparator);
		}
	}

	/**
	 * 未正常使用比较器会导致TreeMap get方法获取的错乱
	 *
	 */
	private static final class PathMappingComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
