package com.handy.sql.netty.http.mapping;

import java.util.HashMap;

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

//	@Override
//	public String toString() {
//		return "PathMapping [str=" + str + "]";
//	}
	
}
