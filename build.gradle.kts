import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.9.21"

plugins {
    kotlin("jvm") version "1.9.21"
    maven
}

group = "jp.aoichaan0513"
version = "2.0.0-beta.29"

repositories {
    mavenCentral()
    jcenter()

    maven("https://jitpack.io/")
    maven("https://raw.githubusercontent.com/Aoichaan0513/repository/main")
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.7.3")

    implementation("net.dv8tion", "JDA", "5.0.0-beta.19")
    implementation("club.minnced", "discord-webhooks", "0.8.4")
    implementation("joda-time", "joda-time", "2.12.5")
    implementation("org.ocpsoft.prettytime", "prettytime", "5.0.7.Final")
    implementation("org.json", "json", "20231013")
    implementation("jp.aoichaan0513", "Kotlin_Utils", "1.7.1")
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