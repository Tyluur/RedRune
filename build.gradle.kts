plugins {
    application
    kotlin("jvm") version "1.8.0"
}

val junitVersion = "5.6.2"
val koinVersion = "2.1.5"

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
        implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.4.2")
        implementation("org.koin", "koin-logger-slf4j", koinVersion)


        // Network
        implementation("io.netty:netty-all:4.1.44.Final")

        // Logging
        implementation("org.slf4j:slf4j-api:1.7.30")
        implementation("ch.qos.logback:logback-classic:1.2.3")

        // https://mvnrepository.com/artifact/org.cryptonode.jncryptor/jncryptor
        implementation("org.cryptonode.jncryptor:jncryptor:1.2.0")

        // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
        implementation("com.zaxxer:HikariCP:2.3.2")

        compileOnly("org.projectlombok:lombok:1.16.10")

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