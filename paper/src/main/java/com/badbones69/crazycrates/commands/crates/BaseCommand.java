package com.badbones69.crazycrates.commands.crates;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCratesPaper;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Description;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.config.ConfigManager;

@Command(value = "crazycrates", alias = {"crate"})
@Description("The base command for CrazyCrates")
public abstract class BaseCommand {

    protected final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    protected final @NotNull SettingsManager config = ConfigManager.getConfig();

}