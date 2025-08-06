plugins {
    java
    kotlin("jvm") version "1.9.23"
    kotlin("kapt") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

val springBootVersion = "3.3.2"
val starterVersion = "3.3.2"
val jwtTokenVersion = "0.11.5"
val mapStructVersion = "1.6.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "groovy")
    apply(plugin = "idea")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    group = "com.service"
    version = "1.0.0"

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    configurations.compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    dependencies {
        // Spring Boot Starters
        implementation("org.springframework.boot:spring-boot-starter-webflux:$starterVersion")
        implementation("org.springframework.boot:spring-boot-starter-validation:$starterVersion")
        implementation("org.springframework.boot:spring-boot-starter-security:$starterVersion")

        // JWT
        implementation("io.jsonwebtoken:jjwt-api:$jwtTokenVersion")
        implementation("io.jsonwebtoken:jjwt-impl:$jwtTokenVersion")
        implementation("io.jsonwebtoken:jjwt-jackson:$jwtTokenVersion")

        // Lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        // Jasypt
        implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

        // Testing
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        // JSON
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("com.hubspot.jackson:jackson-datatype-protobuf:0.9.15")

        // Native Query
        implementation("com.github.jsqlparser:jsqlparser:4.9")

        // Base64
        implementation("commons-codec:commons-codec:1.17.0")

        // MapStruct
        implementation("org.mapstruct:mapstruct:$mapStructVersion")
        annotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")

        // Commons Lang3
        implementation("org.apache.commons:commons-lang3:3.16.0")

        // UUID Generator
        implementation("com.fasterxml.uuid:java-uuid-generator:5.1.0")

        // p6spy
        implementation("p6spy:p6spy:3.9.1")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

// 서브 프로젝트
project(":service_admin") {
    dependencies {
        compileOnly(project(":module_common"))
    }
}

project(":service_user") {
    dependencies {
        compileOnly(project(":module_common"))
    }
}

// 루트에서 bootJar 비활성화
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

// 커스텀 실행 태스크
tasks.register<Exec>("runBoot") {
    commandLine("gradlew.bat", "run")
}

tasks.register("run") {
    doLast {
        println("==============RUN==============")
    }
}

tasks.register("runAll") {
    val profile = if (project.hasProperty("profile")) project.property("profile") as String else "local"
    dependsOn(":bff_admin:bootRun", ":bff_user:bootRun")
    doFirst {
        configureBootRunArgs(":bff_admin", profile)
        configureBootRunArgs(":bff_user", profile)
    }
    enabled = false
}

tasks.register("contentRun") {
    configureBootRunArgs(":bff_admin")
    configureBootRunArgs(":bff_user")
    finalizedBy(":bff_admin:bootRun", ":bff_user:bootRun")
}

fun configureBootRunArgs(projectPath: String, profile: String = "local") {
    val bootRun = project(projectPath).tasks.findByName("bootRun")
    bootRun?.let {
        (it as? JavaExec)?.args = listOf("--spring.profiles.active=$profile")
    }
}
