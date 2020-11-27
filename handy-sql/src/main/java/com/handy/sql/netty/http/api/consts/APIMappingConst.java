package com.handy.sql.netty.http.api.consts;

import com.handy.sql.netty.http.api.mapping.APIMapping;

public interface APIMappingConst {

	APIMapping SYTEM_REGISTER_API_POST = APIMapping.post("/sys_api/register");
	
	APIMapping SYTEM_REGISTER_API_GET = APIMapping.get("/sys_api/register");
	
	APIMapping SYTEM_API_MAPPING_GET = APIMapping.get("/sys_api/mapping");
	
	APIMapping SYTEM_API_HEADER_NAME_OPTIONS_GET = APIMapping.get("/sys_api/header/name/options");
	
	APIMapping SYTEM_API_HEADER_VALUE_OPTIONS_GET = APIMapping.get("/sys_api/header/value/options");
	
}
