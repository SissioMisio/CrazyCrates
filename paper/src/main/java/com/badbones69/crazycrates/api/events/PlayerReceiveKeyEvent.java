package com.badbones69.crazycrates.api.events;

import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerReceiveKeyEvent extends Event implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final Crate crate;
    private final Key key;
    private final KeyReceiveReason reason;
    private final int amount;
    private boolean isCancelled;

    @Deprecated(forRemoval = true, since = "2.0")
    public PlayerReceiveKeyEvent(Player player, Crate crate, KeyReceiveReason reason, int amount) {
        this.player = player;
        this.crate = crate;
        this.reason = reason;
        this.amount = amount;
        this.isCancelled = false;

        this.key = null;
    }

    public PlayerReceiveKeyEvent(Player player, Key key, KeyReceiveReason reason, int amount) {
        this.player = player;
        this.key = key;
        this.reason = reason;
        this.amount = amount;
        this.isCancelled = false;

        this.crate = null;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public Player getPlayer() {
        return this.player;
    }

    @Deprecated(forRemoval = true, since = "2.0")
    public Crate getCrate() {
        return this.crate;
    }

    public Key getKey() {
        return this.key;
    }

    public KeyReceiveReason getReason() {
        return this.reason;
    }
    
    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }
    
    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
    
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public enum KeyReceiveReason {
        // Received a key from the /cc give command.
        GIVE_COMMAND,
        // Received a key from the /cc giveall command.
        GIVE_ALL_COMMAND,
        // Received when player has come online after being given a key while offline.
        OFFLINE_PLAYER,
        // Received a key as a refund from a crate that failed.
        REFUND,
        // Received a key from the /cc transfer command.
        TRANSFER
    }
}