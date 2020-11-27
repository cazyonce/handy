package com.handy.sql.netty.http.api.processor.dynamic.adapter.post;

import java.util.ArrayList;
import com.handy.sql.netty.exception.CustomException;

public class PostProcessAdapter extends ArrayList<InterfacePostProcessAdapter> {

	private static final long serialVersionUID = 1L;

	public Class<? extends InterfacePostProcessAdapter> adaptive(String mimeType) throws CustomException {
		for (InterfacePostProcessAdapter adapter : this) {
			if (mimeType.startsWith(adapter.contentType())) {
				return adapter.getClass();
			}
		}
		throw new CustomException("Http Post对应的文本类型处理器未找到：" + mimeType);
	}
}
