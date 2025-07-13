plugins {
	kotlin("jvm")
}

dependencies {
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
	implementation(kotlin("stdlib"))

	// Redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	enabled = false
}
tasks.named("bootRun") {
	enabled = false
}
