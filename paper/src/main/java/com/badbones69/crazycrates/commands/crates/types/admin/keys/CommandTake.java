package com.badbones69.crazycrates.commands.crates.types.admin.keys;

import com.badbones69.crazycrates.commands.crates.BaseCommand;
import com.badbones69.crazycrates.platform.CustomPlayer;
import com.badbones69.crazycrates.platform.crates.KeyManager;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Suggestion;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandTake extends BaseCommand {

    private final @NotNull KeyManager keyManager = this.plugin.getKeyManager();

    private final @NotNull UserManager userManager = this.plugin.getUserManager();

    @Command("take")
    @Permission(value = "crazycrates.takekey", def = PermissionDefault.OP)
    public void take(CommandSender sender, @Suggestion("keys") String keyName, @Suggestion("numbers") int amount, @Suggestion("players") CustomPlayer target) {
        Key key = this.keyManager.getKey(keyName);

        if (key == null) {
            return;
        }

        if (target.getPlayer() != null) {
            Player player = target.getPlayer();

            //todo() take keys.

            return;
        }

        OfflinePlayer offlinePlayer = target.getOfflinePlayer();

        //todo() take keys.
    }
}