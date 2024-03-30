package com.badbones69.crazycrates;

import com.badbones69.crazycrates.commands.CommandManager;
import com.badbones69.crazycrates.platform.PaperFactory;
import com.badbones69.crazycrates.platform.PaperServer;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import com.badbones69.crazycrates.platform.crates.KeyManager;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.support.metrics.MetricsManager;
import com.ryderbelserion.cluster.ClusterFactory;
import com.ryderbelserion.cluster.api.files.FileManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class CrazyCratesPaper extends JavaPlugin {

    private FileManager fileManager;
    private PaperServer instance;
    private KeyManager keyManager;
    private CrateManager crateManager;
    private UserManager userManager;

    private MetricsManager metrics;

    @Override
    public void onLoad() {
        this.instance = new PaperServer(getDataFolder());
        new PaperFactory(this);

        this.fileManager = new FileManager();
        this.fileManager.addStaticFile("locations.yml").addStaticFile("data.yml").create();
    }

    @Override
    public void onEnable() {
        this.crateManager = new CrateManager();
        this.crateManager.load();

        this.keyManager = new KeyManager();
        this.keyManager.load();

        this.userManager = new UserManager();

        CommandManager.load();

        this.metrics = new MetricsManager();

        if (MiscUtils.toggleMetrics()) {
            this.metrics.start();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public PaperServer getInstance() {
        return this.instance;
    }

    public KeyManager getKeyManager() {
        return this.keyManager;
    }

    public CrateManager getCrateManager() {
        return this.crateManager;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public MetricsManager getMetrics() {
        return this.metrics;
    }
}