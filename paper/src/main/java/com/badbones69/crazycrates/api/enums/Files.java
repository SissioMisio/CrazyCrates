package com.badbones69.crazycrates.api.enums;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.ryderbelserion.cluster.api.files.FileManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

public enum Files {

    locations("locations.yml"),
    data("data.yml");

    private final String fileName;

    Files(String fileName) {
        this.fileName = fileName;
    }

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull FileManager fileManager = this.plugin.getFileManager();

    public FileConfiguration getFile() {
        return this.fileManager.getStaticFile(this.fileName);
    }

    public void save() {
        this.fileManager.saveStaticFile(this.fileName);
    }
}