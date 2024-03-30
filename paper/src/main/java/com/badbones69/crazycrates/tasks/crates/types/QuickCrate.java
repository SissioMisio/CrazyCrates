package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.ChestManager;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.Prize;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.builders.CrateBuilder;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import java.util.logging.Level;

public class QuickCrate extends CrateBuilder {

    private final @NotNull UserManager userManager = null;
    private final @NotNull CrateManager crateManager = null;

    public QuickCrate(Key key, Crate crate, Player player, Location location) {
        super(key, crate, player, location);
    }

    @Override
    public void open(KeyType keyType, boolean checkHand) {
        if (!isFireCracker()) {
            if (isCrateEventValid(keyType, checkHand)) {
                return;
            }
        }

        Crate crate = getCrate();
        Key key = getKey();
        Player player = getPlayer();

        // If the crate type is not fire cracker.
        //this.crateManager.addActiveCrate(player, getLocation());

        int keys = switch (keyType) {
            //case virtual_key -> this.userManager.getVirtualKeys(player.getUniqueId(), key.getName());
            //case physical_key -> this.userManager.getPhysicalKeys(player.getUniqueId(), crate.getName(), key.getName());
            default -> 1;
        };

        if (getPlayer().isSneaking() && keys > 1) {
            int used = 0;

            for (;keys > 0; keys--) {
                if (MiscUtils.isInventoryFull(player)) break;
                if (used >= crate.getMaxMassOpen()) break;

                crate.givePrize(player, crate.pickPrize(player));

                used++;
            }

            //boolean keyCheck = this.userManager.takeKeys(used, player.getUniqueId(), crate.getName(), key.getName(), true, false);

            if (!true) {
                // Send the message about failing to take the key.
                //MiscUtils.failedToTakeKey(player, crate.getName(), key.getName());

                // Remove from opening list.
                //this.crateManager.removePlayerFromOpeningList(player);

                return;
            }

            //this.crateManager.endQuickCrate(player, getLocation(), crate, true);

            return;
        }

        //boolean keyCheck = this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), key.getName(), true, true);

        if (!true) {
            // Send the message about failing to take the key.
            //MiscUtils.failedToTakeKey(player, crate.getName(), key.getName());

            // Remove from opening list.
            //this.crateManager.removePlayerFromOpeningList(player);

            return;
        }

        Prize prize = crate.pickPrize(player, getLocation().clone().add(.5, 1.3, .5));
        crate.givePrize(player, prize);

        boolean showQuickCrateItem = ConfigManager.getConfig().getProperty(ConfigKeys.show_quickcrate_item);

        // Only related to the item above the crate.
        if (showQuickCrateItem) {
            // Get the display item.
            ItemStack display = prize.getDisplayItem(player);

            // Get the item meta.
            ItemMeta itemMeta = display.getItemMeta();

            // Access the pdc and set "crazycrates-item"
            PersistentKeys type = PersistentKeys.crate_prize;

            itemMeta.getPersistentDataContainer().set(type.getNamespacedKey(), type.getType(), "1");

            // Set the item meta.
            display.setItemMeta(itemMeta);

            Item reward;

            try {
                reward = getPlayer().getWorld().dropItem(getLocation().clone().add(.5, 1, .5), display);
            } catch (IllegalArgumentException exception) {
                this.plugin.getLogger().warning("A prize could not be given due to an invalid display item for this prize.");
                this.plugin.getLogger().log(Level.WARNING, "Crate: " + prize.getCrateName() + " Prize: " + prize.getPrizeName(), exception);

                return;
            }

            reward.setMetadata("betterdrops_ignore", new FixedMetadataValue(this.plugin, true));
            reward.setVelocity(new Vector(0, .2, 0));
            reward.setCustomName(itemMeta.getDisplayName());
            reward.setCustomNameVisible(true);
            reward.setPickupDelay(-1);

            //this.crateManager.addReward(getPlayer(), reward);

            // Always open the chest.
            ChestManager.openChest(getLocation().getBlock(), true);

            // Always spawn fireworks if enabled.
            if (prize.isFirework()) MiscUtils.spawnFirework(getLocation().clone().add(0.5, 1, .5), null);

            // Always end the crate.
            addCrateTask(new BukkitRunnable() {
                @Override
                public void run() {
                    //crateManager.endQuickCrate(player, getLocation(), crate, false);
                }
            }.runTaskLater(this.plugin, 5 * 20));

            return;
        }

        // Always open the chest.
        ChestManager.openChest(getLocation().getBlock(), true);

        // Always spawn fireworks if enabled.
        if (prize.isFirework()) MiscUtils.spawnFirework(getLocation().clone().add(0.5, 1, .5), null);

        // Always end the crate.
        addCrateTask(new BukkitRunnable() {
            @Override
            public void run() {
                //crateManager.endQuickCrate(getPlayer(), getLocation(), getCrate(), false);
            }
        }.runTaskLater(this.plugin, 40));
    }
    @Override
    public void run() {

    }
}