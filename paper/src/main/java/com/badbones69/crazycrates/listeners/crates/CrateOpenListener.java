package com.badbones69.crazycrates.listeners.crates;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.events.CrateOpenEvent;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;

public class CrateOpenListener implements Listener {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull UserManager userManager = null;

    private final @NotNull CrateManager crateManager = null;

    @EventHandler
    public void onCrateOpen(CrateOpenEvent event) {
        Player player = event.getPlayer();
        Crate crate = event.getCrate();

        if (crate.getCrateType() != CrateType.menu) {
            if (!crate.canWinPrizes(player)) {
                player.sendMessage(Messages.no_prizes_found.getMessage("{crate}", crate.getName(), player));

                //this.crateManager.removePlayerFromOpeningList(player);
                //this.crateManager.removePlayerKeyType(player);

                event.setCancelled(true);

                return;
            }
        }

        if (!player.hasPermission("crazycrates.open." + crate.getName()) || !player.hasPermission("crazycrates.open." + crate.getName().toLowerCase())) {
            player.sendMessage(Messages.no_crate_permission.getMessage("{crate}", crate.getName(), player));

            //this.crateManager.removePlayerFromOpeningList(player);
            //this.crateManager.removeActiveCrate(player);

            event.setCancelled(true);

            return;
        }

        //this.crateManager.addPlayerToOpeningList(player, crate);

        //if (crate.getCrateType() != CrateType.cosmic) this.userManager.addOpenedCrate(player.getUniqueId(), crate.getName());

        crate.broadcast(player);
        crate.execute(player);
    }
}