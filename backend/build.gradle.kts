import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.quarkus") version "2.8.0.Final"
    kotlin("jvm") version "1.6.10"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:2.8.0.Final"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-jsonb")
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-jdbc-h2")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    implementation(kotlin("stdlib-jdk8"))
}

group = "io.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.register<Delete>("remove-old-files") {
    println("Deleting old files.")
    delete("$rootDir/app")
    delete("$rootDir/backend/src/main/resources/META-INF/resources/*")
}

tasks.register<Delete>("remove-redundant-gradle-folders") {
    println("Deleting redundant (empty) gradle folders.")
    delete("$rootDir/build")
}

tasks.register<Copy>("copy-scripts") {
    println("Copying scripts.")
    from("$rootDir/scripts")
    into("$rootDir/app")
}

tasks.register<Copy>("copy-frontend") {
    println("Copying static resources.")
    from("$rootDir/frontend/dist")
    into("$rootDir/backend/src/main/resources/META-INF/resources")
    mustRunAfter("remove-old-files")
}

tasks.register<Copy>("copy-backend") {
    println("Copying backend to 'app' folder.")
    from("$buildDir/quarkus-app")
    into("$rootDir/app")
    mustRunAfter(tasks.quarkusBuild)
}

tasks.register("quark-build") {
    dependsOn("remove-old-files", "copy-frontend", "copy-scripts")
    finalizedBy(tasks.quarkusBuild, "copy-backend", "remove-redundant-gradle-folders")
}
