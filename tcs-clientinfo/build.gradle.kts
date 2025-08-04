plugins {
    java
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.tcs"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val mapstruct = "1.6.3"
val lombokMapstruct = "0.2.0"
val camelVersion = "4.13.0"
val jakartaValidation = "3.1.1"
val mindrot = "0.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.mindrot:jbcrypt:$mindrot")

    implementation("org.apache.camel.springboot:camel-spring-boot-starter:$camelVersion")
    implementation("org.apache.camel:camel-core:$camelVersion")
    implementation("org.apache.camel:camel-jackson:$camelVersion")
    implementation("org.apache.camel:camel-jacksonxml:$camelVersion")
    implementation("org.apache.camel.springboot:camel-jackson-starter:$camelVersion")
    implementation("org.apache.camel:camel-sql:$camelVersion")
    implementation("org.apache.camel:camel-spring:$camelVersion")
    implementation("org.apache.camel.springboot:camel-kafka-starter:$camelVersion")
    implementation("org.apache.camel.springboot:camel-jdbc-starter:$camelVersion")
    implementation("org.apache.camel:camel-activemq:$camelVersion")
    implementation("org.apache.camel:camel-disruptor:$camelVersion")
    implementation("org.apache.camel:camel-timer:$camelVersion")
    implementation("org.apache.camel.springboot:camel-spring-redis-starter:$camelVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("jakarta.validation:jakarta.validation-api:$jakartaValidation")

    implementation("org.mapstruct:mapstruct:$mapstruct")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstruct")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapstruct")

    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.mindrot:jbcrypt:$mindrot")
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
    testImplementation("org.awaitility:awaitility")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
