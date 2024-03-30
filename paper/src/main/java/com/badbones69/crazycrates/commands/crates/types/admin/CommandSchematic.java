package com.badbones69.crazycrates.commands.crates.types.admin;

import com.badbones69.crazycrates.commands.crates.BaseCommand;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class CommandSchematic extends BaseCommand {

    @Command("save")
    @Permission(value = "crazycrates.save", def = PermissionDefault.OP)
    public void onSchematicSave(Player player) {

    }

    @Command("wand")
    @Permission(value = "crazycrates.wand", def = PermissionDefault.OP)
    public void onSchematicWand(Player player) {

    }
}