package com.badbones69.crazycrates.commands.crates.types.admin.keys;

import com.badbones69.crazycrates.commands.crates.BaseCommand;
import com.badbones69.crazycrates.platform.CustomPlayer;
import com.badbones69.crazycrates.platform.crates.KeyManager;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MsgUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Suggestion;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandGive extends BaseCommand {

    private final @NotNull KeyManager keyManager = this.plugin.getKeyManager();

    private final @NotNull UserManager userManager = this.plugin.getUserManager();

    @Command("give")
    @Permission(value = "crazycrates.givekey", def = PermissionDefault.OP)
    public void give(CommandSender sender, @Suggestion("keys") String keyName, @Suggestion("numbers") int amount, @Suggestion("players") CustomPlayer target) {
        Key key = this.keyManager.getKey(keyName);

        if (key == null) {
            return;
        }

        if (target.getPlayer() != null) {
            Player player = target.getPlayer();

            this.userManager.addKeys(player.getUniqueId(), key.getKeyName(), key.isVirtual(), amount);

            return;
        }

        OfflinePlayer offlinePlayer = target.getOfflinePlayer();

        this.userManager.addVirtualKeys(offlinePlayer.getUniqueId(), key.getKeyName(), amount);
    }
}