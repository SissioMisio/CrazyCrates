package com.badbones69.crazycrates.support.holograms.types;

import com.badbones69.crazycrates.api.crates.CrateHologram;
import com.badbones69.crazycrates.api.objects.Crate;
import eu.decentsoftware.holograms.api.DHAPI;
import org.bukkit.Location;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import com.badbones69.crazycrates.support.holograms.HologramManager;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import java.util.HashMap;
import java.util.Map;

public class DecentHologramsSupport extends HologramManager {

    private final Map<String, Hologram> holograms = new HashMap<>();

    @Override
    public void createHologram(final Location location, final Crate crate, final String id) {
        if (crate.getCrateType() == CrateType.menu) return;

        final CrateHologram crateHologram = crate.getHologram();

        if (!crateHologram.isEnabled()) {
            removeHologram(id);

            return;
        }

        // We don't want to create a new one if one already exists.
        if (exists(id)) return;

        final Hologram hologram = DHAPI.createHologram(name(id), location.clone().add(getVector(crate)));

        crateHologram.getMessages().forEach(line -> {
            if (line != null) {
                String coloredLine1 = color(line);

                if (coloredLine1 != null) {
                    String coloredLine = coloredLine1
                            .replace("<GREEN>", "&a")
                            .replace("<AQUA>", "&b")
                            .replace("<RED>", "&c")
                            .replace("<LIGHT_PURPLE>", "&d")
                            .replace("<YELLOW>", "&e")
                            .replace("<WHITE>", "&f")
                            .replace("<BLACK>", "&0")
                            .replace("<DARK_BLUE>", "&1")
                            .replace("<DARK_GREEN>", "&2")
                            .replace("<DARK_AQUA>", "&3")
                            .replace("<DARK_RED>", "&4")
                            .replace("<DARK_PURPLE>", "&5")
                            .replace("<GOLD>", "&6")
                            .replace("<GRAY>", "&7")
                            .replace("<DARK_GRAY>", "&8")
                            .replace("<DARK_BLUE>", "&9")
                            .replace("<OBFUSCATED>", "&k")
                            .replace("<BOLD>", "&l")
                            .replace("<STRIKETHROUGH>", "&m")
                            .replace("<UNDERLINED>", "&n")
                            .replace("<ITALIC>", "&o")
                            .replace("<RESET>", "&r");

                    DHAPI.addHologramLine(hologram, coloredLine);

                }
            }
        });

        hologram.setDisplayRange(crateHologram.getRange());

        if (crateHologram.getUpdateInterval() != -1) {
            hologram.setUpdateInterval(crateHologram.getUpdateInterval());
        }

        this.holograms.putIfAbsent(name(id), hologram);
    }

    @Override
    public void removeHologram(final String id) {
        DHAPI.removeHologram(name(id));
    }

    @Override
    public boolean exists(final String id) {
        return DHAPI.getHologram(name(id)) != null;
    }

    @Override
    public void purge(final boolean isShutdown) {
        this.holograms.forEach((key, value) -> value.delete());
        this.holograms.clear();
    }
}