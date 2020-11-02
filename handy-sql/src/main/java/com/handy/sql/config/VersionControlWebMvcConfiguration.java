package com.handy.sql.config;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 配置使用自定义版本url处理器
 *
 * @author Hanqi <jpanda@aliyun.com>
 * @since 2019/3/13 16:11
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class VersionControlWebMvcConfiguration implements WebMvcRegistrations {	
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new APIControlRequestMappingHandlerMapping();
    }
}