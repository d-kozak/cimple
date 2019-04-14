
plugins {
    kotlin("jvm") version "1.3.21"
}

repositories {
    jcenter()
}

group = "io.dkozak.cimple"
version = "0.0.1-SNAPSHOT"

dependencies {

    implementation(kotlin("stdlib"))

    testCompile("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testCompile("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    testCompile("org.assertj:assertj-core:3.11.1")
}
