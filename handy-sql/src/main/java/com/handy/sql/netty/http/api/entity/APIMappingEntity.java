package com.handy.sql.netty.http.api.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class APIMappingEntity {

	private Integer id;

	private String path;

	private String method;

	private String name;

	private String describe;

	private Integer responseCode;

	private String responseReason;

	private Integer status;

	private String executeSQL;

	private LocalDateTime createTime;

	public APIMappingEntity(String path, String method, String name, String describe, Integer responseCode,
			Integer status, String executeSQL, String responseReason) {
		this.path = path;
		this.method = method;
		this.name = name;
		this.describe = describe;
		this.responseCode = responseCode;
		this.status = status;
		this.executeSQL = executeSQL;
		this.responseReason = responseReason;
	}

}
