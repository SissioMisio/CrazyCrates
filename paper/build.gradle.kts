plugins {
    `paper-plugin`

    id("io.papermc.paperweight.userdev")

    alias(libs.plugins.run.paper)
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(fileTree("$rootDir/libs/compile").include("*.jar"))

    implementation(fileTree("$rootDir/libs/shade").include("*.jar"))

    paperweight.paperDevBundle(libs.versions.bundle)

    implementation(libs.bundles.triumph)

    implementation(libs.config.me)

    implementation(libs.metrics)

    implementation(projects.api)

    compileOnly(libs.vault)
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion("1.20.4")
    }

    shadowJar {
        archiveClassifier.set("")

        listOf(
            "dev.triumphteam.cmd",
            "dev.triumphteam.gui",
            "org.bstats"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }

    assemble {
        dependsOn(reobfJar)
    }

    reobfJar {
        outputJar = rootProject.layout.buildDirectory.file("jars/paper/${rootProject.name}-${rootProject.version}.jar")
    }

    processResources {
        val properties = hashMapOf(
            "name" to rootProject.name,
            "version" to project.version,
            "group" to rootProject.group,
            "description" to rootProject.description,
            "apiVersion" to providers.gradleProperty("apiVersion").get(),
            "authors" to providers.gradleProperty("authors").get(),
            "website" to providers.gradleProperty("website").get()
        )

        inputs.properties(properties)

        filesMatching("plugin.yml") {
            expand(properties)
        }
    }
}