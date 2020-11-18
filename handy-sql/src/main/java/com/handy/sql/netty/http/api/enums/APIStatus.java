package com.handy.sql.netty.http.api.enums;

public enum APIStatus {

	DISABLED(0), ENABLED(1);

	public final int STATUS;

	private APIStatus(int status) {
		this.STATUS = status;
	}

}
