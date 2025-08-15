plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("application")
}

application {
    mainClass.set("com.service.user.UserApplicationKt")
}

springBoot {
    mainClass.set("com.service.user.UserApplicationKt")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set("com.service.user.UserApplicationKt")
}

val mapStructVersion = "1.6.0"

dependencies {
    implementation(project(":module_common"))
    implementation(project(":module_grpc"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-core")
    implementation("org.springframework.security:spring-security-oauth2-client")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    kapt("org.mapstruct:mapstruct-processor:$mapStructVersion")
}

kotlin {
    sourceSets["main"].kotlin.srcDir("build/generated/source/kapt/main")
}

tasks.named("bootRun") {
    enabled = true
}
