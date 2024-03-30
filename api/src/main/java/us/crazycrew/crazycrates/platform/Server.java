package us.crazycrew.crazycrates.platform;

import us.crazycrew.crazycrates.CrazyCratesProvider;
import us.crazycrew.crazycrates.platform.config.ConfigManager;

import java.io.File;
import java.util.logging.Logger;

public abstract class Server {

    public Server(File dataFolder) {
        ConfigManager.load(dataFolder);

        CrazyCratesProvider.register(this);
    }

    public void reload() {
        ConfigManager.reload();
    }

    public void disable() {
        ConfigManager.save();

        CrazyCratesProvider.unregister();
    }

    public abstract Logger getLogger();

    public abstract boolean isLogging();

    public abstract File getFolder();

    public abstract File getKeyFolder();

    public abstract File getCrateFolder();

    public abstract File[] getKeyFiles();

    public abstract File[] getCrateFiles();
}