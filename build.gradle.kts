plugins {
    `java-library`
}

group = "com.kkoreng.diskkord"
version = "1.0.0"

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

tasks.test {
    useJUnitPlatform()
}
