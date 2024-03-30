package com.badbones69.crazycrates.commands.crates.types.admin;

import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.commands.crates.BaseCommand;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Suggestion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandPreview extends BaseCommand {

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();

    @Command("preview")
    @Permission(value = "crazycrates.preview", def = PermissionDefault.OP)
    public void onAdminCratePreview(CommandSender sender, @Suggestion("crates") String crateName, @Suggestion("players") Player target) {
        Crate crate = this.crateManager.getCrate(crateName);

        if (crate == null) {
            if (sender instanceof Player player) {
                player.sendMessage(Messages.not_a_crate.getMessage("{crate}", crateName, player));

                return;
            }

            sender.sendMessage(Messages.not_a_crate.getMessage("{crate}", crateName));

            return;
        }

        if (!crate.isPreviewToggle()) {
            if (sender instanceof Player player) {
                player.sendMessage(Messages.preview_disabled.getMessage("{crate}", crate.getName(), player));

                return;
            }

            sender.sendMessage(Messages.preview_disabled.getMessage("{crate}", crate.getName()));

            return;
        }

        //this.inventoryManager.addViewer(target); likely don't need this anymore.
        //this.inventoryManager.openNewCratePreview(target, crate, crate.getCrateType() == CrateType.casino);
    }
}