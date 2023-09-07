plugins {
	java
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.session:spring-session-core")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
