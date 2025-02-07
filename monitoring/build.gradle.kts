plugins {
    id("java")
}

group = "org.vjiki"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.23")
//    implementation("io.projectreactor:reactor-core:3.7.2")
//    implementation("ch.qos.logback:logback-classic:1.5.16")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}