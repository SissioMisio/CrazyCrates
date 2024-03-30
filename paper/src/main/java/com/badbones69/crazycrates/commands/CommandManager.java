package com.badbones69.crazycrates.commands;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.commands.crates.types.admin.CommandAdmin;
import com.badbones69.crazycrates.commands.crates.types.admin.keys.CommandGive;
import com.badbones69.crazycrates.commands.relations.ArgumentRelations;
import com.badbones69.crazycrates.platform.CustomPlayer;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandManager {

    private final static @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final static @NotNull BukkitCommandManager<CommandSender> commandManager = BukkitCommandManager.create(plugin);

    public static void load() {
        new ArgumentRelations().build();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

        getCommandManager().registerSuggestion(SuggestionKey.of("players"), (sender, context) -> players.stream().map(Player::getName).toList());

        getCommandManager().registerSuggestion(SuggestionKey.of("numbers"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            for (int i = 1; i <= 64; i++) numbers.add(String.valueOf(i));

            return numbers;
        });

        getCommandManager().registerSuggestion(SuggestionKey.of("key-types"), (sender, arguments) -> List.of("virtual", "v", "physical", "p"));

        //getCommandManager().registerSuggestion(SuggestionKey.of("locations"), (sender, arguments) -> BUKKIT_CRATE_MANAGER.getCrateLocations().stream().map(CrateLocation::getID).toList());

        //getCommandManager().registerSuggestion(SuggestionKey.of("crates"), (sender, arguments) -> FileUtils.getFiles("crates"));

        getCommandManager().registerSuggestion(SuggestionKey.of("keys"), (sender, arguments) -> plugin.getKeyManager().getKeys().stream().map(Key::getKeyName).toList());

        getCommandManager().registerArgument(CustomPlayer.class, (sender, arguments) -> new CustomPlayer(arguments));

        List.of(
                new CommandAdmin(),
                new CommandGive()
        ).forEach(getCommandManager()::registerCommand);
    }

    public static @NotNull BukkitCommandManager<CommandSender> getCommandManager() {
        return commandManager;
    }
}