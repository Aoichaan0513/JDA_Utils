import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    maven
}

val groupId = "jp.aoichaan0513"
group = groupId
version = "1.0.1"

repositories {
    mavenCentral()
    jcenter()

    maven("https://repository.aoichaan0513.jp")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.dv8tion", "JDA", "4.2.0_224")
    implementation("club.minnced", "discord-webhooks", "0.5.3")
    implementation("jp.aoichaan0513", "Kotlin_Utils", "1.1.5")
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
                    "groupId"(groupId)
                    "artifactId"(rootProject.name)
                    "version"(version)
                }
            }
        }
    }
}