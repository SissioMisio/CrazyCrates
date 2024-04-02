package us.crazycrew.crazycrates;

import us.crazycrew.crazycrates.platform.Server;

/**
 * A class used to initialize the api so other plugins can use it.
 *
 * @author Ryder Belserion
 * @version 0.4
 */
public class CratesProvider {

    private static Server instance;

    public static Server get() {
        if (instance == null) {
            throw new IllegalStateException("CrazyCrates API is not loaded.");
        }

        return instance;
    }

    private CratesProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static void register(Server instance) {
        if (CratesProvider.instance != null) return;

        CratesProvider.instance = instance;
    }

    public static void unregister() {
        CratesProvider.instance = null;
    }
}