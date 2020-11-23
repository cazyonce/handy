package com.handy.sql.netty.http.api.enums;

import com.handy.sql.netty.exception.CustomException;

public enum APIStatus {

	DISABLED(0), ENABLED(1);

	public final int STATUS;

	private APIStatus(int status) {
		this.STATUS = status;
	}

	public static APIStatus valueOf(int status) throws CustomException {
		for (APIStatus sta : APIStatus.values()) {
			if (status == sta.STATUS) {
				return sta;
			}
		}
		throw new CustomException("未知的状态：" + status);
	}
}
