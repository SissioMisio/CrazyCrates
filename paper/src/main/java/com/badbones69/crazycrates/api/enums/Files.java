package com.badbones69.crazycrates.api.enums;

import com.badbones69.crazycrates.CrazyCrates;
import com.ryderbelserion.vital.files.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public enum Files {

    locations("locations.yml"),
    data("data.yml");

    private final String fileName;

    Files(String fileName) {
        this.fileName = fileName;
    }

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final @NotNull FileManager fileManager = this.plugin.getFileManager();

    public FileConfiguration getFile() {
        return this.fileManager.getStaticFile(this.fileName);
    }

    public void save() {
        this.fileManager.saveStaticFile(this.fileName);
    }
}