import utils.convertList
import utils.updateMarkdown

plugins {
    id("modrinth-plugin")
    id("hangar-plugin")

    `paper-plugin`
}

val git = feather.getGit()

val releaseType = rootProject.ext.get("release_type").toString()
val color = rootProject.property("${releaseType.lowercase()}_color").toString()
val isRelease = releaseType.equals("release", true)
val isAlpha = releaseType.equals("alpha", true)

repositories {
    maven("https://repo.papermc.io/repository/maven-public")

    maven("https://repo.crazycrew.us/libraries")

    maven("https://maven.enginehub.org/repo")
}

dependencies {
    implementation(libs.vital.paper)

    compileOnly(libs.worldguard)
}

feather {
    rootDirectory = rootProject.rootDir.toPath()

    val data = git.getGithubCommit("ryderbelserion/${rootProject.name}")

    val user = data.user

    discord {
        webhook {
            group(rootProject.name.lowercase())
            task("release-build")

            if (System.getenv("BUILD_WEBHOOK") != null) {
                post(System.getenv("BUILD_WEBHOOK"))
            }

            if (isRelease) {
                username(user.getName())

                avatar(user.avatar)
            } else {
                username(rootProject.property("author_name").toString())

                avatar(rootProject.property("author_avatar").toString())
            }

            embeds {
                embed {
                    color(color)

                    title("A new $releaseType version of ${rootProject.name} is ready!")

                    //if (isRelease) {
                    //    content("<@&${rootProject.property("discord_role_id").toString()}>")
                    //}

                    fields {
                        field(
                            "Version ${rootProject.version}",
                            listOf(
                                "*Click below to download!*",
                                "<:modrinth:1115307870473420800> [Modrinth](https://modrinth.com/plugin/${rootProject.name.lowercase()}/version/${rootProject.version})"
                            ).convertList()
                        )

                        field(
                            ":bug: Report Bugs",
                            "https://github.com/${rootProject.property("repository_owner")}/${rootProject.name}/issues"
                        )

                        field(
                            ":hammer: Changelog",
                            rootProject.ext.get("mc_changelog").toString().updateMarkdown()
                        )
                    }
                }
            }

            webhook {
                group(rootProject.name.lowercase())
                task("failed-build")

                if (System.getenv("BUILD_WEBHOOK") != null) {
                    post(System.getenv("BUILD_WEBHOOK"))
                }

                username(rootProject.property("mascot_name").toString())

                avatar(rootProject.property("mascot_avatar").toString())

                embeds {
                    embed {
                        color(rootProject.property("failed_color").toString())

                        title("Oh no! It failed!")

                        thumbnail("https://raw.githubusercontent.com/ryderbelserion/Branding/refs/heads/main/booze.jpg")

                        fields {
                            field(
                                "The build versioned ${rootProject.version} for project ${rootProject.name} failed.",
                                "The developer is likely already aware, he is just getting drunk.",
                                inline = true
                            )
                        }
                    }
                }
            }
        }
    }
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")
        jvmArgs("-Dcom.mojang.eula.agree=true")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }

    shadowJar {
        listOf(
            "com.ryderbelserion.vital"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }

    configurations.all { //todo() FIX ME later, fucking forced dependencies, give me a fucking break
        resolutionStrategy {
            force("org.apache.logging.log4j:log4j-bom:2.24.1")
            force("com.google.guava:guava:33.3.1-jre")
            force("com.google.code.gson:gson:2.11.0")
            force("it.unimi.dsi:fastutil:8.5.15")
        }
    }
}