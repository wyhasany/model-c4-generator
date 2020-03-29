import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    java
    application
    id("io.freefair.lombok") version "5.0.0-rc6"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.structurizr:structurizr-core:1.3.5")
    implementation("com.structurizr:structurizr-plantuml:1.3.4")
}

application {
    mainClassName = "pl.wyhasany.Structurizr"
}

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

tasks.named<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}

tasks.named<JavaCompile>("compileTestJava") {
    options.encoding = "UTF-8"
}


