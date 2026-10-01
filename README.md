# Lootable Bodies Reanimated

A Minecraft 1.7.10 fork of DrCyano's Lootable Bodies. The runtime mod ID remains
`lootablebodies` for compatibility with existing worlds.

## Requirements

- Minecraft 1.7.10 and Forge 10.13.4.1614
- Java 8 JDK for development

## Development

Use the included Gradle 4.4.1 wrapper with a Java 8 JDK. The build uses the
anatawa12 ForgeGradle 1.2 fork. Set `JAVA_HOME` to the JDK before running:

```text
gradlew.bat clean --refresh-dependencies
gradlew.bat setupDecompWorkspace
gradlew.bat build
gradlew.bat runClient
gradlew.bat runServer
```

Gradle places the production JAR in `build/libs/` and uses `run/` for development
launches. Set the release version in `version.properties`.
The development server creates `run/eula.txt` on its first launch; accept the
Minecraft EULA there before starting a local server.

## Configuration

Forge writes `config/lootablebodies.cfg` under the run directory. Corpses are
created on the server when a player dies, including when `keepInventory` is on.
With `keepInventory`, player equipment stays with the player and the corpse
contains only optional bones and any separate death drops from other mods.

| Category | Option | Default | Effect |
| --- | --- | --- | --- |
| `options` | `item_damage_on_death` | `0` | Adds this much wear to damageable items placed in a corpse, leaving at least one durability when enabled. |
| `options` | `corpse_HP` | `50` | Sets the corpse's maximum and starting health. |
| `options` | `add_bones_to_corpse` | `true` | Adds bones and rotten flesh to each corpse. |
| `options` | `corpse_inventory_size` | `108` | Limits the total number of corpse item slots; 54 are visible at once. Excess death drops remain in the world. |
| `options` | `eio_Soulbound_id` | `-1` | Excludes drops with the configured EnderIO Soulbound enchantment ID from the corpse; `-1` disables the match. |
| `corpse damage` | `hurt_by_all` | `false` | Allows every damage category below. |
| `corpse damage` | `hurt_by_fire` | `false` | Allows fire and lava damage. |
| `corpse damage` | `hurt_by_explosions` | `false` | Allows explosion damage. |
| `corpse damage` | `hurt_by_fall` | `false` | Allows fall damage, including falls caused by explosions. |
| `corpse damage` | `hurt_by_cactus` | `false` | Allows cactus damage. |
| `corpse damage` | `hurt_by_weapons` | `false` | Allows damage attributed to a living attacker. |
| `corpse damage` | `hurt_by_block_suffocation` | `false` | Allows suffocation damage inside blocks. |
| `corpse damage` | `hurt_by_other` | `false` | Allows damage sources outside those named above. |
| `corpse decay` | `enable_corpse_decay` | `false` | Enables timed corpse removal and drops its remaining contents. |
| `corpse decay` | `empty_only_decay` | `false` | Starts the timer after all visible and overflow slots become empty. |
| `corpse decay` | `corpse_decay_time` | `1:00:00` | Sets the decay delay as `hours:minutes:seconds` or `hours:minutes`, with a two-second minimum. |

Shovels can bury corpses after three hits even when regular damage is disabled.
Decay uses total server time, so disabling daylight progression does not stop
the timer. An unloaded corpse can expire when its chunk is loaded again.
Existing config files retain their saved `item_damage_on_death` value; set it
to `0` there to disable wear in an existing world.
The old `use_player_skin` setting is not offered by this 1.7.10 port; its
player-skin renderer is disabled.

## License

The original source tree includes the CC0 1.0 Universal dedication in [LICENSE](LICENSE).
