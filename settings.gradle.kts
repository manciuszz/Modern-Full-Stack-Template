pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("io.quarkus") version "2.8.0.Final"
    }
}
rootProject.name = "example"
include("backend", "frontend")
