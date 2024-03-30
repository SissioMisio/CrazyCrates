package com.badbones69.crazycrates.api.builders.types;

import com.badbones69.crazycrates.api.objects.Crate;
import org.bukkit.entity.Player;
import com.badbones69.crazycrates.api.builders.InventoryBuilder;

public class CratePrizeMenu extends InventoryBuilder {

    public CratePrizeMenu(String title, int rows, Player player, Crate crate) {
        super(title, rows, player, crate);
    }

    @Override
    public InventoryBuilder build() {
        return this;
    }
}