package com.badbones69.crazycrates.commands.crates.types;

import com.badbones69.crazycrates.api.builders.types.CrateMainMenu;
import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.commands.crates.BaseCommand;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;

public class CommandHelp extends BaseCommand {

    @Command
    @Permission("crazycrates.gui")
    public void gui(Player player) {
        if (this.config.getProperty(ConfigKeys.enable_crate_menu)) {
            //CrateMainMenu crateMainMenu = new CrateMainMenu(player, this.config.getProperty(ConfigKeys.inventory_size), this.config.getProperty(ConfigKeys.inventory_name));

            //player.openInventory(crateMainMenu.build().getInventory());

            return;
        }

        help(player);
    }

    @Command("help")
    @Permission(value = "crazycrates.help", def = PermissionDefault.TRUE)
    public void help(CommandSender sender) {
        if (sender instanceof Player player) {
            if (player.hasPermission("crazycrates.admin")) {
                player.sendMessage(Messages.admin_help.getMessage(player));

                return;
            }

            player.sendMessage(Messages.help.getMessage(player));

            return;
        }

        sender.sendMessage(Messages.admin_help.getMessage());
    }
}