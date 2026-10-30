<h1 align="center">CustomDeathMessages</h1>
<p align="center">
    <img alt="Licencia" src="https://img.shields.io/github/license/melerodev/CustomDeathMessages?style=for-the-badge&color=blue&labelColor=141417">
    <img alt="Última versión" src="https://img.shields.io/github/v/release/melerodev/CustomDeathMessages?include_prereleases&sort=semver&style=for-the-badge&label=VERSI%C3%93N&labelColor=141417">
    <img alt="CI" src="https://img.shields.io/github/actions/workflow/status/melerodev/CustomDeathMessages/ci.yml?style=for-the-badge&labelColor=141417">
    <img alt="Paper" src="https://img.shields.io/badge/Paper%20%2F%20Folia-1.20.6%20%E2%86%92%2026.3-e74c3c?style=for-the-badge&labelColor=141417">
</p>

---

## Descripción

**CustomDeathMessages** reemplaza los mensajes de muerte de los jugadores. No se limita a "cambiar un par de mensajes":
usa **plantillas configurables** con **placeholders**, mensajes **distintos para cada causa de muerte** y para cada tipo
de mob, varias variantes elegidas al azar y **compatibilidad con plugins de traducción**.

Construido sobre la plantilla [Minecraft-Plugin-Template](https://github.com/milkdrinkers/Minecraft-Plugin-Template).

## Características

- 📝 **Plantillas configurables** en MiniMessage (y códigos legacy), con varias variantes aleatorias por clave.
- ☠️ **Mensajes por causa de muerte**: caída, lava, ahogamiento, vacío, explosión… (todas las `DamageCause`).
- 🗡️ **Asesinado por jugador** (con el arma, con su tooltip) y **por mob**, con mensajes específicos por tipo (`zombie`, `creeper`, `warden`…).
- 🧩 **Placeholders**: víctima, asesino, tipo de asesino, arma, causa, mundo, coordenadas y el mensaje vanilla original; además **PlaceholderAPI** y **MiniPlaceholders**.
- 🌐 **Compatibilidad con traducciones**:
  - Las partes traducibles (nombre del mob, del arma, `<original>`, etiquetas `<lang:...>`) se envían como *translatable components*: cada cliente las ve en su idioma, y funcionan con plugins de traducción que usan el `GlobalTranslator` de Adventure.
  - Modo `per-player-locale`: cada jugador recibe la plantilla de **su idioma** (sección `locales`).
  - Evento de API `DeathMessageEvent` para que otros plugins (p. ej. de traducción) modifiquen o cancelen el mensaje.
- 🙊 Modos `custom`, `vanilla` o `hidden`; respeta la gamerule `showDeathMessages`.
- 🔄 `/customdeathmessages reload` sin reiniciar.
- ⚡ Compatible con **Folia**.

## Requisitos

- Servidor **Paper** o **Folia** de **1.20.6 a 26.3**.
- **Java 21** (Minecraft 1.20.6 – 1.21.x) o **Java 25** (Minecraft 26.x).
- No funciona en Spigot ni en versiones anteriores a 1.20.6.
- Opcional: [PlaceholderAPI](https://modrinth.com/plugin/placeholderapi), [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders).

Versiones probadas: 1.20.6, 1.21.1, 1.21.4, 1.21.8, 1.21.11, 26.1.2, 26.2 y 26.3.

## Instalación

1. Descarga el `.jar` desde [Releases](https://github.com/melerodev/CustomDeathMessages/releases) (o compílalo, ver abajo).
2. Cópialo en `plugins/` y reinicia el servidor.
3. Edita `plugins/CustomDeathMessages/config.yml` y ejecuta `/customdeathmessages reload`.

## Comandos

| Comando                        | Descripción                              | Permiso                                     |
|--------------------------------|------------------------------------------|---------------------------------------------|
| `/customdeathmessages`         | Información (modo, plantillas, idiomas)  | `customdeathmessages.command` (OP)          |
| `/customdeathmessages reload`  | Recarga `config.yml` y los idiomas       | `customdeathmessages.command.reload` (OP)   |

Alias: `/cdm`, `/deathmessages`.

## Placeholders

| Placeholder             | Valor                                                          |
|-------------------------|----------------------------------------------------------------|
| `<victim>` / `<player>` | Nombre de quien muere                                          |
| `<victim_displayname>`  | Nombre mostrado de la víctima                                  |
| `<killer>`              | Asesino: nombre del jugador, nombre personalizado o tipo de mob (traducido) |
| `<killer_type>`         | Tipo de entidad del asesino (traducido)                        |
| `<weapon>`              | Objeto en la mano del asesino, con tooltip (o `empty-hand`)    |
| `<cause>`               | Causa de muerte (`fall`, `lava`…)                              |
| `<world>` `<x>` `<y>` `<z>` | Lugar de la muerte                                         |
| `<original>`            | El mensaje vanilla (traducido en el idioma de cada cliente)   |
| `<lang:clave>`          | Cualquier clave de traducción (MiniMessage)                    |
| `%...%`                 | Cualquier placeholder de PlaceholderAPI (de la víctima)        |

PlaceholderAPI: `%customdeathmessages_deaths%` (muertes totales del jugador).

## Cómo se elige el mensaje

Para cada muerte se busca la plantilla más específica, en este orden:

1. Asesinado por un jugador → `messages.player`
2. Asesinado por un mob → `mob-messages.<tipo>` (p. ej. `creeper`)
3. Causa de muerte → `messages.<causa>` (p. ej. `fall`)
4. Cualquier mob → `messages.mob`
5. `messages.default`

Si una clave tiene varias líneas, se elige una al azar. Si no hay ninguna, se mantiene el mensaje vanilla
(`fallback-to-vanilla: true`) o la muerte es silenciosa.

## Mensajes por idioma

```yaml
per-player-locale: true
locales:
  es:            # o es_es, es_mx… (el idioma del cliente)
    messages:
      fall:
        - <red>☠ <white><victim> <gray>olvidó que no sabe volar
      player:
        - <red>☠ <white><victim> <gray>fue asesinado por <white><killer> <gray>usando <weapon>
    mob-messages:
      creeper:
        - <red>☠ <white><victim> <gray>abrazó a un <green>creeper
```

Los jugadores cuyo idioma no esté en `locales` reciben `messages`/`mob-messages`.

## API para desarrolladores

```java
@EventHandler
public void onDeathMessage(DeathMessageEvent event) {
    Player viewer = event.getViewer(); // null si es un broadcast a todos
    event.setMessage(myTranslator.translate(event.getMessage(), viewer));
    // event.setCancelled(true) para no enviarlo
}
```

<details>
<summary><code>config.yml</code> por defecto (resumido)</summary>

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

## Compilar

```bash
./gradlew build
```

El jar queda en `paper/build/libs/`. Para probarlo en un servidor local:

```bash
./gradlew runServer                      # Paper 1.21.11
./gradlew runServer -PmcVersion=1.20.6   # cualquier otra versión
```

## Licencia

[MIT No Attribution](LICENSE). Basado en [milkdrinkers/Minecraft-Plugin-Template](https://github.com/milkdrinkers/Minecraft-Plugin-Template).
