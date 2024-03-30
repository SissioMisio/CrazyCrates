package com.badbones69.crazycrates.platform;

import com.badbones69.crazycrates.CrazyCratesPaper;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record CustomPlayer(String name) {

    private static final @NotNull CrazyCratesPaper plugin = CrazyCratesPaper.getPlugin(CrazyCratesPaper.class);

    public @NotNull OfflinePlayer getOfflinePlayer() {
        CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> plugin.getServer().getOfflinePlayer(name)).thenApply(OfflinePlayer::getUniqueId);

        return plugin.getServer().getOfflinePlayer(future.join());
    }

    public Player getPlayer() {
        return plugin.getServer().getPlayer(name);
    }
}