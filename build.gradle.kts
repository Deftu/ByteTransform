plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = extra["project.group"]?.toString()
    ?: throw groovy.lang.MissingPropertyException("Project group was not set!")
version = extra["project.version"]?.toString()
    ?: throw groovy.lang.MissingPropertyException("Project version was not set!")

repositories {
    // Default repositories
    mavenCentral()

    // Repositories
    maven("https://maven.unifycraft.xyz/releases")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://jitpack.io/")

    // Snapshots
    maven("https://maven.unifycraft.xyz/snapshots")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.ow2.asm:asm-tree:9.3")
    val slf4j = "2.0.0"
    implementation("org.slf4j:slf4j-api:$slf4j")
    implementation("org.slf4j:slf4j-ext:$slf4j")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId =
                extra["project.name"]?.toString() ?: throw IllegalArgumentException("The project name has not been set.")
            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])
        }
    }

    repositories {
        if (project.hasProperty("unifycraft.publishing.username") && project.hasProperty("unifycraft.publishing.password")) {
            fun MavenArtifactRepository.applyCredentials() {
                credentials {
                    username = property("unifycraft.publishing.username")?.toString()
                    password = property("unifycraft.publishing.password")?.toString()
                }
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }

            maven {
                name = "UnifyCraftRelease"
                url = uri("https://maven.unifycraft.xyz/releases")
                applyCredentials()
            }

            maven {
                name = "UnifyCraftSnapshots"
                url = uri("https://maven.unifycraft.xyz/snapshots")
                applyCredentials()
            }
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
