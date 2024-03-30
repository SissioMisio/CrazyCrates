package com.badbones69.crazycrates.commands.relations;

import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.commands.MessageManager;
import com.badbones69.crazycrates.platform.utils.MsgUtils;
import com.ryderbelserion.cluster.utils.AdvUtils;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.extention.meta.MetaKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ArgumentRelations extends MessageManager {

    private String getContext(String command, String order) {
        String usage = null;

        switch (command) {
            case "transfer" -> usage = order + "<crate-name> <player-name> <amount>";
            case "debug", "open", "set" -> usage = order + "<crate-name>";
            case "tp" -> usage = order + "<id>";
            case "additem" -> usage = order + "<crate-name> <prize-number> <chance> [tier]";
            case "preview", "forceopen" -> usage = order + "<crate-name> <player-name>";
            case "open-others" -> usage = order + "<crate-name> <player-name> [key-type]";
            case "mass-open" -> usage = order + "<crate-name> <key-type> <amount>";
            case "give-random" -> usage = order + "<key-type> <amount> <player-name>";
            case "give", "take" -> usage = order + "<key-type> <crate-name> <amount> <player-name>";
            case "giveall" -> usage = order + "<key-type> <crate-name> <amount>";
            case "admin" -> usage = order;
        }

        return usage;
    }

    @Override
    public void build() {
        this.commandManager.registerMessage(BukkitMessageKey.UNKNOWN_COMMAND, (sender, context) -> {
            String input = context.getInvalidInput();

            if (sender instanceof Player player) {
                send(player, Messages.unknown_command.getMessage("{command}", input, player));
                return;
            }

            send(sender, Messages.unknown_command.getMessage("{command}", input));
        });

        this.commandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, context) -> {
            Optional<String> meta = context.getMeta().get(MetaKey.NAME);

            meta.ifPresent(key -> {
                if (sender instanceof Player player) {
                    if (key.equalsIgnoreCase("view")) {
                        send(player, Messages.correct_usage.getMessage("{usage}", getContext(key, "/keys " + key), player));

                        return;
                    }

                    send(player, Messages.correct_usage.getMessage("{usage}", getContext(key, "/crazycrates " + key), player));

                    return;
                }

                if (key.equalsIgnoreCase("view")) {
                    send(sender, Messages.correct_usage.getMessage("{usage}", getContext(key, "/keys " + key)));

                    return;
                }

                send(sender, Messages.correct_usage.getMessage("{usage}", getContext(key, "/crazycrates " + key)));
            });
        });

        this.commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, context) -> {
            Optional<String> meta = context.getMeta().get(MetaKey.NAME);

            meta.ifPresent(key -> {
                if (sender instanceof Player player) {
                    if (key.equalsIgnoreCase("view")) {
                        send(player, Messages.correct_usage.getMessage("{usage}", getContext(key, "/keys " + key), player));

                        return;
                    }

                    send(player, Messages.correct_usage.getMessage("{usage}", getContext(key, "/crazycrates " + key), player));

                    return;
                }

                if (key.equalsIgnoreCase("view")) {
                    send(sender, Messages.correct_usage.getMessage("{usage}", getContext(key, "/keys " + key)));

                    return;
                }

                send(sender, Messages.correct_usage.getMessage("{usage}", getContext(key, "/crazycrates " + key)));
            });
        });

        this.commandManager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, context) -> {
            if (sender instanceof Player player) {
                send(sender, Messages.correct_usage.getMessage("{usage}", context.getArgumentName(), player));

                return;
            }

            send(sender, Messages.correct_usage.getMessage("{usage}", context.getArgumentName()));
        });

        this.commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, context) -> {
            if (sender instanceof Player player) {
                send(sender, Messages.no_permission.getMessage("{permission}", context.getPermission().toString(), player));
            } else {
                send(sender, Messages.no_permission.getMessage("{permission}", context.getPermission().toString()));
            }
        });

        this.commandManager.registerMessage(BukkitMessageKey.PLAYER_ONLY, (sender, context) -> {
            if (sender instanceof Player player) {
                send(sender, Messages.must_be_a_player.getMessage(player));
            } else {
                send(sender, Messages.must_be_a_player.getMessage());
            }
        });

        this.commandManager.registerMessage(BukkitMessageKey.CONSOLE_ONLY, (sender, context) -> {
            if (sender instanceof Player player) {
                send(sender, Messages.must_be_console_sender.getMessage(player));
            } else {
                send(sender, Messages.must_be_console_sender.getMessage());
            }
        });
    }

    @Override
    public void send(@NotNull CommandSender sender, @NotNull Component component) {
        sender.sendMessage(component);
    }

    @Override
    public Component parse(@NotNull String message) {
        return AdvUtils.parse(message);
    }
}