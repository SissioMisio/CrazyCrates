package com.badbones69.crazycrates.listeners;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.builders.types.CrateAdminMenu;
import com.badbones69.crazycrates.api.builders.types.CrateMainMenu;
import com.badbones69.crazycrates.api.builders.types.CratePreviewMenu;
import com.badbones69.crazycrates.api.builders.types.CratePrizeMenu;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.tasks.InventoryManager;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;

public class MiscListener implements Listener {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull InventoryManager inventoryManager = null;

    private final @NotNull CrateManager crateManager = null;

    private final @NotNull UserManager userManager = null;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Set new keys if we have to.
        //this.crateManager.setNewPlayerKeys(player);

        // Just in case any old data is in there.
        //this.userManager.loadOldOfflinePlayersKeys(player, this.crateManager.getKeys());

        // Also add the new data.
        //this.userManager.loadOfflinePlayersKeys(player, this.crateManager.getKeys());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAttemptPickUp(PlayerAttemptPickupItemEvent event) {
        //if (this.crateManager.isDisplayReward(event.getItem())) {
            event.setCancelled(true);

            //return;
        //}

        Player player = event.getPlayer();

        //if (this.crateManager.isInOpeningList(player)) {
            // DrBot Start
            //if (this.crateManager.getOpeningCrate(player).getCrateType().equals(CrateType.quick_crate)) return;

            // DrBot End
            event.setCancelled(true);
        //}
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.inventoryManager.removeViewer(player);
        this.inventoryManager.removeCrateViewer(player);
        this.inventoryManager.removePageViewer(player);

        //this.crateManager.endQuickCrate(player, player.getLocation(), this.crateManager.getOpeningCrate(player), false);

        // End just in case.
       // this.crateManager.endActiveTask(player);
        //this.crateManager.endActiveQuadTask(player);

        //this.crateManager.removeCloser(player);
        //this.crateManager.removeHands(player);
        //this.crateManager.removePicker(player);
        //this.crateManager.removePlayerKeyType(player);
    }

    @EventHandler
    public void onItemPickUp(InventoryPickupItemEvent event) {
        //if (this.crateManager.isDisplayReward(event.getItem())) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Inventory inventory = event.getView().getTopInventory();

        if (inventory.getHolder(false) instanceof CrateAdminMenu || inventory.getHolder(false) instanceof CrateMainMenu || inventory.getHolder(false) instanceof CratePreviewMenu || inventory.getHolder(false) instanceof CratePrizeMenu) {
            event.setCancelled(true);
        }
    }
}