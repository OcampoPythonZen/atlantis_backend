plugins {
	java
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.flywaydb.flyway") version "10.21.0"
	id("com.google.protobuf") version "0.9.4"
}

group = "com.atlantis"
version = "0.0.1-SNAPSHOT"
description = "Atlantis  project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// JWT Token Subproject
	implementation(project(":jwt-token"))

	// Spring Boot
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	// Database
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")

	// gRPC & Protocol Buffers
	implementation("net.devh:grpc-spring-boot-starter:3.1.0.RELEASE")
	implementation("io.grpc:grpc-protobuf:1.62.2")
	implementation("io.grpc:grpc-stub:1.62.2")
	implementation("com.google.protobuf:protobuf-java:3.25.2")
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	compileOnly("jakarta.annotation:jakarta.annotation-api:3.0.0")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("io.grpc:grpc-testing:1.62.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<ProcessResources> {
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

flyway {
	url = "jdbc:postgresql://localhost:5432/nutritionist_db"
	user = "postgres"
	password = "postgres"
	locations = arrayOf("classpath:db/migration")
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.25.2"
	}
	plugins {
		create("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.62.2"
		}
	}
	generateProtoTasks {
		all().forEach { task ->
			task.plugins {
				create("grpc")
			}
		}
	}
}

sourceSets {
	main {
		proto {
			srcDir("src/main/proto")
		}
	}
}
