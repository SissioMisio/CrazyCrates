package com.badbones69.crazycrates.commands.crates.types.admin;

import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.commands.crates.BaseCommand;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.support.metrics.MetricsManager;
import com.badbones69.crazycrates.tasks.InventoryManager;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import com.ryderbelserion.cluster.api.files.FileManager;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;

public class CommandReload extends BaseCommand {

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();
    private final @NotNull FileManager fileManager = this.plugin.getFileManager();
    private final @NotNull MetricsManager metrics = this.plugin.getMetrics();

    @Command("reload")
    @Permission(value = "crazycrates.reload", def = PermissionDefault.OP)
    public void reload(CommandSender sender) {
        ConfigManager.reload();

        MiscUtils.cleanFiles();
        MiscUtils.loadFiles();

        boolean isEnabled = MiscUtils.toggleMetrics();

        if (!isEnabled) {
            this.metrics.stop();
        } else {
            this.metrics.start();
        }

        // Close previews
        if (this.config.getProperty(ConfigKeys.take_out_of_preview)) {
            this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                //todo() update this to use something in triumph gui
                /*if (this.inventoryManager.inCratePreview(player)) {
                    this.inventoryManager.closeCratePreview(player);

                    if (this.config.getProperty(ConfigKeys.send_preview_taken_out_message)) {
                        player.sendMessage(Messages.reloaded_forced_out_of_preview.getMessage(player));
                    }
                }*/
            });
        }

        this.fileManager.reloadStaticFile("locations.yml");

        //this.fileManager.reloadAllFiles();
        //this.fileManager.setup(false);

        this.crateManager.load();

        if (sender instanceof Player player) {
            player.sendMessage(Messages.reloaded_plugin.getMessage(player));

            return;
        }

        sender.sendMessage(Messages.reloaded_plugin.getMessage());
    }
}