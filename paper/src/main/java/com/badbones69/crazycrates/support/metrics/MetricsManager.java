package com.badbones69.crazycrates.support.metrics;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import java.util.HashMap;
import java.util.Map;

public class MetricsManager {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();

    private Metrics metrics;

    public void start() {
        if (this.metrics != null) {
            if (MiscUtils.isLogging()) this.plugin.getLogger().warning("Metrics is already enabled.");

            return;
        }

        this.metrics = new Metrics(this.plugin, 4514);

        this.metrics.addCustomChart(new SimplePie("use_new_random", () -> ConfigManager.getConfig().getProperty(ConfigKeys.use_different_random).toString()));

        this.metrics.addCustomChart(new AdvancedPie("crate_types", () -> {
            Map<String, Integer> values = new HashMap<>();

            this.crateManager.getCrates().forEach(crate -> {
                CrateType crateType = crate.getCrateType();

                values.put(crateType.getName(), this.crateManager.getCrates().stream().filter(type -> type.getCrateType() == crateType).toList().size());
            });

            return values;
        }));

        if (MiscUtils.isLogging()) this.plugin.getLogger().fine("Metrics has been enabled.");
    }

    public void stop() {
        if (this.metrics == null) {
            if (MiscUtils.isLogging()) this.plugin.getLogger().warning("Metrics isn't enabled so we do nothing.");

            return;
        }

        this.metrics.shutdown();

        this.metrics = null;

        if (MiscUtils.isLogging()) this.plugin.getLogger().fine("Metrics has been turned off.");
    }
}