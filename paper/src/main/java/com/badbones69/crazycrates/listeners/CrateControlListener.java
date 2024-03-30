package com.badbones69.crazycrates.listeners;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.events.KeyCheckEvent;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.other.CrateLocation;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import com.badbones69.crazycrates.tasks.InventoryManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;

import java.util.HashMap;
import java.util.Map;

public class CrateControlListener implements Listener {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull UserManager userManager = null;

    private final @NotNull CrateManager crateManager = null;

    private final @NotNull InventoryManager inventoryManager = null;

    private final @NotNull SettingsManager config = ConfigManager.getConfig();

    @EventHandler
    public void onLeftClickCrate(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        if (block == null) return;

        //CrateLocation crateLocation = this.crateManager.getCrateLocation(block.getLocation());

        //if (crateLocation == null) return;

        //Crate crate = crateLocation.getCrate();

        //boolean isKey = this.crateManager.hasKey(true, player, crate);

        if (true) {
            event.setCancelled(true);

            player.updateInventory();

            return;
        }

        event.setCancelled(true);

        if (player.getGameMode() == GameMode.CREATIVE && player.isSneaking() && player.hasPermission("crazycrates.admin")) {
            //if (crateLocation.getLocation().equals(block.getLocation())) {
            //    this.crateManager.removeCrateLocation(crateLocation.getID());

            //    player.sendMessage(Messages.removed_physical_crate.getMessage("{id}", crateLocation.getID(), player));
            //}

            return;
        }

        /*if (crate.isPreviewToggle()) {
            this.inventoryManager.addViewer(player);
            this.inventoryManager.openNewCratePreview(player, crate, crate.getCrateType() == CrateType.casino);
        } else {
            player.sendMessage(Messages.preview_disabled.getMessage("{crate}", crate.getName(), player));
        }*/
    }
    
    // This must run as highest, so it doesn't cause other plugins to check
    // the items that were added to the players inventory and replaced the item in the player's hand.
    // This is only an issue with QuickCrate
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        if (block == null) return;

        //CrateLocation crateLocation = this.crateManager.getCrateLocation(block.getLocation());

        //if (crateLocation == null) return;

        //Crate crate = crateLocation.getCrate();

        //boolean isKey = this.crateManager.hasKey(true, player, crate);

        if (true) {
            event.setCancelled(true);

            player.updateInventory();
        }

        event.setCancelled(true);

        /*if (crate.getCrateType() == CrateType.menu) {
            // this is to stop players in QuadCrate to not be able to try and open a crate set to menu.
            if (!this.crateManager.isInOpeningList(player) && this.config.getProperty(ConfigKeys.enable_crate_menu)) {
                CrateMainMenu crateMainMenu = new CrateMainMenu(player, this.config.getProperty(ConfigKeys.inventory_size), this.config.getProperty(ConfigKeys.inventory_name));

                player.openInventory(crateMainMenu.build().getInventory());
            } else {
                player.sendMessage(Messages.feature_disabled.getMessage(player));
            }

            return;
        }*/

        //KeyCheckEvent keyCheckEvent = new KeyCheckEvent(player, crateLocation);
        //player.getServer().getPluginManager().callEvent(keyCheckEvent);

        //if (keyCheckEvent.isCancelled()) return;

        boolean hasKey = false;
        boolean isPhysical = false;
        boolean useQuickCrateAgain = false;

        //int requiredKeys = this.crateManager.getCrate(crate.getName()).getRequiredKeys();

        //Key key = this.crateManager.getKeyFromCrate(crate.getName(), this.crateManager.getItemFromHand(player));

        //Key key = null;

        //if (key == null) return;

       // int totalKeys = this.userManager.getTotalKeys(player.getUniqueId(), crate.getName(), "");
        int totalKeys = 0;

        /*if (requiredKeys > 0 && totalKeys < requiredKeys) {
            Map<String, String> placeholders = new HashMap<>();

            placeholders.put("{key_amount}", String.valueOf(requiredKeys));
            placeholders.put("{crate}", crate.getPreviewName());
            placeholders.put("{amount}", String.valueOf(totalKeys));

            player.sendMessage(Messages.required_keys.getMessage(placeholders, player));

            return;
        }*/

