plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")

    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
