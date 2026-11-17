# Schematic assets

CoroUtil's `build.world.Build` only reads schematics from disk, at
`<save folder>/TCSchematics/<name>.schematic` (see `net.tropicraft.util.TCSchematics`, which copies the files
below out of the jar on first use). The files here are the source of truth and must be shipped in the jar.

## koavillage.schematic

* Original Tropicraft 1.7.10 asset, last modified 2015-04-12, recovered from the original distribution.
* CoroUtil "newFormat" `version 1.1`: `Width` 86, `Height` 16, `Length` 76, `surfaceOffset` 9,
  `Blocks` and `Data` stored as **int** arrays (not the byte arrays of old MCEdit schematics), plus
  `blockTranslationMap` and `TileEntities`.
* Dimensions must match `TownKoaVillageGenHelper.areaWidth/areaHeight/areaLength` (86/16/76).

### Do not "fix" the `tile.` prefixes in `blockTranslationMap`

`CoroUtilBlock.getNameByBlock()` returns `Block.blockRegistry.getNameForObject()`, and Tropicraft registers
its blocks with a `tile.` prefix on the *registry* name while setting the unlocalized name separately:

```java
// TCBlockRegistry.registerBlock(Block, String)
GameRegistry.registerBlock(block, "tile." + name);   // -> registry name "tropicraft:tile.chunk"
block.setBlockName(name);                            // -> unlocalized name "tile.chunk"
```

so the runtime lookup key for the chunk block is `tropicraft:tile.chunk`, exactly as stored in this file.
Rewriting the keys to `tropicraft:chunk` (which looks like the obvious cleanup) makes every Tropicraft block
in the village resolve to air - only the vanilla glass, ladders and air survive, i.e. an empty beach with
floating glass panes.

Verify against the running game rather than by eye: `Build.genBlockIDSchemToBlockWithRegistry` prints
`missing a mapping in this schematic for: <id>` for any entry it cannot resolve.

### Vertical placement

The structure's deck is at local y=1 and it extends to y=14, with the top layer empty, so
`TownKoaVillage.genSchematic()` builds from the terrain height with `yOffset = 0`. `surfaceOffset` is only
read into `Build.map_surfaceOffset`, which nothing uses (`BuildJob` has the alignment code behind
`if (false)`), so it is not a placement knob.
