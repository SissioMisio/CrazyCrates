package com.badbones69.crazycrates.listeners.crates;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.Prize;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.builders.types.CratePrizeMenu;
import us.crazycrew.crazycrates.api.enums.types.CrateType;

public class WarCrateListener implements Listener {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull CrateManager crateManager = null;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder(false) instanceof CratePrizeMenu holder)) return;

        Player player = holder.getPlayer();

        event.setCancelled(true);

        /*if (this.crateManager.containsPicker(player) && this.crateManager.isInOpeningList(player)) {
            Crate crate = this.crateManager.getOpeningCrate(player);

            if (crate.getCrateType() == CrateType.war && this.crateManager.isPicker(player)) {
                ItemStack item = event.getCurrentItem();

                if (item != null && item.getType().toString().contains(Material.GLASS_PANE.toString())) {
                    int slot = event.getRawSlot();
                    Prize prize = crate.pickPrize(player);
                    inventory.setItem(slot, prize.getDisplayItem(player));

                    if (this.crateManager.hasActiveTask(player)) this.crateManager.endActiveTask(player);

                    this.crateManager.removePicker(player);
                    this.crateManager.addCloser(player, true);

                    crate.givePrize(player, prize);

                    this.crateManager.removePlayerFromOpeningList(player);

                    crate.playSound(player, "cycle-sound", "BLOCK_ANVIL_LAND", SoundCategory.PLAYERS);

                    this.crateManager.addActiveTask(player, new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 9; i++) {
                                if (i != slot) inventory.setItem(i, crate.pickPrize(player).getDisplayItem(player));
                            }

                            if (crateManager.hasActiveTask(player)) crateManager.endActiveTask(player);

                            // Removing other items then the prize.
                            crateManager.addActiveTask(player, new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < 9; i++) {
                                        if (i != slot) inventory.setItem(i, new ItemStack(Material.AIR));
                                    }

                                    if (crateManager.hasActiveTask(player)) crateManager.endActiveTask(player);

                                    // Closing the inventory when finished.
                                    crateManager.addActiveTask(player, new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (crateManager.hasActiveTask(player)) crateManager.endActiveTask(player);

                                            crateManager.removePlayerFromOpeningList(player);

                                            player.closeInventory();
                                        }
                                    }.runTaskLater(plugin, 30));
                                }
                            }.runTaskLater(plugin, 30));
                        }
                    }.runTaskLater(this.plugin, 30));
                }
            }
        }*/
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        /*if (this.crateManager.containsPicker(player) && this.crateManager.isPicker(player)) {
            for (Crate crate : this.crateManager.getCrates()) {
                if (crate.getCrateType() == CrateType.war && event.getInventory().getHolder(false) instanceof CratePrizeMenu) {
                    if (this.crateManager.hasActiveTask(player)) {
                        this.crateManager.removeCloser(player);

                        this.crateManager.endActiveTask(player);
                    }
                }
            }
        }*/
    }
}