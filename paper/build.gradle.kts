plugins {
    `paper-plugin`
}

project.group = "${rootProject.group}.paper"

repositories {
    maven("https://maven.enginehub.org/repo")
}

dependencies {
    implementation(libs.fusion.paper)

    compileOnly(libs.worldguard)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        listOf(
            "com.ryderbelserion.fusion",
            "io.leangen.geantyref",
            //"com.zaxxer.hikari",
            "org.spongepowered",
            "com.google.gson",
            "org.jspecify"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }

    runPaper.folia.registerTask()

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")
        jvmArgs("-Dcom.mojang.eula.agree=true")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }
}