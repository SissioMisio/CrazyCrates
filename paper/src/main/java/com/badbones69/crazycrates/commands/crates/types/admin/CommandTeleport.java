package com.badbones69.crazycrates.commands.crates.types.admin;

import com.badbones69.crazycrates.commands.crates.BaseCommand;
import com.badbones69.crazycrates.platform.utils.MsgUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Suggestion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class CommandTeleport extends BaseCommand {

    private final @NotNull FileConfiguration locations = null;

    @Command("tp")
    @Permission(value = "crazycrates.teleport", def = PermissionDefault.OP)
    public void onAdminTeleport(Player player, @Suggestion("locations") String id) {
        ConfigurationSection section = this.locations.getConfigurationSection("Locations");

        if (section == null) {
            this.locations.set("Locations.Clear", null);

            //Files.LOCATIONS.saveFile();

            return;
        }

        for (String name : section.getKeys(false)) {
            if (name.equalsIgnoreCase(id)) {
                World world = this.plugin.getServer().getWorld(Objects.requireNonNull(this.locations.getString("Locations." + name + ".World")));

                int x = this.locations.getInt("Locations." + name + ".X");
                int y = this.locations.getInt("Locations." + name + ".Y");
                int z = this.locations.getInt("Locations." + name + ".Z");

                Location loc = new Location(world, x, y, z);

                player.teleport(loc.add(.5, 0, .5));

                player.sendMessage(MsgUtils.getPrefix("&7You have been teleported to &6" + name + "&7."));

                return;
            }
        }

        player.sendMessage(MsgUtils.getPrefix("&cThere is no location called &6" + id + "&c."));
    }
}