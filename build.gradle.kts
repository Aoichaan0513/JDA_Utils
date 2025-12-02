plugins {
    kotlin("jvm") version "2.2.21"
    id("maven-publish")
}

group = "com.github.aoichaan0513"
version = "6.1.2_1"

repositories {
    mavenCentral()

    maven("https://jitpack.io/")
}

dependencies {
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.9.0")

    implementation("net.dv8tion", "JDA", "6.1.2")
    implementation("club.minnced", "discord-webhooks", "0.8.4")
    implementation("joda-time", "joda-time", "2.13.0")
    implementation("org.ocpsoft.prettytime", "prettytime", "5.0.9.Final")
    implementation("org.json", "json", "20240303")
    implementation("com.github.aoichaan0513", "Kotlin_Utils", "2.0.21_1")
}

java {
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain(11)
    compilerOptions {
        optIn.add("kotlin.RequiresOptIn")
    }
}

val sourcesJar: Jar by tasks
val javadocJar: Jar by tasks
artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["kotlin"])
                groupId = this@afterEvaluate.group.toString()
                artifactId = rootProject.name
                version = this@afterEvaluate.version.toString()
            }
        }
    }
}