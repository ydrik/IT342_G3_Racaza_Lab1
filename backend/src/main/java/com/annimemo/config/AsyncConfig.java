package com.annimemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Async Configuration
 * Enables async method execution for email sending
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
