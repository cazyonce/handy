package com.handy.sql.netty.http.channel;

import java.util.ArrayList;
import java.util.List;

import com.handy.sql.netty.exception.CustomException;

public class APIChannelPipeline<T> {

	private final List<AbstractAPIChannel<T>> list = new ArrayList<AbstractAPIChannel<T>>();
	
	public void initChannel(List<AbstractAPIChannel<T>> list) throws CustomException {

	}
	
	public APIChannelPipeline<T> addLast(AbstractAPIChannel<T> ch) {
		list.add(ch);
		return this;
	}
	
	public Object process(T t) throws CustomException {
		for (AbstractAPIChannel<T> ch : list) {
			if (ch.read(t)) {
				return ch.process(t);
			}
		}
		throw new CustomException("未处理的数据");
	}
}
