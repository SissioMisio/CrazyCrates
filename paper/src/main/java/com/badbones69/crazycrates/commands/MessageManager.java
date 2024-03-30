package com.badbones69.crazycrates.commands;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class MessageManager {

    protected final @NotNull BukkitCommandManager<CommandSender> commandManager = CommandManager.getCommandManager();

    public abstract void build();

    public abstract void send(@NotNull CommandSender sender, @NotNull Component component);

    public abstract Component parse(@NotNull String message);

}