package net.tropicraft.world.chunk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.tropicraft.world.mapgen.MapGenTropicsCaves;
import net.tropicraft.world.mapgen.MapGenUndergroundGrove;
import net.tropicraft.world.mapgen.MapGenVolcano;

public class ChunkProviderTropicraft implements IChunkProvider {

    private static final int CHUNK_SIZE_Y = 256;
    private static final int HOME_TREE_RARITY = 350;
    private World worldObj;
    private long seed;
    protected Random rand;
    private BiomeGenBase[] biomesForGeneration;
    private MapGenUndergroundGrove groveGen;
    private MapGenTropicsCaves caveGenerator;
    private MapGenVolcano volcanoGen;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorOctaves noiseGen4;
    private NoiseGeneratorOctaves noiseGen5;
    private WorldGenerator eudialyteGen;
    private WorldGenerator zirconGen;
    private WorldGenerator azuriteGen;
    private WorldGenerator ironGen;
    private WorldGenerator coalGen;
    private WorldGenerator lapisGen;
    private float[] parabolicField;
    /**
     * Radius (in chunks) of the neighborhood that must be fully loaded before a chunk's decorations are applied.
     * This guarantees that every block read/write performed by decoration (trees, villages, etc.) lands in an
     * already-generated chunk, eliminating cascading chunk generation while keeping worldgen fully accurate.
     */
    private static final int DECOR_RADIUS = 3;
    /** Chunks whose terrain is generated but whose decorations are deferred until their neighborhood is loaded. */
    private final Set<ChunkCoordIntPair> pendingDecoration = new HashSet<>();
    /** Debug: depth of nesting while decorations are being applied (detects cascading chunk generation). */
    private int decoratingDepth;
    /** Debug: number of chunk generations triggered from inside decoration code (must be 0). */
    private long cascadesDetected;

