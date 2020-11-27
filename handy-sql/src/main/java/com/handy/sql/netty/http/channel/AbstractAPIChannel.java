package com.handy.sql.netty.http.channel;

import com.handy.sql.netty.exception.CustomException;

public abstract class AbstractAPIChannel<T> {

	public abstract Object process(T t) throws CustomException;
	
	public abstract boolean read(T t);
}
