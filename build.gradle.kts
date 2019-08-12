import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.1.7.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version "1.3.41"
	kotlin("plugin.spring") version "1.3.41"
}

group = "dashfwd"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Cache2K: see https://cache2k.org/docs/latest/user-guide.html#getting-started

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-freemarker")
	implementation("org.springframework.boot:spring-boot-starter-quartz")
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Cache2K dependencies
	// ------------------------------------
	// see https://cache2k.org/docs/latest/user-guide.html#enable-cache2k-spring-support
	val cache2kVersion = "1.2.2.Final"
	implementation("org.cache2k:cache2k-api:$cache2kVersion")
	runtimeOnly("org.cache2k:cache2k-core:$cache2kVersion")
	implementation("org.cache2k:cache2k-spring:$cache2kVersion")

	// Spring Boot Cache
	// ------------------------------------
	implementation("org.springframework.boot:spring-boot-starter-cache")


	// (Added) Nicer I/O utilities
	implementation("commons-io:commons-io:2.2")

	// (Added) Needed to deserialize Kotlin data classes using Jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")


}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
