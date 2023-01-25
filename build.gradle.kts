plugins {
    application
    kotlin("jvm") version "1.8.0"
}

val junitVersion = "5.6.2"
val koinVersion = "2.1.5"

apply(plugin = "kotlin")
apply(plugin = "idea")
apply(plugin = "org.jetbrains.kotlin.jvm")

group = "RedRune"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.toVersion('8')
java.targetCompatibility = JavaVersion.toVersion('8')

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
    maven(url = "https://repo.maven.apache.org/maven2")
    maven(url = "https://jitpack.io")
}

dependencies {
    // Jvm
    implementation(kotlin("stdlib"))

    // Kotlin
    implementation(kotlin("reflect"))
    // Java
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(kotlin("gradle-plugin", version = "1.5.0"))

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.4.2")

    // Dependency Injection
    implementation("org.koin", "koin-core", koinVersion)
    implementation("org.koin", "koin-logger-slf4j", koinVersion)

    // Network
    implementation("io.netty:netty-all:4.1.86.Final")

    // Logging
    implementation("org.slf4j:slf4j-api:1.7.30")
    // Logging
    implementation("ch.qos.logback:logback-classic:1.2.9") {
        exclude("org.slf4j", "slf4j-jdk14")
    }
    implementation("com.michael-bull.kotlin-inline-logger", "kotlin-inline-logger-jvm", "1.0.2")

    // https://mvnrepository.com/artifact/org.cryptonode.jncryptor/jncryptor
    implementation("org.cryptonode.jncryptor:jncryptor:1.2.0")

    // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
    implementation("com.zaxxer:HikariCP:2.3.2")

    // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    implementation("org.apache.commons:commons-lang3:3.4")

    // https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:30.0-android")

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
