package com.badbones69.crazycrates.listeners.crates;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.events.KeyCheckEvent;
import com.badbones69.crazycrates.api.objects.other.CrateLocation;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.tasks.BukkitUserManager;
import com.badbones69.crazycrates.tasks.crates.CrateManager;
import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.events.CrateOpenEvent;
import com.badbones69.crazycrates.api.objects.Crate;
import com.ryderbelserion.vital.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import com.badbones69.crazycrates.api.utils.MsgUtils;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import java.util.List;

public class CrateOpenListener implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();

    private final @NotNull BukkitUserManager userManager = this.plugin.getUserManager();

    private final @NotNull SettingsManager config = ConfigManager.getConfig();

    @EventHandler
    public void onKeyCheck(KeyCheckEvent event) {
        CrateLocation location = event.getCrateLocation();
        Player player = event.getPlayer();

        if (location == null) {
            event.setCancelled(true);

            return;
        }

        Crate crate = location.getCrate();

        if (crate == null || crate.getCrateType() == CrateType.menu) {
            event.setCancelled(true);

            return;
        }

        boolean lackingKey = true;
        KeyType keyType = null;

        if (this.config.getProperty(ConfigKeys.physical_accepts_virtual_keys)) {
            lackingKey = this.userManager.getVirtualKeys(player.getUniqueId(), crate.getName()) >= 1;
            keyType = KeyType.virtual_key;
        } else {
            if (this.config.getProperty(ConfigKeys.physical_accepts_physical_keys)) {
                if (this.userManager.hasPhysicalKey(player.getUniqueId(), crate.getName(), false)) {
                    keyType = KeyType.physical_key;
                    lackingKey = false;
                }
            }
        }

        if (lackingKey) {
            if (this.config.getProperty(ConfigKeys.need_key_sound_toggle)) {
                player.playSound(player.getLocation(), Sound.valueOf(this.config.getProperty(ConfigKeys.need_key_sound)), SoundCategory.PLAYERS, 1f, 1f);
            }

            player.sendMessage(Messages.required_keys.getMessage("{crate}", crate.getName(), player));

            event.setCancelled(true);

            return;
        }

        event.setCancelled(lackingKey);
        event.setKeyType(keyType);
    }

    @EventHandler
    public void onCrateOpen(CrateOpenEvent event) {
        Player player = event.getPlayer();
        Crate crate = event.getCrate();

        if (crate.getCrateType() != CrateType.menu) {
            if (!crate.canWinPrizes(player)) {
                player.sendMessage(Messages.no_prizes_found.getMessage("{crate}", crate.getName(), player));

                this.crateManager.removePlayerFromOpeningList(player);
                this.crateManager.removePlayerKeyType(player);

                event.setCancelled(true);

                return;
            }
        }

        for (String world : this.config.getProperty(ConfigKeys.disabled_worlds)) {
            if (world.equalsIgnoreCase(player.getWorld().getName())) {
                player.sendMessage(Messages.world_disabled.getMessage("{world}", player.getWorld().getName(), player));

                return;
            }
        }

        if (MiscUtils.isInventoryFull(player)) {
            player.sendMessage(Messages.inventory_not_empty.getMessage("{crate}", crate.getName(), player));

            return;
        }

        if (!player.hasPermission("crazycrates.open." + crate.getName()) || !player.hasPermission("crazycrates.open." + crate.getName().toLowerCase())) {
            player.sendMessage(Messages.no_crate_permission.getMessage("{crate}", crate.getName(), player));

            this.crateManager.removePlayerFromOpeningList(player);
            this.crateManager.removeCrateInUse(player);

            event.setCancelled(true);

            return;
        }

        this.crateManager.addPlayerToOpeningList(player, crate);

        if (crate.getCrateType() != CrateType.cosmic) this.userManager.addOpenedCrate(player.getUniqueId(), crate.getName());

        FileConfiguration configuration = event.getConfiguration();

        String broadcastMessage = configuration.getString("Crate.BroadCast", "");
        boolean broadcastToggle = configuration.contains("Crate.OpeningBroadCast") && configuration.getBoolean("Crate.OpeningBroadCast");

        if (broadcastToggle) {
            if (!broadcastMessage.isBlank()) {
                this.plugin.getServer().broadcastMessage(MsgUtils.color(broadcastMessage.replaceAll("%prefix%", MsgUtils.getPrefix())).replaceAll("%player%", player.getName()));
            }
        }

        boolean commandToggle = configuration.contains("Crate.opening-command") && configuration.getBoolean("Crate.opening-command.toggle");

        if (commandToggle) {
            List<String> commands = configuration.getStringList("Crate.opening-command.commands");

            if (!commands.isEmpty()) {
                commands.forEach(line -> {
                    String builder;

                    if (Support.placeholder_api.isEnabled() ) {
                        builder = PlaceholderAPI.setPlaceholders(player, line.replaceAll("%prefix%", MsgUtils.getPrefix()).replaceAll("%player%", player.getName()));
                    } else {
                        builder = line.replaceAll("%prefix%", MsgUtils.getPrefix()).replaceAll("%player%", player.getName());
                    }

                    this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), builder);
                });
            }
        }
    }
}