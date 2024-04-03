package com.badbones69.crazycrates;

import com.badbones69.crazycrates.commands.CommandManager;
import com.ryderbelserion.vital.VitalPlugin;
import us.crazycrew.crazycrates.platform.Server;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import com.badbones69.crazycrates.platform.crates.KeyManager;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.ryderbelserion.vital.files.FileManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;

public class CrazyCrates extends JavaPlugin {

    private CrateManager crateManager;
    private UserManager userManager;
    private FileManager fileManager;
    private KeyManager keyManager;
    private VitalPlugin vital;
    private Server instance;

    @Override
    public void onLoad() {
        this.instance = new Server(this);
        this.instance.enable();

        this.vital = new VitalPlugin(this);
        this.vital.setLogging(ConfigManager.getConfig().getProperty(ConfigKeys.verbose_logging));
        this.vital.start();

        this.fileManager = new FileManager();
        this.fileManager
                .addDynamicFile("crates", "CrateExample.yml")
                .addDynamicFile("crates", "QuadCrateExample.yml")
                .addDynamicFile("crates", "QuickCrateExample.yml")
                .addDynamicFile("crates", "WarCrateExample.yml")
                .addDynamicFile("keys", "CasinoKey.yml")
                .addDynamicFile("keys", "DiamondKey.yml")
                .addStaticFile("locations.yml")
                .addStaticFile("data.yml")
                .addFolder("crates")
                .addFolder("keys").create();
    }

    @Override
    public void onEnable() {
        this.crateManager = new CrateManager();
        this.crateManager.load();

        this.keyManager = new KeyManager();
        this.keyManager.load();

        this.userManager = new UserManager();

        CommandManager.load();
    }

    @Override
    public void onDisable() {
        if (this.instance != null) {
            this.instance.disable();
        }

        if (this.vital != null) {
            this.vital.stop();
        }
    }

    public @NotNull CrateManager getCrateManager() {
        return this.crateManager;
    }

    public @NotNull FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull KeyManager getKeyManager() {
        return this.keyManager;
    }

    public @NotNull UserManager getUserManager() {
        return this.userManager;
    }

    public @NotNull Server getInstance() {
        return this.instance;
    }
}