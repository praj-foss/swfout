plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("junit:junit:4.13")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "in.praj.swfout.App"
}
