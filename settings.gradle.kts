import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Default repositories
        gradlePluginPortal()
        mavenCentral()

        // Repositories
        maven("https://maven.unifycraft.xyz/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://jitpack.io/")

        // Snapshots
        maven("https://maven.unifycraft.xyz/snapshots")
        maven("https://s01.oss.sonatype.org/content/groups/public/")
        mavenLocal()
    }

    plugins {
        val kotlin = "1.6.21"
        kotlin("jvm") version(kotlin)
    }
}

val projectName: String = extra["project.name"]?.toString()
    ?: throw MissingPropertyException("Project name was not set!")
rootProject.name = projectName

include(":annotation-processor")
