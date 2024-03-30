package com.badbones69.crazycrates.platform.crates;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.google.common.base.Preconditions;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import us.crazycrew.crazycrates.CrazyCratesProvider;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class CrateManager {

    private final Set<String> brokenCrates = new HashSet<>();
    private final Set<Crate> crates = new HashSet<>();

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull Server instance = this.plugin.getInstance();

    public void load() {
        // Notify if the directory is not readable.
        Preconditions.checkNotNull(this.instance.getCrateFiles(), "Could not read from the crates directory! " + this.instance.getCrateFolder().getAbsolutePath());

        // Clear the previous crates.
        this.crates.clear();

        for (File file : this.instance.getCrateFiles()) {
            if (MiscUtils.isLogging()) this.plugin.getLogger().info("Loading crate: " + file.getName());

            CrateConfig crateConfig = new CrateConfig(file);

            try {
                crateConfig.load();

                ConfigurationSection section = crateConfig.getCrateSection().getConfigurationSection("PhysicalKey");

                if (section != null) {
                    File keyFile = new File(CrazyCratesProvider.get().getKeyFolder() + "/" + crateConfig.getName() + ".yml");

                    keyFile.createNewFile();

                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(keyFile);

                    configuration.set("options.virtual", true);

                    configuration.set("item.name", section.getString("Name"));
                    configuration.set("item.lore", section.getStringList("Lore"));
                    configuration.set("item.material", section.getString("Item"));
                    configuration.set("item.glowing", section.getBoolean("Glowing"));

                    configuration.set("item.flags", Collections.emptyList());
                    configuration.set("item.unbreakable", false);

                    configuration.save(keyFile);

                    crateConfig.getFile().set("Crate.PhysicalKey", null);

                    crateConfig.getFile().set("Crate.keys", new ArrayList<>() {{
                        add(keyFile.getName().replaceAll(".yml", ""));
                    }});

                    crateConfig.getFile().save();
                    crateConfig.getFile().loadWithComments();
                }
            } catch (InvalidConfigurationException exception) {
                this.plugin.getLogger().log(Level.WARNING, file.getName() + " contains invalid YAML structure.", exception);

                this.brokenCrates.add(file.getName());

                continue;
            } catch (IOException exception) {
                this.plugin.getLogger().log(Level.WARNING, "Could not load crate file: " + file.getName(), exception);

                this.brokenCrates.add(file.getName());

                continue;
            }

            this.crates.add(new Crate(crateConfig));
        }
    }

    public Crate getCrate(String name) {
        Crate crate = null;

        for (Crate key : this.crates) {
            if (!key.getName().equalsIgnoreCase(name)) continue;

            crate = key;

            break;
        }

        return crate;
    }

    public Set<Crate> getCrates() {
        return Collections.unmodifiableSet(this.crates);
    }

    public Set<String> getBrokenCrates() {
        return Collections.unmodifiableSet(this.brokenCrates);
    }
}