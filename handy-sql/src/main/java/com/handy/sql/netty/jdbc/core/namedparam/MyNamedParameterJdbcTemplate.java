package com.handy.sql.netty.jdbc.core.namedparam;

import java.util.ArrayList;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.http.api.pojo.MapSqlParameterSourcePOJO;

public class MyNamedParameterJdbcTemplate extends NamedParameterJdbcTemplate {

	public MyNamedParameterJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	public int updateExt(String sql, Object obj, KeyHolder generatedKeyHolder) throws DataAccessException {
		MapSqlParameterSource parameters = GlobalProvide.DB_OBJECT_MAPPER.convertValue(obj,
				MapSqlParameterSourcePOJO.class);
		return super.update(sql, parameters, generatedKeyHolder);
	}

	public <T> int[] batchUpdateExt(String sql, ArrayList<T> batchArgs) {
		if (batchArgs.isEmpty()) {
			return new int[0];
		}

		MapSqlParameterSource[] objs = new MapSqlParameterSource[batchArgs.size()];
		for (int i = 0; i < batchArgs.size(); i++) {
			objs[i] = toMapSqlParameterSource(batchArgs.get(i));
		}

		return batchUpdate(sql, objs);
	}

	private <T> MapSqlParameterSource toMapSqlParameterSource(T t) {
		return GlobalProvide.DB_OBJECT_MAPPER.convertValue(t, MapSqlParameterSourcePOJO.class);
	}
}
