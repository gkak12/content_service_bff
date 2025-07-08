package com.service.common.config

import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationFailedEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener

@Configuration
class GracefulShutdownConfig {

    class ShutdownListener {

        private val log = LoggerFactory.getLogger(ShutdownListener::class.java)

        @EventListener
        fun onContextClosed(event: ContextClosedEvent) {
            log.info("BFF Application shutdown initiated")
        }

        @PreDestroy
        fun onDestroy() {
            log.info("BFF Application graceful shutdown in progress")
        }

        @EventListener
        fun onShutdownFailure(event: ApplicationFailedEvent) {
            log.error("BFF Application shutdown failed", event.exception)
        }
    }
}
