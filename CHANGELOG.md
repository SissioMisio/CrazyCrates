## Changes:
* Updated the message in commands, instead of `misc.no-virtual-keys`, It will be using `misc.no-keys` message

## Fixes:
* Send the message to the command sender instead of the player when using `/crates forceopen`

<details>
    <summary>Previous Changelog</summary>

[8e19c5c](https://github.com/Crazy-Crew/CrazyCrates/commit/8e19c5c) Add 1.20.6 support

## Additions:
* Added missing messages to the `messages.yml`
    * Ability to customize the output of /crazycrates list
* Support for PlaceholderAPI in lores/displaynames for keys
* You no longer have to include `https://textures.minecraft.net/texture/` when using custom heads.
    * You can simply use `1ee3126ff2c343da525eef2b93272b9fed36273d0ea08c2616b80009948ad57e` in the `Player` field.
    * You can find an example in the `examples/crates` directory!
* Added a warning if trying to add AIR to a crate using `/crates additem`

## Fixes:
* [b25b867](https://github.com/Crazy-Crew/CrazyCrates/commit/b25b867) fix an issue with the preview not working for casino/cosmic tiers (#726)
* [46e6dba](https://github.com/Crazy-Crew/CrazyCrates/commit/46e6dba) Add /crazycrates forceopen back (#715)
* [5940d29](https://github.com/Crazy-Crew/CrazyCrates/commit/5940d29) Fix a compile issue with workflows (#724)
* [d9a9f49](https://github.com/Crazy-Crew/CrazyCrates/commit/d9a9f49) Fix cosmic crate (#716)
* [854efe6](https://github.com/Crazy-Crew/CrazyCrates/commit/854efe6) Fix a npe on player quit (#719)
* Fixed an issue with QuickCrate where holograms were stacking.
* Fixed an issue where the display name of the item above QuickCrate had [] around it i.e. [Diamond Helmet]
* open-others shouldn't be usable by everyone.
* fix default casino crate.
* update fallback sound.

## Changes:
* Move the creation of random out of the for loops for tiers and prize picking. a performance increase nonetheless.
* CrazyCrates API has been bumped to 0.6.
* Add second prize to AdvancedExample.yml.

## Breaking Changes:
### Permissions:
* Command / General Permissions have been updated!
    * You can find a list of permissions @ https://docs.crazycrew.us/docs/1.20.6/plugins/crazycrates/commands/permissions
    * They will not change again, but they are easier to type.

### In-game editor:
* All previous iterations of the in-game editor **do not** work anymore. All added prizes using the old methods WILL not work.
    * You **must** update all your prizes as a lot of the internals have changed for **1.20.5-1.20.6**
    * You should be able to just re-run the command.

### Item IDS
* All items ids used for potions, materials, blocks, trim materials/patterns and sounds etc. have all been changed.
    * A list of sounds: https://minecraft.wiki/w/Sounds.json#Java_Edition_values, **Custom Sounds from resource packs are also supported!**
* Enchantments instead of `PROTECTION_ENVIRONMENTAL` and `DAMAGE_ALL`, It would be `protection` and `sharpness`
    <details>
      <summary>List of Enchantments</summary>

  * protection
  * fire_protection
  * feather_falling
  * blast_protection
  * projectile_protection
  * respiration
  * aqua_affinity
  * thorns
  * depth_strider
  * frost_walker
  * binding_curse
  * sharpness
  * smite
  * bane_of_arthropods
  * knockback
  * fire_aspect
  * looting
  * sweeping_edge
  * efficiency
  * silk_touch
  * unbreaking
  * fortune
  * power
  * punch
  * flame
  * infinity
  * luck_of_the_sea
  * lure
  * loyalty
  * impaling
  * riptide
  * channeling
  * multishot
  * quick_charge
  * piercing
  * mending
  * vanishing_curse
  * soul_speed
  * swift_sneak
  </details>

* You can find a list of updated trim materials/patterns below!
    * https://docs.crazycrew.us/docs/1.20.6/plugins/crazycrates/guides/prizes/items/armor-trim

</details>

## Other:
* [Feature Requests](https://github.com/Crazy-Crew/CrazyCrates/discussions/categories/features)
* [Bug Reports](https://github.com/Crazy-Crew/CrazyCrates/issues)