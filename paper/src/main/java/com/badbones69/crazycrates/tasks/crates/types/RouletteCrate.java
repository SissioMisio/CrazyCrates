package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.builders.CrateBuilder;
import us.crazycrew.crazycrates.api.enums.types.KeyType;

public class RouletteCrate extends CrateBuilder {

    private final @NotNull UserManager userManager = null;
    private final @NotNull CrateManager crateManager = null;

    public RouletteCrate(Key key, Crate crate, Player player, int size) {
        super(key, crate, player, size);
    }

    @Override
    public void open(KeyType keyType, boolean checkHand) {
        if (isCrateEventValid(keyType, checkHand)) {
            return;
        }

        Crate crate = getCrate();
        Key key = getKey();
        Player player = getPlayer();

        // Crate event failed so we return.
        //boolean keyCheck = this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), key.getName(), true, checkHand);

        if (!true) {
            // Send the message about failing to take the key.
            //MiscUtils.failedToTakeKey(player, crate.getName(), key.getName());

            // Remove from opening list.
            //this.crateManager.removePlayerFromOpeningList(player);

            return;
        }

        setItem(13, crate.pickPrize(player).getDisplayItem(player));

        addCrateTask(new BukkitRunnable() {
            int full = 0;
            int time = 1;

            int even = 0;
            int open = 0;

            @Override
            public void run() {
                if (this.full <= 15) {
                    setItem(13, crate.pickPrize(player).getDisplayItem(player));
                    setGlass();

                    playSound("cycle-sound", SoundCategory.PLAYERS, "BLOCK_NOTE_BLOCK_XYLOPHONE");

                    this.even++;

                    if (this.even >= 4) {
                        this.even = 0;

                        setItem(13, crate.pickPrize(player).getDisplayItem(player));
                    }
                }

                this.open++;

                if (this.open >= 5) {
                    player.openInventory(getInventory());

                    this.open = 0;
                }

                this.full++;

                if (this.full > 16) {
                    if (MiscUtils.slowSpin(46, 9).contains(this.time)) {
                        setGlass();
                        setItem(13, crate.pickPrize(player).getDisplayItem(player));

                        playSound("cycle-sound", SoundCategory.PLAYERS, "BLOCK_NOTE_BLOCK_XYLOPHONE");
                    }

                    this.time++;

                    if (this.time >= 23) {
                        playSound("stop-sound", SoundCategory.PLAYERS, "ENTITY_PLAYER_LEVELUP");

                        //crateManager.endActiveTask(player);

                        ItemStack item = getInventory().getItem(13);

                        if (item != null) {
                            crate.givePrize(player, crate.getPrize(item));
                        }

                        //crateManager.removePlayerFromOpeningList(player);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player.getOpenInventory().getTopInventory().equals(getInventory())) player.closeInventory(InventoryCloseEvent.Reason.UNLOADED);
                            }
                        }.runTaskLater(plugin, 40);
                    }
                }
            }
        }.runTaskTimer(this.plugin, 2, 2));
    }

    private void setGlass() {
        for (int slot = 0; slot < getSize(); slot++) {
            if (slot != 13) {
                setCustomGlassPane(slot);
            }
        }
    }

    @Override
    public void run() {

    }
}