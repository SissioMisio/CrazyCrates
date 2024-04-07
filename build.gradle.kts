plugins {
    `root-plugin`
}

tasks {
    assemble {
        doFirst {
            delete(File("$rootDir/jars"))
        }
    }
}