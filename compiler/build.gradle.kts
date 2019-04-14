
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
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
