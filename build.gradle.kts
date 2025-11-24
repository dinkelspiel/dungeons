plugins {
    id("java")
    id("io.freefair.lombok") version "8.14"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "dev.keii.dungeons"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("net.minestom:minestom:2025.10.31-1.21.10")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "dev.keii.dungeons.Dungeons"
        }
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("") // Prevent the -all suffix on the shadowjar file.
    }
}