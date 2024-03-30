package com.badbones69.crazycrates.commands.crates.types.admin;

import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.commands.crates.BaseCommand;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Suggestion;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandDebug extends BaseCommand {

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();

    @Command("debug")
    @Permission(value = "crazycrates.debug", def = PermissionDefault.OP)
    public void debug(Player player, @Suggestion("crates") String crateName) {
        Crate crate = this.crateManager.getCrate(crateName);

        if (crate == null) {
            player.sendMessage(Messages.not_a_crate.getMessage("{crate}", crateName, player));

            return;
        }

        crate.getPrizes().forEach(prize -> crate.givePrize(player, prize));
    }
}