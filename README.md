<h1 align="center">CustomDeathMessages</h1>
<p align="center">
    <img alt="License" src="https://img.shields.io/github/license/melerodev/CustomDeathMessages?style=for-the-badge&color=blue&labelColor=141417">
    <img alt="Latest version" src="https://img.shields.io/github/v/release/melerodev/CustomDeathMessages?include_prereleases&sort=semver&style=for-the-badge&label=VERSION&labelColor=141417">
    <img alt="CI" src="https://img.shields.io/github/actions/workflow/status/melerodev/CustomDeathMessages/ci.yml?style=for-the-badge&labelColor=141417">
    <img alt="Paper" src="https://img.shields.io/badge/Paper%20%2F%20Folia-1.20.6%20%E2%86%92%2026.3-e74c3c?style=for-the-badge&labelColor=141417">
    <a href="https://discord.gg/rMuWwZzCW2"><img alt="Discord" src="https://img.shields.io/badge/Discord-Join-5865F2?style=for-the-badge&logo=discord&logoColor=white&labelColor=141417"></a>
</p>

---

## Description

**CustomDeathMessages** replaces player death messages. It goes beyond "changing a couple of messages":
it uses **configurable templates** with **placeholders**, **different messages for each death cause** and for each
mob type, several randomly picked variants and **compatibility with translation plugins**.

