package com.example.librarianassistant.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that attaches a correlation ID to every inbound request.
 *
 * <p>The ID is taken from the {@code X-Correlation-ID} request header when present,
 * or a new random UUID is generated. The value is:
 * <ul>
 *   <li>stored in SLF4J's {@link MDC} under the key {@code correlationId} so it
 *       appears automatically in every log statement for the duration of the request;</li>
 *   <li>echoed back to the caller in the {@code X-Correlation-ID} response header.</li>
 * </ul>
 * MDC is always cleared after the response is committed to prevent leakage between
 * requests in thread-pool environments.
 */
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    static final String MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(MDC_KEY, correlationId);
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }
}
