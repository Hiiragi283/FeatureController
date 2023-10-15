# Feature Controller

## Overview

- Control below features via JSON file
  - Biome Decorations
    - `ICE`: Ice spike and Ice patch in cold biomes
    - `SAND_PASS2`: Gravel Generation
  - Chunk Features
    - `FIRE`: Fire patches in Nether
    - `NETHER_LAVA`: Lava traps facing outside
    - `NETHER_LAVA2`: Lava traps in Netherracks
    - `NETHER_MAGMA`: Magma block vein
    - `AMINALS`: Initial creatures when chunk generated
  - Minable Features
  - Removing Cave/Ravine
  - Prevent Mob Spawning
    - when `preventFromSpawner` is true, prevent mob spawning from mob spawner

- All allowed values are written in config when generated file
  - not loaded before the value `enabled` is changed

## License

- [MIT License](https://github.com/Hiiragi283/FeatureController/blob/master/LICENSE)
    - `logo.png` ... [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/)

## Credits

- This project is based on [GregTechCEu/BuildScripts](https://github.com/GregTechCEu/Buildscripts)