package com.handy.sql.netty.http.api.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.initializer.Initializer;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.api.processor.system.GetRegisterListAPIProcessor;
import com.handy.sql.netty.http.api.processor.system.PostRegisterAPIProcessor;
import com.handy.sql.netty.http.info.APIInfo;

import io.netty.handler.codec.http.HttpMethod;

public final class APIProcessorMappingManager implements Initializer {

	AbstractHttpProcessor[] SYSTEM_PROCESSOR = { PostRegisterAPIProcessor.newInstance(),
			GetRegisterListAPIProcessor.newInstance() };

	public final PathMapping root = new PathMapping();

	public AbstractHttpProcessor get(String path, HttpMethod method) throws CustomException {
		return getPathMapping(path).getProcessor(method);
	}

	public void register(AbstractHttpProcessor processor) throws CustomException {
		registerPathMapping(processor.getPath()).addProcessor(processor);
	}

	public List<APIInfo> getAPIInfoList() {
		return list(new ArrayList<APIInfo>(), root);
	}

	private List<APIInfo> list(List<APIInfo> list, PathMapping root) {
		for (PathMapping pathMapping : root.values()) {
			Map<HttpMethod, AbstractHttpProcessor> processors = pathMapping.processors;
			if (processors != null) {
				for (AbstractHttpProcessor processor : processors.values()) {
					list.add(processor.getApiInfo());
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
		for (AbstractHttpProcessor processor : SYSTEM_PROCESSOR) {
			register(processor);
		}
	}

	private PathMapping getPathMapping(String path) throws CustomException {
		return process(root, path, new PathProcessor() {

			@Override
			protected PathMapping process(PathMapping parent, String currentKey, String path) throws CustomException {
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

	private PathMapping registerPathMapping(String path) throws CustomException {
		return process(root, path, new PathProcessor() {

			@Override
			protected PathMapping process(PathMapping parent, String currentKey, String path) throws CustomException {
				if (currentKey == null) { // 如果key为null 也表示 path mapping注入完成
					return parent;
				}
				synchronized (parent) {
					PathMapping mapping = parent.get(currentKey);
					if (Objects.isNull(mapping)) {
						parent.put(currentKey, mapping = new PathMapping());
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
			root = processor.process(root, key, path);
		}

		// path是以/结尾
		if (slashSufix) {
			key = "/";
			return processor.process(root, key, path);
		}
		// 只有当path为"/aa"时，代码才会执行到这里
		key = null;
		return processor.process(root, key, path);
	}

	private abstract class PathProcessor {
		protected abstract PathMapping process(PathMapping parent, String currentKey, String path)
				throws CustomException;
	}

	private final class PathMapping extends HashMap<String, PathMapping> {

		private static final long serialVersionUID = 1L;

		private Map<HttpMethod, AbstractHttpProcessor> processors;

		public void addProcessor(AbstractHttpProcessor processor) throws CustomException {
			if (processors == null) {
				synchronized (this) {
					if (processors == null) {
						processors = new HashMap<HttpMethod, AbstractHttpProcessor>();
					}
				}
			}

			synchronized (processors) {
				HttpMethod method = processor.getMethod();
				if (processors.get(method) != null) {
					throw new CustomException("重复的method: " + method);
				}
				processors.put(method, processor);
			}
		}

		public AbstractHttpProcessor getProcessor(HttpMethod method) throws CustomException {
			if (processors == null) {
				throw new CustomException("Method map is empty!");
			}
			AbstractHttpProcessor processor = processors.get(method);
			if (processor == null) {
				throw new NullPointerException("Method ：" + method + "， 未找到!");
			}
			return processor;
		}
	}

}
