plugins {
    kotlin("jvm") version "1.4.21"
}

repositories {
    mavenCentral()
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}

sourceSets.test {
    java.srcDirs("src/test/java", "src/test/kotlin")
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
}

kotlin{
    sourceSets {
        val test by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}