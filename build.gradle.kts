plugins {
    id("java")
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

    // JDA
    implementation(libs.jda)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Classpath
    implementation(libs.classgraph)

    // Config
    implementation(libs.dotenv)
    implementation(libs.snakeyaml)

    // Logging
    implementation(libs.slf4j)
    runtimeOnly(libs.logback)
}

tasks.test {
    useJUnitPlatform()
}
