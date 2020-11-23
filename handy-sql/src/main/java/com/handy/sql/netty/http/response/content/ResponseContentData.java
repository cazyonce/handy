package com.handy.sql.netty.http.response.content;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.ToString;

@ToString
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class ResponseContentData<T> extends ResponseContent {

	@Getter
	@JsonProperty(access = Access.AUTO)
	private T data;

	public ResponseContentData<T> success(T data) {
		return createInstance(ResponseCode.SUCCESS, data);
	}

	private ResponseContentData<T> createInstance(ResponseCode r, T data) {
		return new ResponseContentData<T>(r, data);
	}

	public ResponseContentData(ResponseCode r, T data) {
		super(r.code, r.describe);
		this.data = data;
	}

	public ResponseContentData() {
	}
}