import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by extra("1.7.10")

plugins {
    kotlin("jvm") version "1.7.10"
    maven
}

group = "jp.aoichaan0513"
version = "2.0.0-beta.8"

repositories {
    mavenCentral()
    jcenter()

    // maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io/")
    maven("https://repository.aoichaan0513.jp")
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("script-util", kotlinVersion))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.4")

    implementation("com.github.DV8FromTheWorld", "JDA", "07e8166fc5")
    implementation("club.minnced", "discord-webhooks", "0.8.0")
    implementation("joda-time", "joda-time", "2.10.14")
    implementation("org.ocpsoft.prettytime", "prettytime", "5.0.3.Final")
    implementation("jp.aoichaan0513", "Kotlin_Utils", "1.5.1")
}

java {
    withSourcesJar()
    withJavadocJar()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
compileKotlin.kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"

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