package us.crazycrew.crazycrates;

import org.jetbrains.annotations.ApiStatus;
import us.crazycrew.crazycrates.platform.Server;

/**
 * A class used to initialize the api so other plugins can use it.
 *
 * @author Ryder Belserion
 * @version 0.4
 */
public class CrazyCratesProvider {

    private static Server instance;

    public static Server get() {
        if (instance == null) {
            throw new IllegalStateException("CrazyCrates is not loaded.");
        }

        return instance;
    }

    @ApiStatus.Internal
    private CrazyCratesProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    @ApiStatus.Internal
    public static void register(Server instance) {
        if (CrazyCratesProvider.instance != null) {
            instance.getLogger().warning("CrazyCrates is already enabled.");

            return;
        }

        CrazyCratesProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        CrazyCratesProvider.instance = null;
    }
}