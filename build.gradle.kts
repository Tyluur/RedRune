plugins {
    application
    kotlin("jvm") version "1.8.0"
}

val junitVersion = "5.6.2"

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "dusk-rs"
    version = "0.0.1"

    java.sourceCompatibility = JavaVersion.VERSION_15

    repositories {
        mavenCentral()
        mavenLocal()
        jcenter()
        maven(url = "https://repo.maven.apache.org/maven2")
        maven(url = "https://jitpack.io")
    }
}

subprojects {
    dependencies {
        // Jvm
        implementation(kotlin("stdlib"))

        // Kotlin
        implementation(kotlin("reflect"))
        implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.4.2")

        // Network
        implementation("io.netty:netty-all:4.1.44.Final")

        // Logging
        implementation("org.slf4j:slf4j-api:1.7.30")
        implementation("ch.qos.logback:logback-classic:1.2.3")

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

}