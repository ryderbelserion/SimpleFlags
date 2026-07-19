plugins {
    id("com.gradleup.shadow")
    id("java-plugin")
}

tasks {
    shadowJar {
        archiveClassifier.set("")

        exclude("META-INF/**")

        listOf(
            "com.ryderbelserion.fusion",
            "org.bstats"
        ).forEach {
            relocate(it, "libs.$it")
        }

        minimize {
            exclude(dependency("com.ryderbelserion.fusion:.*"))
        }
    }
}