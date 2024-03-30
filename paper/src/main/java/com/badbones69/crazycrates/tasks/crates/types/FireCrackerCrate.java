package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.builders.CrateBuilder;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FireCrackerCrate extends CrateBuilder {

    private final @NotNull UserManager userManager = null;
    private final @NotNull CrateManager crateManager = null;

    public FireCrackerCrate(Key key, Crate crate, Player player, int size, Location location) {
        super(key, crate, player, size, location);
    }

    @Override
    public void open(KeyType keyType, boolean checkHand) {
        if (isCrateEventValid(keyType, checkHand)) {
            return;
        }

        Crate crate = getCrate();
        Key key = getKey();
        Player player = getPlayer();

        Location location = getLocation();

        // Crate event failed so we return.
        //this.crateManager.addActiveCrate(player, location);

        //boolean keyCheck = this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), key.getName(), true, checkHand);

        if (!true) {
            // Send the message about failing to take the key.
            //MiscUtils.failedToTakeKey(player, crate.getName(), key.getName());

            // Remove from opening list.
            //this.crateManager.removePlayerFromOpeningList(player);

            return;
        }

        //if (this.crateManager.getHologramManager() != null) {
            //this.crateManager.getHologramManager().removeHologram(location.getBlock());
        //}

        List<Color> colors = Arrays.asList(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.BLACK, Color.AQUA, Color.MAROON, Color.PURPLE);

        addCrateTask(new BukkitRunnable() {
            final int random = ThreadLocalRandom.current().nextInt(colors.size());
            final Location clonedLocation = getLocation().clone().add(.5, 25, .5);

            int length = 0;

            @Override
            public void run() {
                this.clonedLocation.subtract(0, 1, 0);

                MiscUtils.spawnFirework(this.clonedLocation, colors.get(this.random));

                this.length++;

                if (this.length == 25) {
                    //crateManager.endActiveTask(player);

                    //QuickCrate quickCrate = new QuickCrate(key, crate, player, location);

                    //quickCrate.open(KeyType.free_key, false);
                }
            }
        }.runTaskTimer(this.plugin, 0, 2));
    }

    @Override
    public void run() {

    }
}