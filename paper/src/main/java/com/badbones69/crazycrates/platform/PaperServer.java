package com.badbones69.crazycrates.platform;

import com.badbones69.crazycrates.CrazyCratesPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import java.io.File;
import java.util.logging.Logger;

public class PaperServer extends Server {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final File crateFolder;
    private final File keyFolder;
    private final File folder;

    public PaperServer(File dataFolder) {
        super(dataFolder);
        
        this.folder = this.plugin.getDataFolder();

        this.keyFolder = new File(this.folder, "keys");

        this.crateFolder = new File(this.folder, "crates");
    }

    @Override
    public @NotNull File getFolder() {
        return this.folder;
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public boolean isLogging() {
        return ConfigManager.getConfig().getProperty(ConfigKeys.verbose_logging);
    }

    @Override
    public @NotNull File getKeyFolder() {
        return this.keyFolder;
    }

    @Override
    public @NotNull File getCrateFolder() {
        return this.crateFolder;
    }

    @Override
    public @NotNull File[] getKeyFiles() {
        return this.keyFolder.listFiles((dir, name) -> name.endsWith(".yml"));
    }

    @Override
    public @NotNull File[] getCrateFiles() {
        return this.crateFolder.listFiles((dir, name) -> name.endsWith(".yml"));
    }
}