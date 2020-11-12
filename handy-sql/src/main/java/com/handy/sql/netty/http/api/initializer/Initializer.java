package com.handy.sql.netty.http.api.initializer;

import com.handy.sql.netty.exception.CustomException;

public interface Initializer {

	void init() throws CustomException;
}
