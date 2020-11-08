package com.handy.sql.netty.http.api.mapping;

import java.util.HashMap;

import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class PathMapping extends HashMap<String, PathMapping> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String path;

	private AbstractHttpProcessor processor;
//	@Override
//	public String toString() {
//		return "PathMapping [str=" + str + "]";
//	}
	
}
