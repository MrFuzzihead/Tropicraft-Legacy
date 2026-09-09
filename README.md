# Tropicraft Legacy

[![Build status](https://github.com/MrFuzzihead/Tropicraft-Legacy/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/MrFuzzihead/Tropicraft-Legacy/actions/workflows/build-and-test.yml)
[![Latest release](https://img.shields.io/github/v/release/MrFuzzihead/Tropicraft-Legacy?include_prereleases&sort=semver)](https://github.com/MrFuzzihead/Tropicraft-Legacy/releases/latest)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.7.10-62a34a)](https://minecraft.wiki/w/Java_Edition_1.7.10)
[![Forge](https://img.shields.io/badge/Forge-10.13.4.1614-1e2b4f)](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.7.10.html)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

**Tropicraft** is the utopian, idyllic tropical getaway that you have been waiting for! This repository is a community-maintained continuation of the classic [Tropicraft](https://legacy.curseforge.com/minecraft/mc-mods/tropicraft) mod for **Minecraft 1.7.10 (Forge)**, keeping the tropical dimension alive with bug fixes, crash fixes, and a modernized build toolchain.

> Looking for Tropicraft on modern versions of Minecraft? Check out the official upstream project on [CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/tropicraft).

## Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Downloads](#downloads)
- [Building from source](#building-from-source)
- [Contributing](#contributing)
- [Changes in this fork](#changes-in-this-fork)
- [Credits](#credits)
- [License](#license)

## Features

- A whole new dimension — **the Tropics** — with beaches, rainforests, and erupting volcanoes
- **Koa villagers** with their own villages, culture, and trade
- New passive and hostile mobs: iguanas, tree frogs, sea turtles, v-monkeys, ashen, and more
- Dozens of new plants, blocks, and building materials: bamboo, mahogany, palm, thatch, and more
- Tropical foods and drinks — pineapples, coconuts, and of course the **Piña Colada** that gets you into the dimension
- Beach chairs, umbrellas, scuba gear, and other vacation essentials

## Requirements

| Dependency | Version | Notes |
| ---------- | ------- | ----- |
| Minecraft | 1.7.10 | — |
| Minecraft Forge | 10.13.4.1614 | [Download](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.7.10.html) |
| [CoroUtil](https://legacy.curseforge.com/minecraft/mc-mods/coroutil) | 1.7.10 build | Required at runtime |

## Installation

1. Install **Minecraft Forge 10.13.4.1614** for Minecraft 1.7.10 (run the installer and select *Client*).
2. Download the latest **Tropicraft Legacy** jar from the [Releases](https://github.com/MrFuzzihead/Tropicraft-Legacy/releases/latest) page.
3. Download [CoroUtil](https://legacy.curseforge.com/minecraft/mc-mods/coroutil) for 1.7.10.
4. Drop both jars into your `.minecraft/mods` folder.
5. Launch the game with the Forge profile.

## Downloads

- **[GitHub Releases](https://github.com/MrFuzzihead/Tropicraft-Legacy/releases/latest)** — recommended; stable builds tagged from `main`.
- **[GitHub Actions](https://github.com/MrFuzzihead/Tropicraft-Legacy/actions)** — bleeding-edge snapshot builds from every push to `main`.

## Building from source

This project uses the [GTNewHorizons](https://github.com/GTNewHorizons) 1.7.10 Gradle toolchain. The correct JDK is provisioned automatically by Gradle — no manual Java setup required.

```bash
git clone https://github.com/MrFuzzihead/Tropicraft-Legacy.git
cd Tropicraft-Legacy
./gradlew build        # on Windows: gradlew.bat build
```

The built jars are placed in `build/libs/`. To launch a development client:

```bash
./gradlew runClient    # on Windows: gradlew.bat runClient
```

## Contributing

Bug reports and pull requests are welcome! Please open issues on the [issue tracker](https://github.com/MrFuzzihead/Tropicraft-Legacy/issues).

- Branch naming follows `bugfix-<issue>-<short-name>` or `feature-<issue>-<short-name>`.
- CI runs the full build (including Checkstyle and Spotless checks) on every pull request, so please make sure `gradlew build` passes locally before opening a PR.

## Changes in this fork

This fork builds on [quentin452's 1.7.10 continuation](https://github.com/quentin452/tropicraft-REUP) of Tropicraft. On top of that:

**Merged**

- Modernized build system for 2026 (GTNH convention plugin, Gradle wrapper, auto-provisioned JDK, GitHub Actions CI)
- Fixed cascading world generation issues (home trees, tall trees, tualang, tall flowers, biome generation)
- Fixed entity crashes and broken/missing sound events (e.g. bamboo now uses proper wood sounds)
- Fixed Unicode corruption of the section sign (`U+FFFD` → `U+00A7`) that could crash the game

**In review (open PRs)**

- Flammable blocks, invisible home tree blocks, and mahogany tree generation fixes
- Bamboo flower pot and Koa chest item icon fixes
- Player model sitting in chairs, fish flopping slow-mo fixes
- Pineapple texture improvements and additional language files

## Credits

- **[Original Tropicraft](https://legacy.curseforge.com/minecraft/mc-mods/tropicraft)** by Cojomax99, Corosus, Fishtaco567, Mr_okushama, MrRube, newthead, and 303
- **[quentin452](https://github.com/quentin452/tropicraft-REUP)** for the prior 1.7.10 continuation this fork builds upon
- **[GTNewHorizons](https://github.com/GTNewHorizons)** for the modern 1.7.10 build tooling

## License

This project is licensed under the [MIT License](LICENSE). Tropicraft is the work of its original authors; this fork only extends and maintains their work for 1.7.10.
