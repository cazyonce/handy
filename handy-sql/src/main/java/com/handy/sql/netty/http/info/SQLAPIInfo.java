package com.handy.sql.netty.http.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SQLAPIInfo extends APIInfo {

	private String executeSQL;
}
