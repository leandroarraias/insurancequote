package com.arraias.insurancequote.adapter.web.exceptionhandler;

import com.arraias.insurancequote.application.exception.InvalidOfferRuntimeException;
import com.arraias.insurancequote.application.exception.InvalidProductRuntimeException;
import com.arraias.insurancequote.application.exception.InvalidQuoteRequestRuntimeException;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@Autowired
	private Tracer tracer;

	@ExceptionHandler(HandlerMethodValidationException.class)
	public final ResponseEntity<Boolean> handleException(HandlerMethodValidationException ex) {
		return new ResponseEntity<>(Boolean.FALSE, FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public final ResponseEntity<InvalidDataMessage> handleException(
			MethodArgumentNotValidException ex, WebRequest webRequest) {

		HttpServletRequest request = ((ServletWebRequest)webRequest).getRequest();

		var invalidData = new ArrayList<String>();

		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			invalidData.add(fe.getField());
		}

		log.error("Invalid request. Path: {} {}; invalid data: {}", request.getMethod(), request.getRequestURI(), invalidData);

		InvalidDataMessage errorMessage = new InvalidDataMessage("Invalid data.", invalidData, getSpanId(), getTraceId());

		return new ResponseEntity<>(errorMessage, BAD_REQUEST);

	}

	@ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleException(Exception ex) {

		var st = escapeHtml4(getStackTrace(ex));

		log.error("Error processing the operation.\n{}", st);

		ErrorMessage errorMessage = new ErrorMessage(
				"Error processing the operation.", getSpanId(), getTraceId());

		return new ResponseEntity<>(errorMessage, INTERNAL_SERVER_ERROR);

    }

	@ExceptionHandler(InvalidProductRuntimeException.class)
	public final ResponseEntity<ErrorMessage> handleException(InvalidProductRuntimeException ex) {

		log.error(ex.getMessage(), ex);

		ErrorMessage errorMessage = new ErrorMessage(
				"Invalid product id: " + ex.getProductId(), getSpanId(), getTraceId());

		return new ResponseEntity<>(errorMessage, BAD_REQUEST);

	}

	@ExceptionHandler(InvalidOfferRuntimeException.class)
	public final ResponseEntity<ErrorMessage> handleException(InvalidOfferRuntimeException ex) {
		log.error(ex.getMessage(), ex);
		ErrorMessage errorMessage = new ErrorMessage("Invalid offer id: " + ex.getOfferId(), getSpanId(), getTraceId());
		return new ResponseEntity<>(errorMessage, BAD_REQUEST);
	}

	@ExceptionHandler(InvalidQuoteRequestRuntimeException.class)
	public final ResponseEntity<ErrorMessage> handleException(InvalidQuoteRequestRuntimeException ex) {
		log.error(ex.getMessage(), ex);
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), getSpanId(), getTraceId());
		return new ResponseEntity<>(errorMessage, BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public final ResponseEntity<ErrorMessage> handleException(HttpMessageNotReadableException ex) {

		var exs = escapeHtml4(getStackTrace(ex));

		log.error("Fail reading payload. \n{}", exs);

		ErrorMessage errorMessage = new ErrorMessage("Fail reading payload.", getSpanId(), getTraceId());

		return new ResponseEntity<>(errorMessage, BAD_REQUEST);

	}

	private String getSpanId() {
		if (getContext() == null) {
			return null;
		} else {
			return getContext().spanId();
		}
	}

	private String getTraceId() {
		if (getContext() == null) {
			return null;
		} else {
			return getContext().traceId();
		}
	}

	private TraceContext getContext() {
		if (tracer == null || tracer.currentSpan() == null) {
			return null;
		} else {
			return tracer.currentSpan().context();
		}
	}
}
