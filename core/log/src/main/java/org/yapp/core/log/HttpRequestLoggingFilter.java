package org.yapp.core.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpRequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIP = request.getRemoteAddr();

        log.info(
            "ThreadName: {}, ThreadId: {}, RequestId: {} - Incoming HTTP Request - Method: {}, URI: {}{}, Client IP: {}",
            Thread.currentThread().getName(),
            Thread.currentThread().threadId(),
            requestId,
            method,
            requestURI,
            (queryString != null ? "?" + queryString : ""),
            clientIP);

        if (log.isDebugEnabled()) {
            String headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(header -> header, request::getHeader))
                .toString();
            log.debug("ThreadName: {}, ThreadId: {}, RequestId: {} - HTTP Request Headers: {}",
                Thread.currentThread().getName(),
                Thread.currentThread().threadId(),
                requestId, headers);
        }

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error(
                "ThreadName: {}, ThreadId: {}, RequestId: {} - Error occurred while processing the HTTP request",
                Thread.currentThread().getName(),
                Thread.currentThread().threadId(),
                requestId,
                ex);

            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info(
                "ThreadName: {}, ThreadId: {}, RequestId: {} - Completed HTTP Request - Method: {}, URI: {}{}, Duration: {} ms",
                Thread.currentThread().getName(),
                Thread.currentThread().threadId(),
                requestId,
                method,
                requestURI,
                (queryString != null ? "?" + queryString : ""),
                duration);
            MDC.clear();
        }
    }
}