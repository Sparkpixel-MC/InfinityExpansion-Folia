plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
}

group = "io.github.mooy1"
version = "1.8.7-Folia"

java {
    toolchain {
        // JDK 25 is needed to read InfinityLib's Java 25 class files,
        // while the addon itself still targets Java 21 bytecode
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(21)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    // Folia API (includes the region/entity/global/async schedulers)
    compileOnly("dev.folia:folia-api:1.21.8-R0.1-SNAPSHOT")

    compileOnly("com.github.SlimefunGuguProject:Slimefun4:2025.1.2")

    compileOnly("net.guizhanss:GuizhanLibPlugin:1.7.6")

    implementation(files("libs/InfinityLib.jar"))

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.processResources {
    val props = mapOf("version" to project.version.toString())
    filesMatching("plugin.yml") {
        expand(props)
    }
    from(project.rootDir) {
        include("LICENSE")
    }
}

tasks.jar {
    archiveClassifier.set("unshaded")
}

tasks.shadowJar {
    archiveClassifier.set("")
    minimize()
    relocate("io.github.mooy1.infinitylib", "io.github.mooy1.infinityexpansion.infinitylib")
    exclude("META-INF/*")
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