Built on top of [Minecraft-Plugin-Template](https://github.com/milkdrinkers/Minecraft-Plugin-Template).

## Features

- 📝 **Configurable templates** in MiniMessage (and legacy codes), with several random variants per key.
- ☠️ **Messages per death cause**: fall, lava, drowning, void, explosions… (every `DamageCause`).
- 🗡️ **Killed by a player** (with the weapon and its tooltip) and **by a mob**, with specific messages per type (`zombie`, `creeper`, `warden`…).
- 🧩 **Placeholders**: victim, killer, killer type, weapon, cause, world, coordinates and the original vanilla message; plus **PlaceholderAPI** and **MiniPlaceholders**.
- 🌐 **Translation support**:
  - Translatable parts (mob name, weapon name, `<original>`, `<lang:...>` tags) are sent as *translatable components*: each client sees them in their own language, and they work with translation plugins that use Adventure's `GlobalTranslator`.
  - `per-player-locale` mode: every player gets the template in **their own language** (`locales` section).
  - `DeathMessageEvent` API event so other plugins (e.g. translation plugins) can change or cancel the message.
- 🙊 `custom`, `vanilla` or `hidden` modes; respects the `showDeathMessages` gamerule.
- 🔄 `/customdeathmessages reload` without restarting.
- ⚡ **Folia** supported.

## Requirements

- **Paper** or **Folia** server, **1.20.6 to 26.3**.
- **Java 21** (Minecraft 1.20.6 – 1.21.x) or **Java 25** (Minecraft 26.x).
- Does not run on Spigot or on versions older than 1.20.6.
- Optional: [PlaceholderAPI](https://modrinth.com/plugin/placeholderapi), [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders).

Tested versions: 1.20.6, 1.21.1, 1.21.4, 1.21.8, 1.21.11, 26.1.2, 26.2 and 26.3.

## Installation

1. Download the `.jar` from [Releases](https://github.com/melerodev/CustomDeathMessages/releases) (or build it, see below).
2. Put it in `plugins/` and restart the server.
3. Edit `plugins/CustomDeathMessages/config.yml` and run `/customdeathmessages reload`.

## Commands

| Command                        | Description                              | Permission                                  |
|--------------------------------|------------------------------------------|---------------------------------------------|
| `/customdeathmessages`         | Info (mode, templates, locales)          | `customdeathmessages.command` (OP)          |
| `/customdeathmessages reload`  | Reloads `config.yml` and the language files | `customdeathmessages.command.reload` (OP) |

Aliases: `/cdm`, `/deathmessages`.

## Placeholders

| Placeholder             | Value                                                          |
|-------------------------|----------------------------------------------------------------|
| `<victim>` / `<player>` | Name of the player who died                                    |
| `<victim_displayname>`  | Victim's display name                                          |
| `<killer>`              | Killer: player name, custom name or mob type (translated)      |
| `<killer_type>`         | Killer's entity type (translated)                              |
| `<weapon>`              | Item in the killer's hand, with tooltip (or `empty-hand`)      |
| `<cause>`               | Death cause (`fall`, `lava`…)                                  |
| `<world>` `<x>` `<y>` `<z>` | Death location                                             |
| `<original>`            | The vanilla message (translated into each client's language)  |
| `<lang:key>`            | Any translation key (MiniMessage)                              |
| `%...%`                 | Any PlaceholderAPI placeholder (of the victim)                 |

PlaceholderAPI: `%customdeathmessages_deaths%` (the player's total deaths).

## How a message is picked

For every death the most specific template is used, in this order:

1. Killed by a player → `messages.player`
2. Killed by a mob → `mob-messages.<type>` (e.g. `creeper`)
3. Death cause → `messages.<cause>` (e.g. `fall`)
4. Any mob → `messages.mob`
5. `messages.default`

If a key has several lines, one is picked at random. If nothing matches, the vanilla message is kept
(`fallback-to-vanilla: true`) or the death is silent.

## Messages per language

```yaml
per-player-locale: true
locales:
  es:            # or es_es, es_mx… (the client language)
    messages:
      fall:
        - <red>☠ <white><victim> <gray>olvidó que no sabe volar
      player:
        - <red>☠ <white><victim> <gray>fue asesinado por <white><killer> <gray>usando <weapon>
    mob-messages:
      creeper:
        - <red>☠ <white><victim> <gray>abrazó a un <green>creeper
```

Players whose language is not in `locales` get `messages`/`mob-messages`.

## Developer API

```java
@EventHandler
public void onDeathMessage(DeathMessageEvent event) {
    Player viewer = event.getViewer(); // null when it is a broadcast to everyone
    event.setMessage(myTranslator.translate(event.getMessage(), viewer));
    // event.setCancelled(true) to not send it
}
```

<details>
<summary>Default <code>config.yml</code> (summarized)</summary>

```yaml
# custom = use the messages below, vanilla = keep Minecraft's messages, hidden = no death messages
mode: custom
per-player-locale: false
empty-hand: <white>their fists
fallback-to-vanilla: true

messages:
  player:
    - <red>☠ <white><victim> <gray>was slain by <white><killer> <gray>using <weapon>
    - <red>☠ <white><killer> <gray>sent <white><victim> <gray>back to spawn with <weapon>
  mob:
    - <red>☠ <white><victim> <gray>was killed by <white><killer>
  fall:
    - <red>☠ <white><victim> <gray>forgot they can't fly
    - <red>☠ <white><victim> <gray>hit the ground too hard
  lava:
    - <red>☠ <white><victim> <gray>tried to swim in lava
  drowning:
    - <red>☠ <white><victim> <gray>drowned
  void:
    - <red>☠ <white><victim> <gray>fell out of the world
  entity_explosion:
    - <red>☠ <white><victim> <gray>was blown up by <white><killer>
  # ... fire, fire_tick, suffocation, starvation, lightning, block_explosion, magic, wither,
  #     freeze, contact, hot_floor, fly_into_wall, falling_block, cramming, world_border, kill
  default:
    - <red>☠ <white><victim> <gray>died

mob-messages:
  zombie:
    - <red>☠ <white><victim> <gray>became a snack for a <dark_green>zombie
  creeper:
    - <red>☠ <white><victim> <gray>hugged a <green>creeper
  # ... skeleton, spider, enderman, warden

locales:
  es:
    messages: { ... }
    mob-messages: { ... }

# en_US or es_ES (plugin command messages)
language: en_US
```

</details>

## Building

```bash
./gradlew build
```

The jar ends up in `paper/build/libs/`. To try it on a local server:

```bash
./gradlew runServer                      # Paper 1.21.11
./gradlew runServer -PmcVersion=1.20.6   # any other version
```

## Support

Questions, suggestions or problems? Join our Discord:

<a href="https://discord.gg/rMuWwZzCW2"><img alt="Join our Discord" src="docs/assets/discord.png" width="300"></a>

You can also open an [issue](https://github.com/melerodev/CustomDeathMessages/issues).

## License

[MIT No Attribution](LICENSE). Based on [milkdrinkers/Minecraft-Plugin-Template](https://github.com/milkdrinkers/Minecraft-Plugin-Template).
