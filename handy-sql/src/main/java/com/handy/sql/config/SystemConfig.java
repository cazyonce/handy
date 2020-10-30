package com.handy.sql.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "amass.system")
@Getter
@Setter
public class SystemConfig {

	/**
	 * 项目环境 dev：开发环境
	 */
	private String active;

	private String qqAPPID;

	private String qqAPPKey;

	private String qqCallbackUrl;

	private String userAvatarDir;

	private String userAvatarDefault;

	private String aliyunDnsRegionId;

	private String aliyunDnsAccessKeyId;

	private String aliyunDnsAccessKeySecret;

	private String aliyunDnsDomainName;

	private int proxyServerPort = 8881;
	
	private int proxyEntryServerPort = 8880;
}