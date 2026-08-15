import com.github.gradle.node.npm.task.NpxTask

plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.node-gradle.node") version "7.1.0"
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
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.springframework.session:spring-session-core")
	implementation("org.json:json:20260719")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
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
