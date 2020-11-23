package com.handy.sql.netty.http.response.content;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class ResponseContent {

	@Getter
	@JsonProperty(access = Access.AUTO)
	protected int code;

	@Getter
	@JsonProperty(access = Access.AUTO)
	protected String message;

	public enum ResponseCode {
		SUCCESS(1, "操作成功"), NO_LOGIN(2, "没有登录"), FAIL(10, "操作失败"), NO_PERMISSION(3,
				"权限不足"), INVALID_ARGUMENT(4, "非法参数");
		public final int code;
		protected final String describe;

		private ResponseCode(int code, String describe) {
			this.code = code;
			this.describe = describe;
		}
	}

	public ResponseContent success() {
		return createInstance(ResponseCode.SUCCESS);
	}

	public ResponseContent fail() {
		return createInstance(ResponseCode.FAIL);
	}

	public ResponseContent fail(String message) {
		return createInstance(ResponseCode.FAIL.code, message);
	}

	public ResponseContent noLogin() {
		return createInstance(ResponseCode.NO_LOGIN);
	}

	public ResponseContent noPermission() {
		return createInstance(ResponseCode.NO_PERMISSION);
	}

	public ResponseContent parameterNotValid(String message) {
		return createInstance(ResponseCode.INVALID_ARGUMENT.code, message);
	}
	
	private ResponseContent createInstance(int code, String message) {
		return new ResponseContent(code, message);
	}

	private ResponseContent createInstance(ResponseCode r) {
		return new ResponseContent(r.code, r.describe);
	}

	public ResponseContent(ResponseCode r) {
		this.code = r.code;
		this.message = r.describe;
	}

	public ResponseContent(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResponseContent() {
	}

}