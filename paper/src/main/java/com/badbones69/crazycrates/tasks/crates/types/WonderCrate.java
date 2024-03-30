package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.Prize;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.builders.CrateBuilder;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import java.util.ArrayList;
import java.util.List;

public class WonderCrate extends CrateBuilder {

    private final @NotNull UserManager userManager = null;
    private final @NotNull CrateManager crateManager = null;

    public WonderCrate(Key key, Crate crate, Player player, int size) {
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
           // this.crateManager.removePlayerFromOpeningList(player);

            return;
        }

        final List<String> slots = new ArrayList<>();

        for (int index = 0; index < getSize(); index++) {
            Prize prize = crate.pickPrize(player);
            slots.add(String.valueOf(index));

            setItem(index, prize.getDisplayItem(player));
        }

        getPlayer().openInventory(getInventory());

        addCrateTask(new BukkitRunnable() {
            int time = 0;
            int full = 0;

            int slot1 = 0;
            int slot2 = 44;

            final List<Integer> other = new ArrayList<>();

            Prize prize = null;

            @Override
            public void run() {
                if (this.time >= 2 && this.full <= 65) {
                    slots.remove(this.slot1 + "");
                    slots.remove(this.slot2 + "");

                    other.add(this.slot1);
                    other.add(this.slot2);

                    ItemStack material = new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();

                    setItem(this.slot1, material);
                    setItem(this.slot2, material);

                    for (String slot : slots) {
                        this.prize = crate.pickPrize(player);

                        setItem(Integer.parseInt(slot), this.prize.getDisplayItem(player));
                    }

                    this.slot1++;
                    this.slot2--;
                }

                if (this.full > 67) {
                    for (int slot : this.other) {
                        setCustomGlassPane(slot);
                    }
                }

                player.openInventory(getInventory());

                if (this.full > 100) {
                    //crateManager.endActiveTask(player);

                    player.closeInventory(InventoryCloseEvent.Reason.UNLOADED);

                    crate.givePrize(player, this.prize);

                    playSound("stop-sound", SoundCategory.PLAYERS, "ENTITY_PLAYER_LEVELUP");

                    //crateManager.removePlayerFromOpeningList(getPlayer());

                    return;
                }

                this.full++;
                this.time++;

                if (this.time > 2) this.time = 0;
            }
        }.runTaskTimer(this.plugin, 0, 2));
    }

    @Override
    public void run() {

    }
}