    public ChunkProviderTropicraft(final World worldObj, final long seed, final boolean par4) {
        this.worldObj = worldObj;
        this.rand = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 16);
        this.caveGenerator = new MapGenTropicsCaves();
        this.volcanoGen = new MapGenVolcano(worldObj, true);
        this.groveGen = new MapGenUndergroundGrove(worldObj);
        this.coalGen = new WorldGenMinable(Blocks.coal_ore, 16);
        this.lapisGen = new WorldGenMinable(Blocks.lapis_ore, 6);
        this.ironGen = new WorldGenMinable(Blocks.iron_ore, 8);
        this.eudialyteGen = new WorldGenMinable(TCBlockRegistry.eudialyteOre, 6);
        this.zirconGen = new WorldGenMinable(TCBlockRegistry.zirconOre, 4);
        this.azuriteGen = new WorldGenMinable(TCBlockRegistry.azuriteOre, 2);
        this.seed = seed;
    }

    public Chunk provideChunk(final int x, final int z) {
        if (this.decoratingDepth > 0) {
            // A chunk was generated from inside decoration code: that means some block read/write
            // touched an unloaded chunk (cascading worldgen). With the deferred-decoration gate this
            // should never happen; log it loudly so it can be fixed.
            ++this.cascadesDetected;
            if (this.cascadesDetected == 1L || this.cascadesDetected % 100L == 0L) {
                System.err.println(
                    "[Tropicraft] CASCADING CHUNK GENERATION DETECTED during decoration! count=" + this.cascadesDetected
                        + " at chunk "
                        + x
                        + ","
                        + z);
                new Throwable("cascade trace").printStackTrace();
            }
        }
        this.rand.setSeed(x * 341873128712L + z * 132897987541L);
        final Block[] blocks = new Block[65536];
        final byte[] metas = new byte[65536];
        this.generateTerrain(x, z, blocks, metas);
        this.replaceBlocksForBiome(
            x,
            z,
            blocks,
            metas,
            this.biomesForGeneration = this.worldObj.getWorldChunkManager()
                .loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16));
        this.volcanoGen.generate(x, z, blocks, metas);
        this.groveGen.generate(x, z, blocks, metas);
        this.caveGenerator.generate((IChunkProvider) this, this.worldObj, x, z, blocks);
        final Chunk chunk = new Chunk(this.worldObj, blocks, metas, x, z);
        final byte[] abyte1 = chunk.getBiomeArray();
        for (int k = 0; k < abyte1.length; ++k) {
            abyte1[k] = (byte) this.biomesForGeneration[k].biomeID;
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    private void generateTerrain(final int x, final int z, final Block[] blocks, final byte[] metas) {
        final byte chunkSizeGenXZ = 4;
        final byte chunkSizeGenY = 16;
        final byte midHeight = 63;
        final int k = chunkSizeGenXZ + 1;
        final byte b3 = 17;
        final int l = chunkSizeGenXZ + 1;
        this.biomesForGeneration = this.worldObj.getWorldChunkManager()
            .getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, k + 5, l + 5);
        double[] noiseArray = null;
        noiseArray = this.initializeNoiseField(noiseArray, x * chunkSizeGenXZ, 0, z * chunkSizeGenXZ, k, b3, l);
        for (int i1 = 0; i1 < chunkSizeGenXZ; ++i1) {
            for (int j1 = 0; j1 < chunkSizeGenXZ; ++j1) {
                for (int k2 = 0; k2 < chunkSizeGenY; ++k2) {
                    final double d0 = 0.125;
                    double d2 = noiseArray[((i1 + 0) * l + j1 + 0) * b3 + k2 + 0];
                    double d3 = noiseArray[((i1 + 0) * l + j1 + 1) * b3 + k2 + 0];
                    double d4 = noiseArray[((i1 + 1) * l + j1 + 0) * b3 + k2 + 0];
                    double d5 = noiseArray[((i1 + 1) * l + j1 + 1) * b3 + k2 + 0];
                    final double d6 = (noiseArray[((i1 + 0) * l + j1 + 0) * b3 + k2 + 1] - d2) * d0;
                    final double d7 = (noiseArray[((i1 + 0) * l + j1 + 1) * b3 + k2 + 1] - d3) * d0;
                    final double d8 = (noiseArray[((i1 + 1) * l + j1 + 0) * b3 + k2 + 1] - d4) * d0;
                    final double d9 = (noiseArray[((i1 + 1) * l + j1 + 1) * b3 + k2 + 1] - d5) * d0;
                    for (int l2 = 0; l2 < 8; ++l2) {
                        final double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        final double d13 = (d4 - d2) * d10;
                        final double d14 = (d5 - d3) * d10;
                        for (int i2 = 0; i2 < 4; ++i2) {
                            final double d15 = 0.25;
                            final double d16 = (d12 - d11) * d15;
                            double d17 = d11 - d16;
                            for (int k3 = 0; k3 < 4; ++k3) {
                                final int index = (i1 * 4 + i2) * 256 * 16 | (j1 * 4 + k3) * 256 | k2 * 8 + l2;
                                if ((d17 += d16) > 0.0) {
                                    blocks[index] = Blocks.stone;
                                } else if (k2 * 8 + l2 < midHeight) {
                                    blocks[index] = (Block) TCBlockRegistry.tropicsWater;
                                } else {
                                    blocks[index] = Blocks.air;
                                }
                            }
                            d11 += d13;
                            d12 += d14;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                    }
                }
            }
        }
    }

    private double[] initializeNoiseField(double[] par1ArrayOfDouble, final int par2, final int par3, final int par4,
        final int par5, final int par6, final int par7) {
        if (par1ArrayOfDouble == null) {
            par1ArrayOfDouble = new double[par5 * par6 * par7];
        }
        if (this.parabolicField == null) {
            this.parabolicField = new float[25];
            for (int k1 = -2; k1 <= 2; ++k1) {
                for (int l1 = -2; l1 <= 2; ++l1) {
                    final float f = 10.0f / MathHelper.sqrt_float(k1 * k1 + l1 * l1 + 0.2f);
                    this.parabolicField[k1 + 2 + (l1 + 2) * 5] = f;
                }
            }
        }
        final double d0 = 684.412;
        final double d2 = 684.412;
        double[] noise1 = null;
        double[] noise2 = null;
        double[] noise3 = null;
        double[] noise4 = null;
        double[] noise5 = null;
        noise1 = this.noiseGen1.generateNoiseOctaves(noise1, par2, par3, par4, par5, par6, par7, d0, d2, d0);
        noise2 = this.noiseGen2.generateNoiseOctaves(noise2, par2, par3, par4, par5, par6, par7, d0, d2, d0);
        noise3 = this.noiseGen3
            .generateNoiseOctaves(noise3, par2, par3, par4, par5, par6, par7, d0 / 80.0, d2 / 160.0, d0 / 80.0);
        noise4 = this.noiseGen4.generateNoiseOctaves(noise4, par2, par4, par5, par7, 1.121, 1.121, 0.5);
        noise5 = this.noiseGen5.generateNoiseOctaves(noise5, par2, par4, par5, par7, 200.0, 200.0, 0.5);
        final boolean flag = false;
        final boolean flag2 = false;
        int i2 = 0;
        int j2 = 0;
        for (int k2 = 0; k2 < par5; ++k2) {
            for (int l2 = 0; l2 < par7; ++l2) {
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                final byte b0 = 2;
                final BiomeGenBase biomegenbase = this.biomesForGeneration[k2 + 2 + (l2 + 2) * (par5 + 5)];
                for (int i3 = -b0; i3 <= b0; ++i3) {
                    for (int j3 = -b0; j3 <= b0; ++j3) {
                        final BiomeGenBase biomegenbase2 = this.biomesForGeneration[k2 + i3
                            + 2
                            + (l2 + j3 + 2) * (par5 + 5)];
                        float f5 = this.parabolicField[i3 + 2 + (j3 + 2) * 5] / (biomegenbase2.rootHeight + 2.0f);
                        if (biomegenbase2.rootHeight > biomegenbase.rootHeight) {
                            f5 /= 2.0f;
                        }
                        f2 += biomegenbase2.heightVariation * f5;
                        f3 += biomegenbase2.rootHeight * f5;
                        f4 += f5;
                    }
                }
                f2 /= f4;
                f3 /= f4;
                f2 = f2 * 0.9f + 0.1f;
                f3 = (f3 * 4.0f - 1.0f) / 8.0f;
                double d3 = noise5[j2] / 8000.0;
                if (d3 < 0.0) {
                    d3 = -d3 * 0.3;
                }
                d3 = d3 * 3.0 - 2.0;
                if (d3 < 0.0) {
                    d3 /= 2.0;
                    if (d3 < -1.0) {
                        d3 = -1.0;
                    }
                    d3 /= 1.4;
                    d3 /= 2.0;
                } else {
                    if (d3 > 1.0) {
                        d3 = 1.0;
                    }
                    d3 /= 8.0;
                }
                ++j2;
                for (int k3 = 0; k3 < par6; ++k3) {
                    double d4 = f3;
                    final double d5 = f2;
                    d4 += d3 * 0.2;
                    d4 = d4 * par6 / 16.0;
                    final double d6 = par6 / 2.0 + d4 * 4.0;
                    double d7 = 0.0;
                    double d8 = (k3 - d6) * 12.0 * 128.0 / 128.0 / d5;
                    if (d8 < 0.0) {
                        d8 *= 4.0;
                    }
                    final double d9 = noise1[i2] / 512.0;
                    final double d10 = noise2[i2] / 512.0;
                    final double d11 = (noise3[i2] / 10.0 + 1.0) / 2.0;
                    if (d11 < 0.0) {
                        d7 = d9;
                    } else if (d11 > 1.0) {
                        d7 = d10;
                    } else {
                        d7 = d9 + (d10 - d9) * d11;
                    }
                    d7 -= d8;
                    if (k3 > par6 - 4) {
                        final double d12 = (k3 - (par6 - 4)) / 3.0f;
                        d7 = d7 * (1.0 - d12) + -10.0 * d12;
                    }
                    par1ArrayOfDouble[i2] = d7;
                    ++i2;
                }
            }
        }
        return par1ArrayOfDouble;
    }

    public void replaceBlocksForBiome(final int x, final int z, final Block[] blocks, final byte[] metas,
        final BiomeGenBase[] biomes) {
        final int sandType = this.rand.nextInt(200);
        Block sandBlock;
        short sandMetadata;
        switch (sandType) {
            case 0: {
                sandBlock = TCBlockRegistry.mineralSands;
                sandMetadata = 0;
                break;
            }
            case 1: {
                sandBlock = TCBlockRegistry.mineralSands;
                sandMetadata = 1;
                break;
            }
            case 2: {
                sandBlock = TCBlockRegistry.mineralSands;
                sandMetadata = 2;
                break;
            }
            case 3: {
                sandBlock = TCBlockRegistry.mineralSands;
                sandMetadata = 3;
                break;
            }
            default: {
                sandBlock = Blocks.sand;
                sandMetadata = 0;
                break;
            }
        }
        int a = -1;
        boolean flag = false;
        final int k = 63;
        final double d = 0.03125;
        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                final BiomeGenTropicraft biome = (BiomeGenTropicraft) biomes[i1 + l * 16];
                final Block top = biome.topBlock;
                final Block filler = biome.fillerBlock;
                Block btop = sandBlock;
                if (biome == BiomeGenTropicraft.tropicsOcean) {
                    btop = biome.sandBlock;
                    sandMetadata = 0;
                }
                for (int l2 = 127; l2 >= 0; --l2) {
                    final int i2 = i1 * 256 * 16 | l * 256 | l2;
                    final Block block = blocks[i2];
                    if (l2 <= 0) {
                        blocks[i2] = Blocks.bedrock;
                    } else if (block == Blocks.air || block == TCBlockRegistry.tropicsWater) {
                        a = 0;
                    } else if (a >= 0 && a < 5) {
                        Block blockUsed = Blocks.stone;
                        if (a == 0 && l2 < 66) {
                            flag = true;
                        }
                        if (flag) {
                            if (a < 5) {
                                blockUsed = btop;
                            }
                        } else if (top != Blocks.sand) {
                            if (a == 0) {
                                blockUsed = top;
                            } else if (a < 5) {
                                blockUsed = filler;
                            }
                        }
                        blocks[i2] = blockUsed;
                        metas[i2] = (byte) sandMetadata;
                        ++a;
                    } else {
                        flag = false;
                        a = -1;
                    }
                }
                a = -1;
            }
        }
    }

    public void populate(final IChunkProvider par1IChunkProvider, final int i, final int j) {
        final ChunkCoordIntPair key = new ChunkCoordIntPair(i, j);
        if (!this.isAreaLoaded(i, j, DECOR_RADIUS)) {
            // Defer decoration until the whole neighborhood this chunk's features can touch is generated.
            // This is what prevents cascading chunk generation without clipping any worldgen.
            this.pendingDecoration.add(key);
            return;
        }
        this.pendingDecoration.remove(key);
        this.doPopulate(i, j);
        this.flushPending();
    }

    private void doPopulate(final int i, final int j) {
        ++this.decoratingDepth;
        try {
            BlockSand.fallInstantly = true;
            final int x = i * 16;
            final int z = j * 16;
            final BiomeGenTropicraft biome = (BiomeGenTropicraft) this.worldObj.getWorldChunkManager()
                .getBiomeGenAt(x, z);
            this.rand.setSeed(this.worldObj.getSeed());
            final long l1 = this.rand.nextLong() / 2L * 2L + 1L;
            final long l2 = this.rand.nextLong() / 2L * 2L + 1L;
            this.rand.setSeed(i * l1 + j * l2 ^ this.worldObj.getSeed());
            biome.decorate(this.worldObj, this.rand, x, z);
            this.generateOres(x, z);
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, biome, x + 8, z + 8, 16, 16, this.rand);
            BlockSand.fallInstantly = false;
            this.worldObj.getChunkFromChunkCoords(i, j)
                .setChunkModified();
        } finally {
            --this.decoratingDepth;
        }
    }

    private boolean isAreaLoaded(final int cx, final int cz, final int radius) {
        final IChunkProvider provider = this.worldObj.getChunkProvider();
        for (int dx = -radius; dx <= radius; ++dx) {
            for (int dz = -radius; dz <= radius; ++dz) {
                if (!provider.chunkExists(cx + dx, cz + dz)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void flushPending() {
        boolean progress;
        do {
            progress = false;
            for (final ChunkCoordIntPair key : new ArrayList<>(this.pendingDecoration)) {
                if (this.isAreaLoaded(key.chunkXPos, key.chunkZPos, DECOR_RADIUS)) {
                    this.pendingDecoration.remove(key);
                    this.doPopulate(key.chunkXPos, key.chunkZPos);
                    progress = true;
                }
            }
        } while (progress);
    }

    public void generateOres(final int x, final int z) {
        this.genStandardOre1(19, this.coalGen, 0, 128, x, z);
        this.genStandardOre1(10, this.ironGen, 0, 64, x, z);
        this.genStandardOre1(15, this.zirconGen, 0, 32, x, z);
        this.genStandardOre1(20, this.eudialyteGen, 0, 64, x, z);
        this.genStandardOre1(10, this.azuriteGen, 0, 128, x, z);
        this.genStandardOre2(1, this.lapisGen, 16, 16, x, z);
    }

    public void genStandardOre1(final int i, final WorldGenerator worldgenerator, final int j, final int k, final int x,
        final int z) {
        for (int l = 0; l < i; ++l) {
            final int i2 = x + this.rand.nextInt(16);
            final int j2 = this.rand.nextInt(k - j) + j;
            final int k2 = z + this.rand.nextInt(16);
            worldgenerator.generate(this.worldObj, this.rand, i2, j2, k2);
        }
    }

    public void genStandardOre2(final int i, final WorldGenerator worldgenerator, final int j, final int k, final int x,
        final int z) {
        for (int l = 0; l < i; ++l) {
            final int i2 = x + this.rand.nextInt(16);
            final int j2 = this.rand.nextInt(k) + this.rand.nextInt(k) + (j - k);
            final int k2 = z + this.rand.nextInt(16);
            worldgenerator.generate(this.worldObj, this.rand, i2, j2, k2);
        }
    }

    int getTerrainHeightAt(final int x, final int z) {
        for (int y = 256; y > 0; --y) {
            final Block block = this.worldObj.getBlock(x, y, z);
            if (block == Blocks.dirt || block == Blocks.grass || block == Blocks.sand || block == Blocks.stone) {
                return y + 1;
            }
        }
        return 0;
    }

    public String makeString() {
        return "TropiLevelSource";
    }

    public boolean chunkExists(final int x, final int z) {
        return true;
    }

    public Chunk loadChunk(final int x, final int z) {
        return this.provideChunk(x, z);
    }

    public boolean saveChunks(final boolean flag, final IProgressUpdate iprogressupdate) {
        return true;
    }

    public boolean unloadQueuedChunks() {
        if (!this.pendingDecoration.isEmpty() && this.worldObj.getTotalWorldTime() % 200L == 0L) {
            System.out.println(
                "[Tropicraft] deferred decoration pending: " + this.pendingDecoration.size()
                    + " chunks, cascades detected: "
                    + this.cascadesDetected);
        }
        if (this.worldObj instanceof WorldServer && ((WorldServer) this.worldObj).levelSaving) {
            // World is saving/unloading: decorate everything still pending so no chunk is ever saved
            // with terrain but without its decorations.
            this.flushAllPendingForSave();
        } else {
            // Normal tick: decorate any pending chunk whose neighborhood has finished generating.
            this.flushPending();
        }
        return false;
    }

    private void flushAllPendingForSave() {
        if (this.pendingDecoration.isEmpty()) {
            return;
        }
        final IChunkProvider provider = this.worldObj.getChunkProvider();
        if (!(provider instanceof ChunkProviderServer)) {
            this.flushPending();
            return;
        }
        final ChunkProviderServer serverProvider = (ChunkProviderServer) provider;
        for (final ChunkCoordIntPair key : new ArrayList<>(this.pendingDecoration)) {
            this.pendingDecoration.remove(key);
            // Force-generate the decoration neighborhood as terrain-only chunks (no populate), then decorate.
            // The terrain-only ring chunks are saved unpopulated and will be decorated normally on next load.
            for (int dx = -DECOR_RADIUS; dx <= DECOR_RADIUS; ++dx) {
                for (int dz = -DECOR_RADIUS; dz <= DECOR_RADIUS; ++dz) {
                    final int nx = key.chunkXPos + dx;
                    final int nz = key.chunkZPos + dz;
                    if (!serverProvider.chunkExists(nx, nz)) {
                        final Chunk chunk = this.provideChunk(nx, nz);
                        serverProvider.loadedChunkHashMap.add(ChunkCoordIntPair.chunkXZ2Int(nx, nz), chunk);
                        serverProvider.loadedChunks.add(chunk);
                        chunk.onChunkLoad();
                    }
                }
            }
            this.doPopulate(key.chunkXPos, key.chunkZPos);
        }
    }

    public boolean canSave() {
        return true;
    }

    public List getPossibleCreatures(final EnumCreatureType par1EnumCreatureType, final int par2, final int par3,
        final int par4) {
        final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(par2, par4);
        return (biomegenbase == null) ? null : biomegenbase.getSpawnableList(par1EnumCreatureType);
    }

    public ChunkPosition func_147416_a(final World world, final String s, final int i, final int j, final int k) {
        return null;
    }

    public int getLoadedChunkCount() {
        return 0;
    }

    public void recreateStructures(final int i, final int j) {}

    public void saveExtraData() {}
}
