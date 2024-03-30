package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.other.CrateLocation;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.support.StructureHandler;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import com.badbones69.crazycrates.tasks.crates.other.quadcrates.QuadCrateManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.crates.quadcrates.CrateSchematic;
import com.badbones69.crazycrates.api.builders.CrateBuilder;
import us.crazycrew.crazycrates.api.enums.types.KeyType;

import java.util.concurrent.ThreadLocalRandom;

public class QuadCrate extends CrateBuilder {

    private final @NotNull CrateManager crateManager = null;

    private final Location location;

    public QuadCrate(Key key, Crate crate, Player player, Location location) {
        super(key, crate, player);

        this.location = location;
    }

    @Override
    public void open(KeyType keyType, boolean checkHand) {
        if (isCrateEventValid(keyType, checkHand)) {
            return;
        }

        Crate crate = getCrate();
        Player player = getPlayer();

        //CrateSchematic crateSchematic = this.crateManager.getCrateSchematics().get(ThreadLocalRandom.current().nextInt(this.crateManager.getCrateSchematics().size()));
        //StructureHandler handler = new StructureHandler(crateSchematic.getSchematicFile());
        //CrateLocation crateLocation = this.crateManager.getCrateLocation(this.location);
        //QuadCrateManager session = new QuadCrateManager(player, getKey(), crate, keyType, crateLocation.getLocation(), checkHand, handler);

        //session.startCrate();
    }

    @Override
    public void run() {

    }
}