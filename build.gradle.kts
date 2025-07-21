plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.feather)
    alias(libs.plugins.shadow)

    `java-library`
}

val git = feather.getGit()

val commitHash: String? = git.getCurrentCommitHash().subSequence(0, 7).toString()
val isSnapshot: Boolean = git.getCurrentBranch() == "dev"
val content: String = if (isSnapshot) "[$commitHash](https://github.com/ryderbelserion/${rootProject.name}/commit/$commitHash) ${git.getCurrentCommit()}" else rootProject.file("changelog.md").readText(Charsets.UTF_8)
val minecraft = libs.versions.minecraft.get()
val versions = listOf(minecraft)

rootProject.group = "com.ryderbelserion.simpleflags"
rootProject.version = if (isSnapshot) "$minecraft-$commitHash" else "1.1.0"
rootProject.description = "A plugin that adds simple worldguard flags."

repositories {
    maven("https://repo.papermc.io/repository/maven-public")

    maven("https://repo.crazycrew.us/libraries")

    maven("https://maven.enginehub.org/repo")
}

dependencies {
    compileOnly(libs.vital.paper)

    compileOnly(libs.worldguard)

    compileOnly(libs.paper)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of("21"))
}

feather {
    rootDirectory = rootProject.rootDir.toPath()

    val data = git.getGithubCommit("ryderbelserion/${rootProject.name}")

    val user = data.user

    discord {
        webhook {
            group(rootProject.name.lowercase())
            task("dev-build")

            if (System.getenv("BUILD_WEBHOOK") != null) {
                post(System.getenv("BUILD_WEBHOOK"))
            }

            username(user.getName())

            avatar(user.avatar)

            embeds {
                embed {
                    color("#ffa347")

                    title("A new dev version of ${rootProject.name} is ready!")

                    fields {
                        field(
                            "Version ${rootProject.version}",
                            listOf(
                                "*Click below to download!*",
                                "<:modrinth:1115307870473420800> [Modrinth](https://modrinth.com/plugin/${rootProject.name.lowercase()}/version/${rootProject.version})",
                            ).convertList()
                        )

                        field(
                            ":bug: Report Bugs",
                            "https://github.com/ryderbelserion/${rootProject.name}/issues"
                        )

                        field(
                            ":hammer: Changelog",
                            content
                        )
                    }
                }
            }
        }

        webhook {
            group(rootProject.name.lowercase())
            task("release-build")

            if (System.getenv("BUILD_WEBHOOK") != null) {
                post(System.getenv("BUILD_WEBHOOK"))
            }

            username(user.getName())

            avatar(user.avatar)

            content("<@&929463441159254066>")

            embeds {
                embed {
                    color("#1bd96a")

                    title("A new release version of ${rootProject.name} is ready!")

                    fields {
                        field(
                            "Version ${rootProject.version}",
                            listOf(
                                "*Click below to download!*",
                                "<:modrinth:1115307870473420800> [Modrinth](https://modrinth.com/plugin/${rootProject.name.lowercase()}/version/${rootProject.version})",
                            ).convertList()
                        )

                        field(
                            ":bug: Report Bugs",
                            "https://github.com/ryderbelserion/${rootProject.name}/issues"
                        )

                        field(
                            ":hammer: Changelog",
                            content
                        )
                    }
                }
            }
        }
    }
}

fun List<String>.convertList(): String {
    val builder = StringBuilder(size)

    forEach {
        builder.append(it).append("\n")
    }

    return builder.toString()
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveClassifier.set("")
    }

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")
        jvmArgs("-Dcom.mojang.eula.agree=true")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }

    modrinth {
        token = System.getenv("MODRINTH_TOKEN")

        projectId = rootProject.name

        versionName = "${rootProject.version}"
        versionNumber = "${rootProject.version}"
        versionType = if (isSnapshot) "beta" else "release"

        changelog = content

        gameVersions.addAll(versions)

        uploadFile = shadowJar.get().archiveFile.get().asFile

        loaders.addAll(listOf("paper", "folia", "purpur"))

        syncBodyFrom = rootProject.file("description.md").readText(Charsets.UTF_8)

        autoAddDependsOn = false
        detectLoaders = false
    }

    processResources {
        val props = mapOf(
            "name" to rootProject.name,
            "version" to rootProject.version,
            "group" to rootProject.group,
            "description" to rootProject.description,
            "apiVersion" to libs.versions.minecraft.get()
        )

        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}