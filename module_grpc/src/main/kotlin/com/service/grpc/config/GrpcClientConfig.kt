package com.service.grpc.config

import net.devh.boot.grpc.client.config.GrpcChannelProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcClientConfig {

    @Bean
    @ConfigurationProperties(prefix = "grpc.client.admin-service")
    fun adminServiceChannelProperties(): GrpcChannelProperties {
        return GrpcChannelProperties()
    }
}
