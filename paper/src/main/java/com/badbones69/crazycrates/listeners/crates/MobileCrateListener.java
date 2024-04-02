package com.badbones69.crazycrates.listeners.crates;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MobileCrateListener implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final @NotNull CrateManager crateManager = null;

    @EventHandler
    public void onCrateUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) return;

        if (!item.hasItemMeta()) return;

        ItemMeta itemMeta = item.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        //Crate crate = this.crateManager.getCrate(container.get(PersistentKeys.crate_key.getNamespacedKey(), PersistentDataType.STRING));

        //if (crate == null) return;

        //if (crate.getCrateType() != CrateType.crate_on_the_go) return;

        //boolean isKey = this.crateManager.hasKey(true, player, crate);

        //if (!isKey) return;

        event.setCancelled(true);

        //this.crateManager.addPlayerToOpeningList(player, crate);

        //ItemUtils.removeItem(item, player);

        //crate.givePrize(player, crate.pickPrize(player));

        //this.crateManager.removePlayerFromOpeningList(player);
    }
}