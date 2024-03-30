package com.badbones69.crazycrates.tasks.crates;

/*
public class CrateManager {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull KeyManager keyManager = this.plugin.getKeyManager();

    private final @NotNull Server instance = this.plugin.getInstance();

    private final Set<Crate> crates = new HashSet<>();
    private final Set<String> brokenCrates = new HashSet<>();
    private final Map<UUID, Location> cratesInUse = new HashMap<>();
    private final Map<UUID, TimerTask> timerTasks = new HashMap<>();
    private final Set<CrateLocation> crateLocations = new HashSet<>();
    private final Set<BrokeLocation> brokenLocations = new HashSet<>();
    private final Map<UUID, BukkitTask> currentTasks = new HashMap<>();
    private final List<CrateSchematic> crateSchematics = new ArrayList<>();
    private final Map<UUID, Set<BukkitTask>> currentQuadTasks = new HashMap<>();

    private HologramManager hologramManager;

    /**
     * Create hologram manager.

    public void loadHolograms() {
        if (PluginSupport.DECENT_HOLOGRAMS.isPluginEnabled()) {
            this.hologramManager = new DecentHologramsSupport();
            if (MiscUtils.isLogging()) this.plugin.getLogger().info("DecentHolograms support has been enabled.");
        } else if (PluginSupport.CMI.isPluginEnabled() && CMIModule.holograms.isEnabled()) {
            this.hologramManager = new CMIHologramsSupport();
            if (MiscUtils.isLogging()) this.plugin.getLogger().info("CMI Hologram support has been enabled.");
        } else if (PluginSupport.HOLOGRAPHIC_DISPLAYS.isPluginEnabled()) {
            this.hologramManager = new HolographicDisplaysSupport();
            if (MiscUtils.isLogging()) this.plugin.getLogger().info("Holographic Displays support has been enabled.");
        } else if (MiscUtils.isLogging()) this.plugin.getLogger().warning("No holograms plugin were found. If using CMI, make sure holograms module is enabled.");
    }

    /**
     * @return the hologram manager.

    public HologramManager getHologramManager() {
        return this.hologramManager;
    }

    /**
     * Load all crate information.

    public void load() {
        purge();

        if (this.hologramManager != null && !this.hologramManager.isMapEmpty()) {
            this.hologramManager.removeAllHolograms();
        }

        File[] crateFilesList = this.instance.getCrateFiles();

        if (crateFilesList == null) {
            this.plugin.getLogger().severe("Could not read from crates directory! " + this.instance.getCrateFolder().getAbsolutePath());

            return;
        }

        for (File file : crateFilesList) {
            this.plugin.getLogger().info("Loading crate: " + file.getName());

            CrateConfig crateConfig = new CrateConfig(file);

            try {
                crateConfig.load();

                ConfigurationSection section = crateConfig.getCrateSection().getConfigurationSection("PhysicalKey");

                if (section != null) {
                    File keyFile = new File(CrazyCratesProvider.get().getKeyFolder() + "/" + crateConfig.getName() + ".yml");

                    keyFile.createNewFile();

                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(keyFile);

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
                this.brokenCrates.add(file.getName());
                this.plugin.getLogger().log(Level.WARNING, file.getName() + " contains invalid YAML structure.", exception);
                continue;
            } catch (IOException exception) {
                this.brokenCrates.add(file.getName());
                this.plugin.getLogger().log(Level.WARNING, "Could not load crate file: " + file.getName(), exception);
                continue;
            }

            Crate crate = new Crate(crateConfig);

            this.crates.add(crate);
        }

        if (MiscUtils.isLogging()) {
            List.of(
                    "All crate information has been loaded.",
                    "Loading all the physical crate locations."
            ).forEach(line -> this.plugin.getLogger().info(line));
        }

        FileConfiguration locations = Files.LOCATIONS.getFile();
        int loadedAmount = 0;
        int brokeAmount = 0;

        org.bukkit.configuration.ConfigurationSection section = locations.getConfigurationSection("Locations");

        if (section != null) {
            for (String locationName : section.getKeys(false)) {
                try {
                    String worldName = locations.getString("Locations." + locationName + ".World");

                    // If name is null, we return.
                    if (worldName == null) return;

                    // If name is empty or blank, we return.
                    if (worldName.isEmpty() || worldName.isBlank()) return;

                    World world = this.plugin.getServer().getWorld(worldName);
                    int x = locations.getInt("Locations." + locationName + ".X");
                    int y = locations.getInt("Locations." + locationName + ".Y");
                    int z = locations.getInt("Locations." + locationName + ".Z");
                    Location location = new Location(world, x, y, z);

                    Crate crate = getCrate(locations.getString("Locations." + locationName + ".Crate"));

                    if (world != null && crate != null) {
                        this.crateLocations.add(new CrateLocation(locationName, crate, location));

                        if (this.hologramManager != null) {
                            this.hologramManager.createHologram(location.getBlock(), crate);
                        }

                        loadedAmount++;
                    } else {
                        this.brokenLocations.add(new BrokeLocation(locationName, crate, x, y, z, worldName));
                        brokeAmount++;
                    }

                } catch (Exception ignored) {}
            }
        }

        // Checking if all physical locations loaded
        if (MiscUtils.isLogging()) {
            if (loadedAmount > 0 || brokeAmount > 0) {
                if (brokeAmount <= 0) {
                    this.plugin.getLogger().info("All physical crate locations have been loaded.");
                } else {
                    this.plugin.getLogger().info("Loaded " + loadedAmount + " physical crate locations.");
                    this.plugin.getLogger().info("Failed to load " + brokeAmount + " physical crate locations.");
                }
            }

            this.plugin.getLogger().info("Searching for schematics to load.");
        }

        // Loading schematic files
        String[] schems = new File(this.plugin.getDataFolder() + "/schematics/").list();

        if (schems != null) {
            for (String schematicName : schems) {
                if (schematicName.endsWith(".nbt")) {
                    this.crateSchematics.add(new CrateSchematic(schematicName, new File(plugin.getDataFolder() + "/schematics/" + schematicName)));

                    if (MiscUtils.isLogging()) this.plugin.getLogger().info(schematicName + " was successfully found and loaded.");
                }
            }
        }

        if (MiscUtils.isLogging()) this.plugin.getLogger().info("All schematics were found and loaded.");

        cleanDataFile();

        this.plugin.getInventoryManager().loadButtons();
    }

    /**
     * Opens a crate for a player.
     *
     * @param player the player that is having the crate opened for them.
     * @param crate the crate that is being used.
     * @param key the key to check.
     * @param location  the location that may be needed for some crate types.
     * @param checkHand if it just checks the players hand or if it checks their inventory.

    public void openCrate(Player player, Crate crate, Key key, KeyType keyType, Location location, boolean virtualCrate, boolean checkHand) {
        SettingsManager config = ConfigManager.getConfig();

        if (crate.getCrateType() == CrateType.menu) {
            if (config.getProperty(ConfigKeys.enable_crate_menu)) {
                CrateMainMenu crateMainMenu = new CrateMainMenu(player, config.getProperty(ConfigKeys.inventory_size), config.getProperty(ConfigKeys.inventory_name));

                player.openInventory(crateMainMenu.build().getInventory());

                return;
            }

            player.sendMessage(Messages.feature_disabled.getMessage(player));

            return;
        }

        CrateBuilder crateBuilder;

        switch (crate.getCrateType()) {
            case csgo -> crateBuilder = new CsgoCrate(key, crate, player, 27);
            case casino -> crateBuilder = new CasinoCrate(key, crate, player, 27);
            case wonder -> crateBuilder = new WonderCrate(key, crate, player, 45);
            case wheel -> crateBuilder = new WheelCrate(key, crate, player, 54);
            case roulette -> crateBuilder = new RouletteCrate(key, crate, player, 45);
            case war -> crateBuilder = new WarCrate(key, crate, player, 9);
            case quad_crate -> {
                if (virtualCrate) {
                    Map<String, String> placeholders = new HashMap<>();

                    placeholders.put("{cratetype}", crate.getCrateType().getName());
                    placeholders.put("{crate}", crate.getName());

                    player.sendMessage(Messages.cant_be_a_virtual_crate.getMessage(placeholders, player));

                    removeActiveCrate(player);

                    return;
                }

                crateBuilder = new QuadCrate(key, crate, player, location);
            }

            case fire_cracker -> {
                if (containsActiveLocation(location)) {
                    player.sendMessage(Messages.crate_in_use.getMessage("{crate}", crate.getName(), player));

                    removeActiveCrate(player);

                    return;
                }

                if (virtualCrate) {
                    Map<String, String> placeholders = new HashMap<>();

                    placeholders.put("{cratetype}", crate.getCrateType().getName());
                    placeholders.put("{crate}", crate.getName());

                    player.sendMessage(Messages.cant_be_a_virtual_crate.getMessage(placeholders, player));

                    removeActiveCrate(player);

                    return;
                }

                crateBuilder = new FireCrackerCrate(key, crate, player, 45, location);
            }

            case crate_on_the_go -> {
                if (virtualCrate) {
                    Map<String, String> placeholders = new HashMap<>();

                    placeholders.put("{cratetype}", crate.getCrateType().getName());
                    placeholders.put("{crate}", crate.getName());

                    player.sendMessage(Messages.cant_be_a_virtual_crate.getMessage(placeholders, player));

                    removeActiveCrate(player);

                    return;
                }

                crateBuilder = new CrateOnTheGo(key, crate, player);
            }

            case quick_crate -> {
                if (containsActiveLocation(location)) {
                    player.sendMessage(Messages.crate_in_use.getMessage("{crate}", crate.getName(), player));

                    removeActiveCrate(player);

                    return;
                }

                if (virtualCrate) {
                    Map<String, String> placeholders = new HashMap<>();

                    placeholders.put("{cratetype}", crate.getCrateType().getName());
                    placeholders.put("{crate}", crate.getName());

                    player.sendMessage(Messages.cant_be_a_virtual_crate.getMessage(placeholders, player));

                    removeActiveCrate(player);

                    return;
                }

                crateBuilder = new QuickCrate(key, crate, player, location);
            }

            default -> {
                crateBuilder = new CsgoCrate(key, crate, player, 27);

                if (MiscUtils.isLogging()) {
                    List.of(
                            crate.getName() + " has an invalid crate type. Your Value: " + crate.getSection().getString("CrateType"),
                            "We will use " + CrateType.csgo.getName() + " until you change the crate type.",
                            "Valid Crate Types: CSGO/Casino/Cosmic/QuadCrate/QuickCrate/Roulette/CrateOnTheGo/FireCracker/Wonder/Wheel/War"
                    ).forEach(line -> this.plugin.getLogger().warning(line));
                }
            }
        }

        // Open the crate.
        crateBuilder.open(keyType, checkHand);
    }

    /**
     * Clean data files of old fields.

    private void cleanDataFile() {
        FileConfiguration data = Files.DATA.getFile();

        if (!data.contains("Players")) return;

        if (MiscUtils.isLogging()) this.plugin.getLogger().info("Cleaning up the data.yml file.");

        List<String> removePlayers = new ArrayList<>();

        for (String uuid : data.getConfigurationSection("Players").getKeys(false)) {
            if (data.contains("Players." + uuid + ".tracking")) return;

            boolean hasKeys = false;
            List<String> noKeys = new ArrayList<>();

            for (Key key : this.keyManager.getKeys()) {
                if (data.getInt("Players." + uuid + "." + key.getName()) <= 0) {
                    noKeys.add(key.getName());
                } else {
                    hasKeys = true;
                }
            }

            if (hasKeys) {
                noKeys.forEach(crate -> data.set("Players." + uuid + "." + crate, null));
            } else {
                removePlayers.add(uuid);
            }
        }

        if (!removePlayers.isEmpty()) {
            if (MiscUtils.isLogging()) this.plugin.getLogger().info(removePlayers.size() + " player's data has been marked to be removed.");

            removePlayers.forEach(uuid -> data.set("Players." + uuid, null));

            if (MiscUtils.isLogging()) this.plugin.getLogger().info("All empty player data has been removed.");
        }

        if (MiscUtils.isLogging()) this.plugin.getLogger().info("The data.yml file has been cleaned.");

        Files.DATA.saveFile();
    }

    /**
     * Set a new player's default amount of keys.
     *
     * @param player the player that has just joined.

    public void setNewPlayerKeys(Player player) {
        String uuid = player.getUniqueId().toString();

        if (!player.hasPlayedBefore()) {
            this.crates.stream()
                    .filter(Crate::isStartingKeys)
                    .forEach(crate -> {
                        Files.DATA.getFile().set("Players." + uuid + "." + crate.getName(), crate.getStartingKeys());

                        Files.DATA.saveFile();
                    });
        }
    }

    /**
     * Get crate by location.
     *
     * @param location the location to check.
     * @return crate object or null if nothing found.

    public Crate getCrate(Location location) {
        Crate crate = null;

        for (CrateLocation key : this.crateLocations) {
            if (!key.getLocation().equals(location)) continue;

            crate = key.getCrate();

            break;
        }

        return crate;
    }

    /**
     * Get a schematic based on its name.
     *
     * @param name the name of the schematic.
     * @return the crate schematic object or null.

    public CrateSchematic getCrateSchematic(String name) {
        CrateSchematic schematic = null;

        for (CrateSchematic key : this.crateSchematics) {
            if (!key.getSchematicName().equalsIgnoreCase(name)) continue;

            schematic = key;

            break;
        }

        return schematic;
    }

    /**
     * Gets the physical crate of the location.
     *
     * @param location location you are checking.
     * @return a crate location if the location is a physical crate otherwise null if not.

    public CrateLocation getCrateLocation(Location location) {
        CrateLocation crateLocation = null;

        for (CrateLocation key : this.crateLocations) {
            if (!isCrateLocation(location)) continue;

            crateLocation = key;

            break;
        }

        return crateLocation;
    }

    /**
     * Add a new physical crate location.
     *
     * @param location the location you wish to add.
     * @param crate the crate which you would like to set it to.

    public void addCrateLocation(Location location, Crate crate) {
        FileConfiguration locations = Files.LOCATIONS.getFile();

        String id = "1"; // Location ID

        for (int i = 1; locations.contains("Locations." + i); i++) {
            id = (i + 1) + "";
        }

        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getLocation().equals(location)) {
                id = crateLocation.getID();

                break;
            }
        }

        locations.set("Locations." + id + ".Crate", crate.getName());
        locations.set("Locations." + id + ".World", location.getWorld().getName());
        locations.set("Locations." + id + ".X", location.getBlockX());
        locations.set("Locations." + id + ".Y", location.getBlockY());
        locations.set("Locations." + id + ".Z", location.getBlockZ());

        Files.LOCATIONS.saveFile();

        addLocation(new CrateLocation(id, crate, location));

        if (this.hologramManager != null) this.hologramManager.createHologram(location.getBlock(), crate);
    }

    /**
     * Checks to see if the location is a physical crate.
     *
     * @param location location you are checking.
     * @return true if it is a physical crate and false if not.

    public boolean isCrateLocation(Location location) {
        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getLocation().equals(location)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Remove a physical crate location.
     *
     * @param id the id of the location.

    public void removeCrateLocation(String id) {
        Files.LOCATIONS.getFile().set("Locations." + id, null);

        Files.LOCATIONS.saveFile();

        CrateLocation location = null;

        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getID().equalsIgnoreCase(id)) {
                location = crateLocation;

                break;
            }
        }

        if (location != null) {
            removeLocation(location);

            if (this.hologramManager != null) this.hologramManager.removeHologram(location.getLocation().getBlock());
        }
    }

    /**
     * @param keyName the key name to check.
     * @return the crate object.

    public Crate getCrateFromKey(String keyName) {
        // If it's null/empty, return.
        if (keyName == null || keyName.isEmpty()) return null;

        // Get crate.
        Crate crate = null;

        for (Crate key : getCrates()) {
            if (!key.getKeys().contains(keyName)) continue;

            crate = key;

            break;
        }

        return crate;
    }

    /**
     * Check if an entity is a display reward for a crate.
     *
     * @param entity entity you wish to check.
     * @return true if it is a display reward item and false if not.

    public boolean isDisplayReward(Entity entity) {
        if (entity instanceof Item item) {
            ItemStack itemStack = item.getItemStack();

            if (itemStack.getType() == Material.AIR) return false;

            ItemMeta itemMeta = itemStack.getItemMeta();

            PersistentKeys prize = PersistentKeys.crate_prize;

            PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            return container.has(prize.getNamespacedKey());
        }

        return false;
    }

    /**
     * Nukes all data.

    public void purge() {
        this.crates.clear();
        this.crateLocations.clear();
        this.crateSchematics.clear();
        this.brokenLocations.clear();
    }

    /**
     * @return a set of loaded crates.

    public Set<Crate> getCrates() {
        return Collections.unmodifiableSet(this.crates);
    }

    /**
     * @return a list of loaded crate schematics.

    public List<CrateSchematic> getCrateSchematics() {
        return Collections.unmodifiableList(this.crateSchematics);
    }

    /**
     * @return a set of broken crates.

    public Set<String> getBrokenCrates() {
        return Collections.unmodifiableSet(this.brokenCrates);
    }

    /**
     * @return a set of broken locations.

    public Set<BrokeLocation> getBrokenLocations() {
        return Collections.unmodifiableSet(this.brokenLocations);
    }

    /**
     * Removes broken locations.
     *
     * @param brokeLocation set of locations to remove.

    public void removeBrokeLocation(Set<BrokeLocation> brokeLocation) {
        this.brokenLocations.removeAll(brokeLocation);
    }

    /**
     * @return a set of crate locations.

    public Set<CrateLocation> getCrateLocations() {
        return Collections.unmodifiableSet(this.crateLocations);
    }

    /**
     * Adds a crate location.
     *
     * @param crateLocation the location to add.

    public void addLocation(CrateLocation crateLocation) {
        this.crateLocations.add(crateLocation);
    }

    /**
     * Removes a crate location.
     *
     * @param crateLocation the location to remove.

    public void removeLocation(CrateLocation crateLocation) {
        this.crateLocations.remove(crateLocation);
    }

    /**
     * @return a map of crates currently in use.

    public Map<UUID, Location> getActiveCrates() {
        return Collections.unmodifiableMap(this.cratesInUse);
    }

    /**
     * @param player the uuid of the player.
     * @return true or false.

    public boolean containsActiveCrate(Player player) {
        return this.cratesInUse.containsKey(player.getUniqueId());
    }

    /**
     * @param location the location to check.
     * @return true or false.

    public boolean containsActiveLocation(Location location) {
        return this.cratesInUse.containsValue(location);
    }

    /**
     * Adds a player opening the crate so that it will be currently in use.
     *
     * @param player the uuid of the player.
     * @param location the location of the crate.

    public void addActiveCrate(Player player, Location location) {
        this.cratesInUse.put(player.getUniqueId(), location);
    }

    /**
     * Checks if a player is currently opening a crate.
     *
     * @param player the uuid of the player opening the crate.
     * @return true or false

    public boolean isCrateActive(Player player) {
        return this.cratesInUse.containsKey(player.getUniqueId());
    }

    // The crate that the player is opening.
    private final Map<UUID, Crate> playerOpeningCrates = new HashMap<>();

    /**
     * Add a player to the list of players that are currently opening crates.
     *
     * @param player the player that is opening a crate.
     * @param crate the crate the player is opening.

    public void addPlayerToOpeningList(Player player, Crate crate) {
        this.playerOpeningCrates.put(player.getUniqueId(), crate);
    }

    /**
     * Remove a player from the list of players that are opening crates.
     *
     * @param player the player that has finished opening a crate.

    public void removePlayerFromOpeningList(Player player) {
        this.playerOpeningCrates.remove(player.getUniqueId());
    }

    /**
     * Check if a player is opening a crate.
     *
     * @param player the player you are checking.
     * @return true if they are opening a crate and false if they are not.

    public boolean isInOpeningList(Player player) {
        return this.playerOpeningCrates.containsKey(player.getUniqueId());
    }

    /**
     * Get the crate the player is currently opening.
     *
     * @param player the player you want to check.
     * @return the Crate of which the player is opening. May return null if no crate found.

    public Crate getOpeningCrate(Player player) {
        return this.playerOpeningCrates.get(player.getUniqueId());
    }


    /**
     * Removes a crate from the active crates.
     *
     * @param player the uuid of the player opening the crate.

    public void removeActiveCrate(Player player) {
        this.cratesInUse.remove(player.getUniqueId());
    }

    /**
     * Adds a repeating timer task for a player opening a crate.
     *
     * @param player player of the player opening the crate.
     * @param task task of the crate.
     * @param delay delay before running the task.
     * @param period interval between task runs.

    public void addRepeatingCrateTask(Player player, TimerTask task, Long delay, Long period) {
        this.timerTasks.put(player.getUniqueId(), task);

        this.plugin.getTimer().scheduleAtFixedRate(task, delay, period);
    }

    /**
     * This forces a crate to end and will not give out a prize. This is meant for people who leave the server to stop any errors or lag from happening.
     *
     * @param player player of the player who the crate being ended for.

    public void removeCrateTask(Player player) {
        // Check if contains.
        if (this.timerTasks.containsKey(player.getUniqueId())) {
            // Cancel the task.
            this.timerTasks.get(player.getUniqueId()).cancel();

            // Remove the player.
            this.timerTasks.remove(player.getUniqueId());
        }
    }

    /**
     * Adds a timer task for a player opening a crate.
     *
     * @param player player of the player  opening the crate.
     * @param task task of the crate.
     * @param delay delay before running the task.

    public void addCrateTask(Player player, TimerTask task, Long delay) {
        this.timerTasks.put(player.getUniqueId(), task);

        this.plugin.getTimer().schedule(task, delay);
    }

    /**
     * Add a crate task that is going on for a player.
     *
     * @param player player of the player opening the crate.
     * @param task task of the crate.

    public void addActiveTask(Player player, BukkitTask task) {
        this.currentTasks.putIfAbsent(player.getUniqueId(), task);
    }

    /**
     * Checks to see if the player has a crate task going on.
     *
     * @param player player of the player that is being checked.
     * @return true if they do have a task and false if not.

    public boolean hasActiveTask(Player player) {
        return this.currentTasks.containsKey(player.getUniqueId());
    }

    /**
     * Gets a crate task that is on going for a player.
     *
     * @param player player of the player opening the crate.
     * @return the task of the crate.

    public BukkitTask getCrateTask(Player player) {
        return this.currentTasks.get(player.getUniqueId());
    }

    /**
     * This forces a crate to end and will not give out a prize. This is meant for people who leave the server to stop any errors or lag from happening.
     *
     * @param player player of the player that the crate is being ended for.

    public void endActiveTask(Player player) {
        if (hasActiveTask(player)) {
            getActiveTask(player).cancel();

            this.currentTasks.remove(player.getUniqueId());
        }
    }

    /**
     * Gets a crate task that is on going for a player.
     *
     * @param player player of the player opening the crate.
     * @return the task of the crate.

    public BukkitTask getActiveTask(Player player) {
        return this.currentTasks.get(player.getUniqueId());
    }

    /**
     * Add a quad crate task that is going on for a player.
     *
     * @param player player of the player opening the crate.
     * @param task the task of the quad crate.

    public void addActiveQuadTask(Player player, BukkitTask task) {
        if (!hasActiveQuadTask(player)) {
            this.currentQuadTasks.putIfAbsent(player.getUniqueId(), new HashSet<>());
        }

        this.currentQuadTasks.get(player.getUniqueId()).add(task);
    }

    /**
     * Checks to see if the player has a quad crate task going on.
     *
     * @param player player of the player that is being checked.
     * @return true if they do have a task and false if not.

    public boolean hasActiveQuadTask(Player player) {
        return this.currentQuadTasks.containsKey(player.getUniqueId());
    }

    /**
     * Ends the tasks running by a player.
     *
     * @param player player of the player using the crate.

    public void endActiveQuadTask(Player player) {
        if (hasActiveQuadTask(player)) {
            getActiveQuadTask(player).forEach(BukkitTask::cancel);

            this.currentQuadTasks.remove(player.getUniqueId());
        }
    }

    /**
     * @param player player of the player to check.
     * @return a set of quad crate tasks.

    public Set<BukkitTask> getActiveQuadTask(Player player) {
        return this.currentQuadTasks.get(player.getUniqueId());
    }

    // QuickCrate/FireCracker
    private final Set<Entity> allRewards = new HashSet<>();
    private final Map<UUID, Entity> rewards = new HashMap<>();

    public void addReward(Player player, Entity entity) {
        this.allRewards.add(entity);

        this.rewards.put(player.getUniqueId(), entity);
    }

    public void endQuickCrate(Player player, Location location, Crate crate, boolean useQuickCrateAgain) {
        if (hasActiveTask(player)) {
            endActiveTask(player);
            removeCrateTask(player);
        }

        if (this.rewards.get(player.getUniqueId()) != null) {
            this.allRewards.remove(this.rewards.get(player.getUniqueId()));

            this.rewards.get(player.getUniqueId()).remove();
            this.rewards.remove(player.getUniqueId());
        }

        ChestManager.closeChest(location.getBlock(), false);

        removeActiveCrate(player);

        if (!useQuickCrateAgain) {
            if (this.hologramManager != null && crate != null) this.hologramManager.createHologram(location.getBlock(), crate);
        }
    }

    public void purgeRewards() {
        if (!this.allRewards.isEmpty()) this.allRewards.stream().filter(Objects::nonNull).forEach(Entity::remove);
    }

    // War Crate
    private final Map<UUID, Boolean> canPick = new HashMap<>();
    private final Map<UUID, Boolean> canClose = new HashMap<>();

    public void addPicker(Player player, boolean value) {
        this.canPick.put(player.getUniqueId(), value);
    }

    public boolean containsPicker(Player player) {
        return this.canPick.containsKey(player.getUniqueId());
    }

    public boolean isPicker(Player player) {
        return this.canPick.get(player.getUniqueId());
    }

    public void removePicker(Player player) {
        this.canPick.remove(player.getUniqueId());
    }

    public void addCloser(Player player, boolean value) {
        this.canClose.put(player.getUniqueId(), value);
    }

    public boolean containsCloser(Player player) {
        return this.canClose.containsKey(player.getUniqueId());
    }

    public void removeCloser(Player player) {
        this.canClose.remove(player.getUniqueId());
    }

    private final Map<UUID, Boolean> checkHands = new HashMap<>();

    public void addHands(Player player, boolean checkHand) {
        this.checkHands.put(player.getUniqueId(), checkHand);
    }

    public void removeHands(Player player) {
        this.checkHands.remove(player.getUniqueId());
    }

    public boolean getHand(Player player) {
        return this.checkHands.get(player.getUniqueId());
    }

    // Keys that are being used in crates. Only needed in cosmic due to it taking the key after the player picks a prize and not in a start method.
    private final Map<UUID, KeyType> playerKeys = new HashMap<>();

    /**
     * Set the type of key the player is opening a crate for.
     * This is only used in the Cosmic CrateType currently.
     *
     * @param player the player that is opening the crate.
     * @param keyType the KeyType that they are using.

    public void addPlayerKeyType(Player player, KeyType keyType) {
        this.playerKeys.put(player.getUniqueId(), keyType);
    }

    /**
     * Remove the player from the list as they have finished the crate.
     * Currently, only used in the Cosmic CrateType.
     *
     * @param player the player you are removing.

    public void removePlayerKeyType(Player player) {
        this.playerKeys.remove(player.getUniqueId());
    }

    /**
     * Check if the player is in the list.
     *
     * @param player the player you are checking.
     * @return true if they are in the list and false if not.

    public boolean hasPlayerKeyType(Player player) {
        return this.playerKeys.containsKey(player.getUniqueId());
    }

    /**
     * The key type the player's current crate is using.
     *
     * @param player the player that is using the crate.
     * @return the key type of the crate the player is using.

    public KeyType getPlayerKeyType(Player player) {
        return this.playerKeys.get(player.getUniqueId());
    }
}*/