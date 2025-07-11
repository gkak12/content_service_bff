package com.service.admin

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.service"])
class AdminApplication

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger(AdminApplication::class.java)
    log.info("Starting MainApplication...")
    runApplication<AdminApplication>(*args)
}
