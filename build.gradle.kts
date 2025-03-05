import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "io.github.zimoyin"
version = "1.0"
description = "imagefx"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    if (!project.hasProperty("mavenCentralUsername")) {
        throw IllegalArgumentException("mavenCentralUsername is not set")
    } else if (!project.hasProperty("mavenCentralPassword")) {
        throw IllegalArgumentException("mavenCentralPassword is not set")
    } else if (!project.hasProperty("signing.keyId")) {
        throw IllegalArgumentException("signing.keyId is not set")
    } else if (!project.hasProperty("signing.password")) {
        throw IllegalArgumentException("signing.password is not set")
    }

    coordinates("io.github.zimoyin", "imagefx", version.toString())

    pom {
        name.set("imagefx")
        description.set("imagefx")
        inceptionYear.set("2025")
        url.set("https://github.com/zimoyin/imagefx/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("zimoyin")
                name.set("zimoyin")
                email.set("tianxuanzimo@qq.com")
                url.set("https://github.com/zimoyin")
            }
        }
        scm {
            url.set("imagefx")
            connection.set("scm:git:git://github.com/zimoyin/imagefx.git")
            developerConnection.set("scm:git:ssh://git@github.com/zimoyin/imagefx.git")
        }
    }
}