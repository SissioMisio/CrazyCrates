package com.badbones69.crazycrates.listeners;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.other.BrokeLocation;
import com.badbones69.crazycrates.api.objects.other.CrateLocation;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Only use for this class is to check if for broken locations and to try and fix them when the server loads the world.
public class BrokeLocationsListener implements Listener {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);
    private final @NotNull CrateManager crateManager = null;
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        Set<BrokeLocation> brokeLocations = Collections.emptySet();

        if (brokeLocations.isEmpty()) return;

        int fixedAmount = 0;
        Set<BrokeLocation> fixedWorlds = new HashSet<>();

        for (BrokeLocation brokeLocation : brokeLocations) {
            Location location = brokeLocation.getLocation();

            if (location.getWorld() != null) {
                if (brokeLocation.getCrate() != null) {
                    //this.crateManager.addLocation(new CrateLocation(brokeLocation.getLocationName(), brokeLocation.getCrate(), location));

                    Crate crate = brokeLocation.getCrate();

                    //if (this.crateManager.getHologramManager() != null && crate.getCrateHologram().isEnabled()) {
                    //    this.crateManager.getHologramManager().createHologram(location.getBlock(), crate);
                    //}

                    fixedWorlds.add(brokeLocation);

                    fixedAmount++;
                }
            }
        }

        //this.crateManager.removeBrokeLocation(fixedWorlds);

        if (MiscUtils.isLogging()) {
            this.plugin.getLogger().warning("Fixed " + fixedAmount + " broken crate locations.");

            if (brokeLocations.isEmpty()) this.plugin.getLogger().warning("All broken crate locations have been fixed.");
        }
    }
}