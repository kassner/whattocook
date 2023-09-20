import java.nio.file.Files
import kotlin.io.path.Path
import org.siouan.frontendgradleplugin.infrastructure.gradle.InstallFrontendTask

plugins {
	java
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.siouan.frontend-jdk17")
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
}

tasks.withType<Test> {
	useJUnitPlatform()
}

frontend {
	nodeVersion.set("18.17.1")
	assembleScript.set("run build")
	verboseModeEnabled.set(true)
}

// @TODO assembleFrontend should not run during "./gradlew test"
tasks.processResources {
	dependsOn("assembleFrontend")
}

tasks.named<InstallFrontendTask>("installFrontend") {
	val ciPlatformPresent = providers.environmentVariable("CI").isPresent()
	val lockFilePath = "${projectDir}/package-lock.json"
	val retainedMetadataFileNames: Set<String>
	if (ciPlatformPresent) {
		// If the host is a CI platform, execute a strict install of dependencies based on the lock file.
		installScript.set("ci")
		retainedMetadataFileNames = setOf(lockFilePath)
	} else {
		// The naive configuration below allows to skip the task if the last successful execution did not change neither
		// the package.json file, nor the package-lock.json file, nor the node_modules directory. Any other scenario
		// where for example the lock file is regenerated will lead to another execution before the task is "up-to-date"
		// because the lock file is both an input and an output of the task.
		val acceptableMetadataFileNames = listOf(lockFilePath, "${projectDir}/yarn.lock")
		retainedMetadataFileNames = mutableSetOf("${projectDir}/package.json")
		for (acceptableMetadataFileName in acceptableMetadataFileNames) {
			if (Files.exists(Path(acceptableMetadataFileName))) {
				retainedMetadataFileNames.add(acceptableMetadataFileName)
				break
			}
		}
		outputs.file(lockFilePath).withPropertyName("lockFile")
	}
	inputs.files(retainedMetadataFileNames).withPropertyName("metadataFiles")
	outputs.dir("${projectDir}/node_modules").withPropertyName("nodeModulesDirectory")
}
