/**
 * @#ApplicationConfiguration.java October 24, 2018
 */
package com.ravaan.prometheus.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

/**
 * The Class ApplicationConfiguration.
 */
@Configuration
public class ApplicationConfiguration {

	/**
	 * Configurer.
	 *
	 * @param applicationName the application name
	 * @return the meter registry customizer
	 */
	@Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(
			@Value("${spring.application.name}") final String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}

	/**
	 * Process memory metrics.
	 *
	 * @return the meter binder
	 */
	@Bean
	public MeterBinder processMemoryMetrics() {
		return new ProcessMemoryMetrics();
	}

	/**
	 * Process thread metrics.
	 *
	 * @return the meter binder
	 */
	@Bean
	public MeterBinder processThreadMetrics() {
		return new ProcessThreadMetrics();
	}
}
