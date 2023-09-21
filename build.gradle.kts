import com.github.gradle.node.npm.task.NpxTask

plugins {
	java
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	id("com.github.node-gradle.node") version "7.0.0"
}

group = "se.kassner"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.session:spring-session-core")
	implementation("org.json:json:20230618")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.h2database:h2")
	testImplementation("org.hamcrest:hamcrest")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<NpxTask>("buildFrontend") {
	dependsOn("npmInstall")
	command.set("npm")
	args.set(listOf("run", "build"))
}

tasks.named<Jar>("bootJar") {
	dependsOn("buildFrontend")
}
