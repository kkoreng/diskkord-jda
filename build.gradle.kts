plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.37.0"
}

group = "com.kkoreng"
version = "0.2.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // JDA types are part of the public API, so users get JDA transitively.
    api(libs.jda)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Classpath
    implementation(libs.classgraph)

    // Logging: API only. The logging backend is chosen by the user.
    implementation(libs.slf4j)
}

// Compile for Java 21 regardless of the JDK running Gradle, so users on Java 21 can use the library.
tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

tasks.test {
    useJUnitPlatform()
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("com.kkoreng", "diskkord", version.toString())

    pom {
        name = "Diskkord"
        description = "A lightweight JDA framework for registering commands, listeners and interactions."
        inceptionYear = "2026"
        url = "https://github.com/kkoreng/diskkord-jda"

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "repo"
            }
        }

        developers {
            developer {
                id = "kkoreng"
                name = "kkoreng"
                url = "https://github.com/kkoreng"
            }
        }

        scm {
            url = "https://github.com/kkoreng/diskkord-jda"
            connection = "scm:git:git://github.com/kkoreng/diskkord-jda.git"
            developerConnection = "scm:git:ssh://git@github.com/kkoreng/diskkord-jda.git"
        }
    }
}
