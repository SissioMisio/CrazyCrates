package com.badbones69.crazycrates.platform;

import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.support.PluginSupport;
import com.ryderbelserion.cluster.ClusterFactory;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class PaperFactory extends ClusterFactory {

    public PaperFactory(JavaPlugin plugin) {
        super(plugin);

        setLogging(MiscUtils.isLogging());

        File dataFolder = plugin.getDataFolder();

        Path cratesDirectory = new File(dataFolder, "crates").toPath();
        Path keysDirectory = new File(dataFolder, "keys").toPath();

        List.of(
                "CrateExample.yml",
                "WarCrateExample.yml",
                "QuadCrateExample.yml",
                "QuickCrateExample.yml"
        ).forEach(file -> copyFile(cratesDirectory, file));

        List.of(
                "CasinoKey.yml",
                "DiamondKey.yml"
        ).forEach(file -> copyFile(keysDirectory, file));
    }

    @Override
    public boolean isPapiEnabled() {
        return PluginSupport.placeholderapi.isPluginEnabled();
    }

    @Override
    public boolean isOraxenEnabled() {
        return PluginSupport.oraxen.isPluginEnabled();
    }

    @Override
    public boolean isItemsAdderEnabled() {
        return PluginSupport.items_adder.isPluginEnabled();
    }

    @Override
    public boolean isHeadDatabaseEnabled() {
        return false;
    }
}