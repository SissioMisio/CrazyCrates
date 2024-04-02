package us.crazycrew.crazycrates.platform;

import us.crazycrew.crazycrates.CratesProvider;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class Server {

    private final JavaPlugin plugin;
    private final File crateFolder;
    private final File keyFolder;

    public Server(JavaPlugin plugin) {
        this.plugin = plugin;

        this.keyFolder = new File(this.plugin.getDataFolder(), "keys");

        this.crateFolder = new File(this.plugin.getDataFolder(), "crates");
    }

    public void enable() {
        ConfigManager.load(this.plugin.getDataFolder());

        List.of(
                "CrateExample.yml",
                "WarCrateExample.yml",
                "QuadCrateExample.yml",
                "QuickCrateExample.yml"
        ).forEach(file -> this.plugin.saveResource(this.crateFolder + "/" + file, false));

        List.of(
                "CasinoKey.yml",
                "DiamondKey.yml"
        ).forEach(file -> this.plugin.saveResource(this.keyFolder + "/" + file, false));

        CratesProvider.register(this);
    }

    public void reload() {
        ConfigManager.reload();
    }

    public void disable() {
        CratesProvider.unregister();
    }

    public @NotNull File getFolder() {
        return this.plugin.getDataFolder();
    }

    public @NotNull Logger getLogger() {
        return this.plugin.getLogger();
    }

    public @NotNull File getKeyFolder() {
        return this.keyFolder;
    }

    public @NotNull File getCrateFolder() {
        return this.crateFolder;
    }

    public @NotNull File[] getKeyFiles() {
        return this.keyFolder.listFiles((dir, name) -> name.endsWith(".yml"));
    }

    public @NotNull File[] getCrateFiles() {
        return this.crateFolder.listFiles((dir, name) -> name.endsWith(".yml"));
    }
}