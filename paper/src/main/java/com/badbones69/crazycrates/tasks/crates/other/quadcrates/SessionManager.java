package com.badbones69.crazycrates.tasks.crates.other.quadcrates;

import org.bukkit.entity.Player;
import java.util.List;

public class SessionManager {

    /**
     * Check if player is in session.
     *
     * @param player player to check.
     * @return true or false.
     */
    public boolean inSession(Player player) {
        List<QuadCrateManager> sessions = QuadCrateManager.getCrateSessions();

        if (sessions.isEmpty()) return false;

        for (QuadCrateManager quadCrateManager : sessions) {
            if (quadCrateManager.getPlayer().getUniqueId() == player.getUniqueId()) return true;
        }

        return false;
    }

    /**
     * Get an ongoing session.
     *
     * @param player player to check.
     * @return crate session or null.
     */
    public QuadCrateManager getSession(Player player) {
        List<QuadCrateManager> sessions = QuadCrateManager.getCrateSessions();

        for (QuadCrateManager quadCrateManager : sessions) {
            if (quadCrateManager.getPlayer().getUniqueId() == player.getUniqueId()) return quadCrateManager;
        }

        return null;
    }

    /**
     * End all crates.
     */
    public static void endCrates() {
        List<QuadCrateManager> sessions = QuadCrateManager.getCrateSessions();

        if (!sessions.isEmpty()) {
            sessions.forEach(session -> session.endCrateForce(false));
            sessions.clear();
        }
    }
}