plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.2.20"

}

group = "org.david"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(platform("com.squareup.okhttp3:okhttp-bom:5.1.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("ai.koog:koog-agents:0.4.1")
    implementation("it.skrape:skrapeit:1.2.2")


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}