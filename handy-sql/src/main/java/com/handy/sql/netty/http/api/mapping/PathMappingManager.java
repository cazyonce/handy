package com.handy.sql.netty.http.api.mapping;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handy.sql.netty.http.api.initializer.Initializer;

public class PathMappingManager implements Initializer {

	public PathMapping get(PathMapping root, String path) throws Exception {
		return process(root, path, new Processor() {

			@Override
			protected PathMapping process(PathMapping parent, String currentKey, String path, boolean lastFragment)
					throws Exception {
				if (currentKey == null) { // key为null 表示不需要或已经找到了
					return parent;
				}
				PathMapping mapping = parent.get(currentKey);
				if (mapping == null) {
					throw new Exception("path: " + path + ", 未找到");
				}
				return mapping;
			}
		});
	}

	public void register(PathMapping root, String path) {
		try {
			process(root, path, new Processor() {

				@Override
				protected PathMapping process(PathMapping parent, String currentKey, String path, boolean lastFragment)
						throws Exception {
					if (currentKey == null) { // 如果key为null 表示不需要作任何操作
						return null;
					}
					PathMapping mapping = parent.get(currentKey);
					if (Objects.isNull(mapping)) {
						parent.put(currentKey, mapping = new PathMapping());
					} else if (lastFragment) {
						if (mapping.getPath() != null) {
							throw new Exception("old path: " + mapping.getPath() + ", new path: " + path + ", 重复的path");
						}
						mapping.setPath(path);
					}
					return mapping;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PathMapping process(PathMapping root, String path, Processor processor) throws Exception {
		String key = null;
		String[] fragments = path.split("\\/");
		boolean slashSufix = path.endsWith("/"); // 是否斜杠结尾
		int i = 0;
		for (String fragment : fragments) {
			if (fragment.isEmpty()) { // 表示第一级片段
				key = "/";
				fragment = "/";
			} else if (fragment.startsWith("{") && fragment.endsWith("}") && fragment.length() > 2) { // 参数
				key = "*";
			} else {
				key = fragment;
			}
			// lastFragment = 判断当前path片段是否最后一个并且不是斜杠结尾
			root = processor.process(root, key, path, i == fragments.length - 1 && !slashSufix);
			++i;
		}

		// path是以/结尾
		if (slashSufix) {
			key = "/";
			return processor.process(root, key, path, true);
		}
		key = null;
		return processor.process(root, key, path, true);
	}

	private abstract class Processor {
		protected abstract PathMapping process(PathMapping parent, String currentKey, String path, boolean lastFragment)
				throws Exception;
	}

	public static void main(String[] args) throws Exception {
		PathMappingManager mappingUtil = new PathMappingManager();
		ObjectMapper mapper = new ObjectMapper();
		PathMapping root = new PathMapping();

		String path = "/api/{aaa}/{bbb}/";
		mappingUtil.register(root, path);
		System.out.println(mappingUtil.get(root, path));

		path = "/api/{aaa}";
		mappingUtil.register(root, path);
		System.out.println(mappingUtil.get(root, path));

		path = "/api/{bbb}";
		mappingUtil.register(root, path);
		System.out.println(mappingUtil.get(root, path));

//		 存在这种情况，但是暂时不确定是否要处理，后面根据实际情况再看
//		 path = "/api/{aaa}/";
//		 System.out.println(mappingUtil.get(root, path));

		path = "/api/{aaa}/{bbb}";
		mappingUtil.register(root, path);
		System.out.println(mappingUtil.get(root, path));

		path = "/api/{}";
		mappingUtil.register(root, path);
		System.out.println(mappingUtil.get(root, path));

//		System.out.println(root);
//		path = "/api";
//		mappingUtil.register(root, path);
//		System.out.println(mappingUtil.get(root, path));
//		
//		path = "/";
//		mappingUtil.register(root, path);
//		System.out.println(mappingUtil.get(root, path));
//		
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
		System.out.println("--------------------------");

		System.out.println(Arrays.toString(path.split("\\/")));
//		throw new Exception("path: " + path + ", 重复的path");
//		boolean isMatch1 = Pattern.matches("^\\{.{1,}\\}$", "{1}");
//		boolean isMatch1 = Pattern.matches("^\\{(.)\\}$",path);
//		System.out.println(isMatch1);
//		System.out.println(path.replaceAll("^\\{.*\\}$", "*"));
//		System.out.println(Arrays.toString(path.split("(?=\\/)")));
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
