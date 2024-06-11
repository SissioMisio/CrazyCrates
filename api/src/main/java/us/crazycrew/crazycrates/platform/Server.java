package us.crazycrew.crazycrates.platform;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.core.config.YamlManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.CratesProvider;
import us.crazycrew.crazycrates.api.CrazyCratesService;
import us.crazycrew.crazycrates.api.ICrazyCrates;
import us.crazycrew.crazycrates.api.users.UserManager;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import java.io.File;
import java.util.logging.Logger;

public class Server extends Vital implements ICrazyCrates {

    private YamlManager fileManager;
    private final JavaPlugin plugin;
    private final File crateFolder;

    @ApiStatus.Internal
    public Server(JavaPlugin plugin) {
        this.plugin = plugin;

        this.plugin.getDataFolder().mkdirs();

        this.crateFolder = new File(this.plugin.getDataFolder(), "crates");
    }

    private boolean isLogging = false;

    @ApiStatus.Internal
    public void enable() {
        ConfigManager.load(this.plugin.getDataFolder());

        this.isLogging = ConfigManager.getConfig().getProperty(ConfigKeys.verbose_logging);

        this.fileManager = new YamlManager();
        this.fileManager.addFile("locations.yml").addFile("data.yml")
                .addFolder("crates")
                .addFolder("schematics")
                .init();

        // Register legacy provider.
        CrazyCratesService.register(this);

        // Register default provider.
        CratesProvider.register(this);
    }

    public void reload() {
        ConfigManager.reload();
    }

    @ApiStatus.Internal
    public void disable() {
        // Unregister legacy provider.
        CrazyCratesService.unregister();

        // Unregister default provider.
        CratesProvider.unregister();
    }

    @Override
    public @NotNull File getDirectory() {
        return this.plugin.getDataFolder();
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public boolean isLogging() {
        return this.isLogging;
    }

    public @NotNull YamlManager getFileManager() {
        return this.fileManager;
    }

    public File getCrateFolder() {
        return this.crateFolder;
    }

    public @NotNull File[] getCrateFiles() {
        return this.crateFolder.listFiles((dir, name) -> name.endsWith(".yml"));
    }

    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public @NotNull UserManager getUserManager() {
        return this.userManager;
    }
}