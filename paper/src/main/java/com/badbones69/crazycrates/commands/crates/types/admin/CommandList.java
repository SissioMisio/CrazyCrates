package com.badbones69.crazycrates.commands.crates.types.admin;

import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.other.CrateLocation;
import com.badbones69.crazycrates.commands.crates.BaseCommand;
import com.badbones69.crazycrates.platform.utils.MsgUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class CommandList extends BaseCommand {

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();

    @Command("list")
    @Permission(value = "crazycrates.list", def = PermissionDefault.OP)
    public void onAdminList(CommandSender sender) {
        StringBuilder crates = new StringBuilder();
        String brokeCrates;

        this.crateManager.getCrates().forEach(crate -> crates.append("&a").append(crate.getName()).append("&8, "));

        StringBuilder brokeCratesBuilder = new StringBuilder();

        this.crateManager.getBrokenCrates().forEach(crate -> brokeCratesBuilder.append("&c").append(crate).append(".yml&8,"));

        brokeCrates = brokeCratesBuilder.toString();

        sender.sendMessage(MsgUtils.color("&e&lCrates:&f " + crates));

        if (!brokeCrates.isEmpty()) sender.sendMessage(MsgUtils.color("&6&lBroken Crates:&f " + brokeCrates.substring(0, brokeCrates.length() - 2)));

        sender.sendMessage(MsgUtils.color("&e&lAll Crate Locations:"));
        sender.sendMessage(MsgUtils.color("&c[ID]&8, &c[Crate]&8, &c[World]&8, &c[X]&8, &c[Y]&8, &c[Z]"));
        int line = 1;

        //todo() add crate locations
        /*for (CrateLocation loc : new ArrayList<CrateLocation>()) {
            Crate crate = loc.getCrate();
            String world = loc.getLocation().getWorld().getName();

            int x = loc.getLocation().getBlockX();
            int y = loc.getLocation().getBlockY();
            int z = loc.getLocation().getBlockZ();

            sender.sendMessage(MsgUtils.color("&8[&b" + line + "&8]: " + "&c" + loc.getID() + "&8, &c" + crate.getName() + "&8, &c" + world + "&8, &c" + x + "&8, &c" + y + "&8, &c" + z));
            line++;
        }*/
    }
}