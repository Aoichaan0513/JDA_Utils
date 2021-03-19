import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    maven
}

group = "jp.aoichaan0513"
version = "1.0.12"

repositories {
    mavenCentral()
    jcenter()

    maven("https://repository.aoichaan0513.jp")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.dv8tion", "JDA", "4.2.0_242")
    implementation("club.minnced", "discord-webhooks", "0.5.6")
    implementation("joda-time", "joda-time", "2.10.8")
    implementation("jp.aoichaan0513", "Kotlin_Utils", "1.1.9")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

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