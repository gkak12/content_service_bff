package com.service.common.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableEncryptableProperties
class JasyptConfig {

    @Bean("jasyptEncryptor")
    fun stringEncryptor() :StringEncryptor{
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()
        config.password = System.getenv("JASYPT_KEY")
        config.algorithm = "PBEWithMD5AndDES"
        config.setKeyObtentionIterations("1000")
        config.setPoolSize("1")
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator")
        config.stringOutputType = "base64"
        encryptor.setConfig(config)
        return encryptor
    }
}
