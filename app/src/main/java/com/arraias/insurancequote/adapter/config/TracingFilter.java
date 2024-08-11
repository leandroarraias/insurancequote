package com.arraias.insurancequote.adapter.config;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TracingFilter extends OncePerRequestFilter {

	private final Tracer tracer;

	@Autowired
	public TracingFilter(Tracer tracer) {
		this.tracer = tracer;
	}


	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	throws ServletException, IOException {

		response.addHeader("spanId", tracer.currentSpan().context().spanId());
		response.addHeader("traceId", tracer.currentSpan().context().traceId());

		filterChain.doFilter(request, response);

	}
}
