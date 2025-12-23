package com.learnease.server.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/*
    Interceptor responsible for logging incoming http requests
    and outgoing responses

    this interceptor executes before and after controller method
    allowing us to catch the metadata , response status ,
    and more details
 */


@Slf4j
@Component
public class RequestHeaderInterceptor implements HandlerInterceptor {

    /*
        Executes before the Controller method is invoked
        -- Returning true allows the request to proceed further.
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Store request start time for execution time calculation
        request.setAttribute("startTime" , System.currentTimeMillis());
        log.info("---------------- Request Start ---------------");
        log.info("Request URl: {}" ,request.getRequestURI());
        log.info("Method Type: {}" ,request.getMethod());
        return true;
    }

    /*
        Executes after ther request has been fully processed
        and the response has been generated

        - Calculate total request processing time
        - Log response status
        - Log any exception that occurred during request handling

        you can ctr + click for more details
     */

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long timeTaken = System.currentTimeMillis() - startTime;

        log.info("Status: {}", response.getStatus());
        log.info("Time Taken: {} ms", timeTaken);

        if (ex != null) {
            log.error("Exception occurred", ex);
        }

        log.info("---------------- Request End ---------------");

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
