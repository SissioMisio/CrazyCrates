package us.crazycrew.crazycrates.api.users;

import net.kyori.adventure.audience.Audience;
import us.crazycrew.crazycrates.api.enums.types.KeyType;

import java.util.UUID;

/**
 * A class that handles fetching users, checking virtual keys, adding virtual keys or physical keys
 * Ability to set keys, get keys, getting total keys or checking total crates opened or individual crates opened.
 *
 * @author Ryder Belserion
 * @version 0.4
 */
public abstract class UserManager {

    /**
     * Checks if user is null.
     *
     * @param uuid the uuid of the player.
     * @return true or false.
     */
    public abstract boolean isUserNull(UUID uuid);

    /**
     * Get the player
     *
     * @param uuid the uuid of the player.
     * @return the player.
     */
    public abstract Audience getUser(UUID uuid);

    /**
     * Get the amount of virtual keys a player has.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @return the amount of virtual keys.
     */
    public abstract int getVirtualKeys(UUID uuid, String keyName);

    /**
     * Get the physical amount of keys a player has.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @return the amount of physical keys.
     */
    public abstract int getPhysicalKeys(UUID uuid, String keyName, boolean loopInventory);

    /**
     * Get the total amount of keys a player has.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @return the total amount of keys a player has.
     */
    public abstract int getTotalKeys(UUID uuid, String keyName);

    /**
     * Give a player keys.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @param isVirtual if the key is virtual or physical.
     * @param amount the amount to give.
     */
    public abstract void addKeys(UUID uuid, String keyName, boolean isVirtual, int amount);

    /**
     * Take keys from a player.
     *
     * @param uuid the uuid of the player.
     * @param crateName the name of the crate.
     * @param keyName the name of the key.
     * @param isVirtual if the key is virtual or physical.
     * @param amount the amount to take.
     */
    public abstract boolean takeKeys(UUID uuid, String crateName, String keyName, boolean isVirtual, int amount);

    /**
     * Take keys from a player.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @param isVirtual if the key is virtual or physical.
     * @param amount the amount to take.
     */
    public abstract boolean takeKeys(UUID uuid, String keyName, boolean isVirtual, int amount);

    /**
     * Give a player virtual keys for a crate.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @param amount the amount to give.
     */
    public abstract void addVirtualKeys(UUID uuid, String keyName, int amount);

    /**
     * Checks if a player has virtual keys.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @return true or false.
     */
    public abstract boolean hasVirtualKeys(UUID uuid, String keyName);

    /**
     * Adds physical keys to a player.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @param amount the amount to give.
     */
    public abstract void addPhysicalKeys(UUID uuid, String keyName, int amount);

    /**
     * Set the amount of virtual keys a player has.
     *
     * @param uuid the uuid of the player.
     * @param keyName the name of the key.
     * @param amount the amount to give.
     */
    public abstract void setKeys(UUID uuid, String keyName, int amount);
}