import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    application
    antlr
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "io.dkozak"
version = "1.0-SNAPSHOT"
project.setProperty("mainClassName", "io.dkozak.cimple.MainKt")

repositories {
    mavenCentral()
}


tasks.named("compileKotlin") {
    dependsOn(":generateGrammarSource")
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor", "-long-messages")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<ShadowJar>() {
    manifest {
        attributes["Main-Class"] = "io.dkozak.cimple.MainKt"
    }
}

//tasks.withType<Jar> {
//    manifest {
//        attributes(mapOf(
//            "Main-Class" to "io.dkozak.cimple.MainKt"
//        ))
//        attributes["Main-Class"] = "io.dkozak.cimple.MainKt"
//    }
//}

//tasks {
//    named<ShadowJar>("shadowJar") {
//        archiveBaseName.set("shadow")
//        mergeServiceFiles()
//        manifest {
//            attributes(mapOf("Main-Class" to "io.dkozak.cimple.MainKt"))
//        }
//    }
//}

//tasks {
//    build {
//        dependsOn(shadowJar)
//    }
//}

dependencies {
    implementation(kotlin("stdlib"))
    antlr("org.antlr:antlr4:4.9.2")
    implementation("org.antlr:antlr4:4.9.2")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