        //if (crate.getCrateType() == CrateType.crate_on_the_go && isKey && this.config.getProperty(ConfigKeys.physical_accepts_physical_keys)) {
        //    hasKey = true;
        //    isPhysical = true;
        //}

        //if (this.config.getProperty(ConfigKeys.physical_accepts_virtual_keys) && this.userManager.getVirtualKeys(player.getUniqueId(), "") >= 1) {
        //    hasKey = true;
        //}

        Map<String, String> placeholders = new HashMap<>();

        //placeholders.put("{crate}", crate.getName());
        //placeholders.put("{key}", key.getName());

        if (hasKey) {
            //boolean isOpening = this.crateManager.isInOpeningList(player);
            //CrateType type = this.crateManager.getOpeningCrate(player).getCrateType();
            //boolean isActive = this.crateManager.isCrateActive(player);
            //boolean isLocationActive = this.crateManager.isCrateLocation(crateLocation.getLocation());

            //if (isOpening && type == CrateType.quick_crate && isActive && isLocationActive) useQuickCrateAgain = true;

            if (!useQuickCrateAgain) {
                //if (this.crateManager.isInOpeningList(player)) {
                //    player.sendMessage(Messages.already_opening_crate.getMessage("{crate}", crate.getName(), player));

                //    return;
                //}

                //if (this.crateManager.containsActiveLocation(crateLocation.getLocation())) {
                    //player.sendMessage(Messages.crate_in_use.getMessage("{crate}", crate.getName(), player));

                    //return;
                //}
            }

            if (MiscUtils.isInventoryFull(player)) {
                //player.sendMessage(Messages.inventory_not_empty.getMessage("{crate}", crate.getName(), player));

                return;
            }

            //if (useQuickCrateAgain) this.crateManager.endQuickCrate(player, crateLocation.getLocation(), crate, true);

            KeyType keyType = isPhysical ? KeyType.physical_key : KeyType.virtual_key;

            // Only cosmic crate type uses this method.
            //if (crate.getCrateType() == CrateType.cosmic) this.crateManager.addPlayerKeyType(player, keyType);

            //this.crateManager.addPlayerToOpeningList(player, crate);

            //this.crateManager.openCrate(player, crate, key, keyType, crateLocation.getLocation(), false, true);

            return;
        }

        //if (crate.getCrateType() != CrateType.crate_on_the_go) {
            if (this.config.getProperty(ConfigKeys.knock_back)) knockBack(player, block.getLocation());

            if (this.config.getProperty(ConfigKeys.need_key_sound_toggle)) {
                player.playSound(player.getLocation(), Sound.valueOf(this.config.getProperty(ConfigKeys.need_key_sound)), SoundCategory.PLAYERS, 1f, 1f);
            }

            player.sendMessage(Messages.no_keys.getMessage(placeholders, player));
        //}
    }

    @EventHandler
    public void onPistonPushCrate(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            Location location = block.getLocation();

            //Crate crate = this.crateManager.getCrate(location);

            //if (crate != null) {
            //    event.setCancelled(true);

            //    return;
            //}
        }
    }

    @EventHandler
    public void onPistonPullCrate(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            Location location = block.getLocation();

            //Crate crate = this.crateManager.getCrate(location);

            //if (crate != null) {
            //    event.setCancelled(true);

           //     return;
            //}
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        //if (this.crateManager.hasActiveTask(player)) this.crateManager.endActiveTask(player);

        //if (this.crateManager.hasActiveQuadTask(player)) this.crateManager.endActiveQuadTask(player);

        //if (this.crateManager.isInOpeningList(player)) this.crateManager.removePlayerFromOpeningList(player);
    }
    
    private void knockBack(Player player, Location location) {
        Vector vector = player.getLocation().toVector().subtract(location.toVector()).normalize().multiply(1).setY(.1);

        if (player.isInsideVehicle() && player.getVehicle() != null) {
            player.getVehicle().setVelocity(vector);

            return;
        }

        player.setVelocity(vector);
    }
}