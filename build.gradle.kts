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

application {
    mainClassName = "in.praj.swfout.App"
}
