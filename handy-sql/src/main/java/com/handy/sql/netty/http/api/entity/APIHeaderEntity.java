package com.handy.sql.netty.http.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class APIHeaderEntity {

	private Integer id;

	private Integer apiMappingId;

	private String name;

	private String value;

	private String headerType;

	public APIHeaderEntity(Integer apiMappingId, String name, String value, String headerType) {
		this.apiMappingId = apiMappingId;
		this.name = name;
		this.value = value;
		this.headerType = headerType;
	}

}
