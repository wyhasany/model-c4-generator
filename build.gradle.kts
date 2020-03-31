import com.cosminpolifronie.gradle.plantuml.PlantUmlPluginExtension
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    java
    application
    id("io.freefair.lombok") version "5.0.0-rc6"
    id("com.cosminpolifronie.gradle.plantuml") version "1.6.0"
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

class PlantUmlConfig(val input: String, val output: String, val format: String, val withMetadata: Boolean)

configure<PlantUmlPluginExtension> {
    render(PlantUmlConfig("c1.puml", "img/c1.png", "png", true))
    render(PlantUmlConfig("c2.puml", "img/c2.png", "png", true))
    render(PlantUmlConfig("c3.puml", "img/c3.png", "png", true))
}

tasks.named<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}

tasks.named<JavaCompile>("compileTestJava") {
    options.encoding = "UTF-8"
}


