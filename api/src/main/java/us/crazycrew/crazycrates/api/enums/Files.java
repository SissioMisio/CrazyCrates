package us.crazycrew.crazycrates.api.enums;

import com.ryderbelserion.vital.core.config.YamlManager;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;
import us.crazycrew.crazycrates.CratesProvider;
import us.crazycrew.crazycrates.platform.Server;

public enum Files {

    locations("locations.yml"),
    data("data.yml");

    private final @NotNull Server server = CratesProvider.get();

    private final @NotNull YamlManager fileManager = server.getFileManager();

    private final FileConfiguration config;
    private final String fileName;

    Files(String fileName) {
        this.config = this.fileManager.getFile(fileName);

        this.fileName = fileName;
    }

    public FileConfiguration getFile() {
        return this.config;
    }

    public void save() {
        this.fileManager.saveFile(this.fileName);
    }

    public void reload() {
        this.fileManager.reloadFile(this.fileName);
    }
}