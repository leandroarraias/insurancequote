package com.arraias.insurancequote.adapter.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.LogRecordProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.SdkLoggerProviderBuilder;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static io.opentelemetry.semconv.ResourceAttributes.SERVICE_NAME;

@Configuration
public class OpenTelemetryConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${otel.endpoint}")
	private String otelEndpoint;

	@Bean
	OpenTelemetry openTelemetry(
			SdkLoggerProvider sdkLoggerProvider,
			SdkTracerProvider sdkTracerProvider,
			ContextPropagators contextPropagators) {

		OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
				.setLoggerProvider(sdkLoggerProvider)
				.setTracerProvider(sdkTracerProvider)
				.setPropagators(contextPropagators)
				.build();

		OpenTelemetryAppender.install(openTelemetrySdk);

		return openTelemetrySdk;

	}

	@Bean
	SdkLoggerProvider otelSdkLoggerProvider(
			Environment environment,
			ObjectProvider<LogRecordProcessor> logRecordProcessors) {

		Resource springResource = Resource.create(Attributes.of(SERVICE_NAME, applicationName));
		Resource resource = Resource.getDefault().merge(springResource);
		SdkLoggerProviderBuilder builder = SdkLoggerProvider.builder().setResource(resource);
		logRecordProcessors.orderedStream().forEach(builder::addLogRecordProcessor);

		return builder.build();

	}

	@Bean
	LogRecordProcessor otelLogRecordProcessor() {

		OtlpGrpcLogRecordExporter logRecordExporter = OtlpGrpcLogRecordExporter
				.builder()
				.setEndpoint(otelEndpoint)
				.build();

		return BatchLogRecordProcessor
				.builder(logRecordExporter)
				.build();

	}
}
