package com.learnease.server.config;


import com.learnease.server.interceptor.RequestHeaderInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
    - Register custom interceptors with the Spring MVC lifecycle

    Interceptors are NOT auto-registered by Spring,
    so explicit registration is required.
 */

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // Custom interceptor for request/response logging
    private final RequestHeaderInterceptor requestHeaderInterceptor;


    /**
     * Registers application interceptors and defines
     * URL patterns they should be applied to.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestHeaderInterceptor)
                .addPathPatterns("/**");
    }
}
