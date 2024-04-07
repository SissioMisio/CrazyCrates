package com.badbones69.crazycrates.support;

import com.badbones69.crazycrates.CrazyCrates;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public enum PluginSupport {

    placeholderapi("PlaceholderAPI"),
    oraxen("Oraxen"),
    items_adder("ItemsAdder");

    private final String name;

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    /**
     * @param name the name of the plugin.
     */
    PluginSupport(String name) {
        this.name = name;
    }

    /**
     * @return name of the plugin.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if plugin is enabled.
     *
     * @return true or false.
     */
    public boolean isPluginEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled(this.name);
    }
}