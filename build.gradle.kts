plugins {
    java
    jacoco
    checkstyle
    id("application")
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.jmp"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "com.jmp.userservice.UserMicroserviceApplication"
}

jacoco {
    toolVersion = "0.8.11"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.keycloak:keycloak-spring-boot-starter:${property("keycloak.version")}")
    implementation("org.keycloak:keycloak-admin-client:${property("keycloak.version")}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("springdoc-openapi-webmvc-ui.version")}")
    implementation("org.liquibase:liquibase-core")
    implementation("org.mapstruct:mapstruct:${property("mapstruct.version")}")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstruct.version")}")
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.github.dasniko:testcontainers-keycloak:${property("keycloak.test-container.version")}")
    testImplementation("com.github.database-rider:rider-spring:${property("database-rider.version")}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Checkstyle> {
    configFile = rootProject.file("checkstyle.xml")
    maxWarnings = 5
    isShowViolations = true
}

tasks.named("checkstyleMain") {
    dependsOn("compileJava")
}

tasks.named("checkstyleTest") {
    enabled = false
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
        csv.required = false
    }
    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include("jacoco/*.exec")
        }
    )
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.named("build") {
    dependsOn("checkstyleMain", "jacocoTestReport")
}
