package us.crazycrew.crazycrates.platform.keys;

import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class KeyConfig extends YamlConfiguration {

    private final File file;

    /**
     * Creates a config object.
     *
     * @param file the file to bind
     */
    public KeyConfig(File file) {
        this.file = file;
    }

    /**
     * Loads the key config file.
     *
     * @throws IOException if the file fails to load
     */
    public void load() throws IOException {
        load(this.file);
    }

    /**
     * @return true or false.
     */
    public boolean isVirtual() {
        return getBoolean("options.virtual", true);
    }

    /**
     * @return the name of the file without .yml
     */
    public String getKeyName() {
        return getFile().getName().replaceAll(".yml", "");
    }

    /**
     * @return the file object.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * @return the material the item will be.
     */
    public String getMaterial() {
        return getString("item.material", "tripwire_hook");
    }

    /**
     * @return the name of the item.
     */
    public String getItemName() {
        return getString("item.name", "");
    }

    /**
     * @return the lore of the item.
     */
    public List<String> getLore() {
        return getStringList("item.lore");
    }

    /**
     * @return the list of item flags to apply on the item.
     */
    public List<String> getItemFlags() {
        return getStringList("item.flags");
    }

    /**
     * @return true or false
     */
    public boolean isUnbreakable() {
        return getBoolean("item.unbreakable", false);
    }

    /**
     * @return true or false
     */
    public boolean isGlowing() {
        return getBoolean("item.glowing", true);
    }
}