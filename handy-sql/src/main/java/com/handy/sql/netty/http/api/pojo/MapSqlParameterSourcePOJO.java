package com.handy.sql.netty.http.api.pojo;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.handy.sql.netty.jackon.converter.MapSqlParameterSourcePOJOConverter;

@JsonDeserialize(converter = MapSqlParameterSourcePOJOConverter.class)
public class MapSqlParameterSourcePOJO extends MapSqlParameterSource {

}
