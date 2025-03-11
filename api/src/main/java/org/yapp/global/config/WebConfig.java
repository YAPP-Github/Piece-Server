package org.yapp.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yapp.core.log.HttpRequestLoggingFilter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<HttpRequestLoggingFilter> loggingFilterRegistration(
        HttpRequestLoggingFilter filter) {
        
        FilterRegistrationBean<HttpRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}