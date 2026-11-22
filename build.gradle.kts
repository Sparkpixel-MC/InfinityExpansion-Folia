plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
}

group = "io.github.mooy1"
version = "1.9.0-Folia"

java {
    toolchain {
        // Folia 26.3 requires Java 25, so both the toolchain and the
        // produced bytecode target Java 25
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(25)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    // Folia API (includes the region/entity/global/async schedulers)
    compileOnly("dev.folia:folia-api:26.3.build.26-stable")

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
