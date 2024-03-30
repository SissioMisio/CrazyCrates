plugins {
    `paper-plugin`

    id("io.papermc.paperweight.userdev")

    alias(libs.plugins.run.paper)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.oraxen.com/releases/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.bundle)

    implementation(libs.bundles.triumph)

    api(libs.cluster.paper) {
        isTransitive = true
    }

    implementation(libs.config.me) {
        exclude(group = "org.yaml", module = "snakeyaml")
    }

    implementation(libs.metrics)

    implementation(projects.api)

    compileOnly(libs.head.database.api)

    compileOnly(libs.bundles.adventure)

    compileOnly(libs.bundles.holograms)

    compileOnly(libs.placeholder.api)

    compileOnly(libs.itemsadder.api)

    compileOnly(libs.oraxen.api)

    compileOnly(libs.vault)

    compileOnly(fileTree("libs").include("*.jar"))
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
            "com.ryderbelserion.cluster",
            "dev.triumphteam.cmd",
            "dev.triumphteam.gui",
            "org.bstats"
        ).forEach {
            relocate(it, "libs.$it")
        }
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