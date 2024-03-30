package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.builders.CrateBuilder;
import us.crazycrew.crazycrates.api.enums.types.KeyType;

public class CrateOnTheGo extends CrateBuilder {

    private final @NotNull CrateManager crateManager = null;
    private final @NotNull UserManager userManager = null;

    public CrateOnTheGo(Key key, Crate crate, Player player) {
        super(key, crate, player);
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
        //boolean keyCheck = this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), key.getName(), true, true);

        if (!true) {
            // Send the message about failing to take the key.
            //MiscUtils.failedToTakeKey(player, crate.getName(), key.getName());

            // Remove from opening list.
            //this.crateManager.removePlayerFromOpeningList(player);

            return;
        }

        crate.givePrize(player, crate.pickPrize(player));

        //this.crateManager.removePlayerKeyType(player);
    }

    @Override
    public void run() {

    }
}