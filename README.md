# Minecraft Ticket Creator (Bukkit)

Simple minecraft plugin for issuing custom tickets for player warps

>Feel free to alter the code of this plugin to your needs

> [!IMPORTANT]
> The plugin is tested only on 1.21 Paper <br />
> The plugin needs [Vault](https://www.spigotmc.org/resources/vault.34315/), [PlayerWarps](https://www.spigotmc.org/resources/⭐-player-warps-⭐-➢-let-your-players-set-warps-1-7-1-21.115286/) installed in order to enable! <br />
> The plugin is configured to use [CMI](https://www.spigotmc.org/resources/cmi-298-commands-insane-kits-portals-essentials-economy-mysql-sqlite-much-more.3742/) as an economy plugin, but can be changed in the `plugin.yml`

## Features
* 5 tiers of tickets
* Customizable lore, name, type, price and glowing for every tier
* [PlayerWarps](https://www.spigotmc.org/resources/⭐-player-warps-⭐-➢-let-your-players-set-warps-1-7-1-21.115286/) plugin connectivity
* Ability to check if the player is the owner of the warp
> /tickets reload \
> /tickets buy `tier` `warp` `quantity`

## Installation
1. Download the latest release on the right side of the page.
2. Download the [Vault](https://www.spigotmc.org/resources/vault.34315/), [PlayerWarps](https://www.spigotmc.org/resources/⭐-player-warps-⭐-➢-let-your-players-set-warps-1-7-1-21.115286/) plugin dependency.
3. Download [CMI](https://www.spigotmc.org/resources/cmi-298-commands-insane-kits-portals-essentials-economy-mysql-sqlite-much-more.3742/) or other Vault compatible economy plugin <br />
3.1. If you installed other economy plugin, change the `depend:` in `plugin.yml` in the source code, then recompile the `.jar` file
4. Drop all the `.jar` files in your `plugins` folder.
5. Start the server and a folder `UltrapixelTicketCreator` will be generated with a `config.yml` file.
6. Configure the plugin using `config.yml` to your needs
7. Reload the plugin using `/tickets reload`

