# NMZ Regen Alert

A RuneLite plugin that warns you just before your hitpoints regenerate, so you can flick Rapid Heal in time and stay at low HP in Nightmare Zone.

## Features

- **Screen flash** — the game view flashes red for a set time before your next hitpoint regenerates. It stops as soon as you flick Rapid Heal or the hitpoint regenerates. The flash is drawn beneath interfaces, so your prayer orb and inventory stay clear.
- **Notification** — a single notification when the warning starts, using RuneLite's standard notification options (sound, tray message, etc.).
- **Flick aware** — toggling Rapid Heal (for example double-clicking quick prayers) resets the regeneration timer and clears the warning.

The warning only shows while your hitpoints are below their real level, since that's the only time they can regenerate.

## Settings

| Setting | Default | Description |
| --- | --- | --- |
| Only in Nightmare Zone | On | Only warn while inside an NMZ dream |
| Warning time | 5 seconds | How long before regeneration to start warning (rounded up to game ticks) |
| Flash screen | On | Flash the game view during the warning |
| Flash colour | Translucent red | Colour of the flash |
| Notification | On | Notification sent when the warning starts |

## Note

The regeneration timer can't be read from the game directly, so the plugin keeps its own count, the same way the core Regeneration Meter does. Flick Rapid Heal once after entering a dream to line the timer up with the game.
