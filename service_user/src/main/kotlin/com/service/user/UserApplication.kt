package com.service.user

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.service"])
class UserApplication

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger(UserApplication::class.java)
    log.info("Starting MainApplication...")
    runApplication<UserApplication>(*args)
}
