import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by extra("1.5.31")

plugins {
    kotlin("jvm") version "1.5.31"
    maven
}

group = "jp.aoichaan0513"
version = "1.3.1"

repositories {
    mavenCentral()
    jcenter()

    maven("https://m2.dv8tion.net/releases")
    maven("https://repository.aoichaan0513.jp")
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("script-util", kotlinVersion))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.2")

    implementation("net.dv8tion", "JDA", "4.3.0_333")
    implementation("club.minnced", "discord-webhooks", "0.7.2")
    implementation("joda-time", "joda-time", "2.10.12")
    implementation("jp.aoichaan0513", "Kotlin_Utils", "1.2.2")
}

java {
    withSourcesJar()
    withJavadocJar()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()

val repo = File(rootDir, "repository")
val uploadArchives: Upload by tasks
uploadArchives.repositories.withConvention(MavenRepositoryHandlerConvention::class) {
    mavenDeployer {
        withGroovyBuilder {
            "repository"("url" to uri("file://${repo.absolutePath}"))
        }

        pom.project {
            withGroovyBuilder {
                "parent" {
                    "groupId"(group)
                    "artifactId"(rootProject.name)
                    "version"(version)
                }
            }
        }
    }
}

val sourcesJar: Jar by tasks
val javadocJar: Jar by tasks
artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}