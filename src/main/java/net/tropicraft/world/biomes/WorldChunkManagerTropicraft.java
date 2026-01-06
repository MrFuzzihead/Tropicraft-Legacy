package net.tropicraft.world.biomes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.crash.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.layer.*;
import net.tropicraft.world.genlayer.*;

public class WorldChunkManagerTropicraft extends WorldChunkManager {

    public static final List<BiomeGenBase> allowedBiomes;
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private final BiomeCache biomeCache;
    private final List<BiomeGenBase> biomesToSpawnIn;

    protected WorldChunkManagerTropicraft() {
        this.biomeCache = new BiomeCache((WorldChunkManager) this);
        (this.biomesToSpawnIn = new ArrayList<BiomeGenBase>()).addAll(WorldChunkManagerTropicraft.allowedBiomes);
    }

    public WorldChunkManagerTropicraft(final long seed, final WorldType worldType) {
        this();
        final GenLayer[] agenlayer = GenLayerTropicraft.initializeAllBiomeGenerators(seed, worldType);
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
    }

    public WorldChunkManagerTropicraft(final World world) {
        this(
            world.getSeed(),
            world.getWorldInfo()
                .getTerrainType());
    }

    public List<BiomeGenBase> getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    public BiomeGenBase getBiomeGenAt(final int par1, final int par2) {
        return this.biomeCache.getBiomeGenAt(par1, par2);
    }

    public float[] getRainfall(float[] par1ArrayOfFloat, final int par2, final int par3, final int par4,
        final int par5) {
        IntCache.resetIntCache();
        if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5) {
            par1ArrayOfFloat = new float[par4 * par5];
        }
        final int[] aint = this.biomeIndexLayer.getInts(par2, par3, par4, par5);
        for (int i1 = 0; i1 < par4 * par5; ++i1) {
            try {
                float f = BiomeGenBase.getBiome(aint[i1])
                    .getIntRainfall() / 65536.0f;
                if (f > 1.0f) {
                    f = 1.0f;
                }
                par1ArrayOfFloat[i1] = f;
            } catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
                crashreportcategory.addCrashSection("biome id", (Object) i1);
                crashreportcategory.addCrashSection("downfalls[] size", (Object) par1ArrayOfFloat.length);
                crashreportcategory.addCrashSection("x", (Object) par2);
                crashreportcategory.addCrashSection("z", (Object) par3);
                crashreportcategory.addCrashSection("w", (Object) par4);
                crashreportcategory.addCrashSection("h", (Object) par5);
                throw new ReportedException(crashreport);
            }
        }
        return par1ArrayOfFloat;
    }

    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, final int par2, final int par3,
        final int par4, final int par5) {
        IntCache.resetIntCache();
        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }
        final int[] aint = this.genBiomes.getInts(par2, par3, par4, par5);
        try {
            for (int i1 = 0; i1 < par4 * par5; ++i1) {
                par1ArrayOfBiomeGenBase[i1] = BiomeGenBase.getBiome(aint[i1]);
            }
            return par1ArrayOfBiomeGenBase;
        } catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", (Object) par1ArrayOfBiomeGenBase.length);
            crashreportcategory.addCrashSection("x", (Object) par2);
            crashreportcategory.addCrashSection("z", (Object) par3);
            crashreportcategory.addCrashSection("w", (Object) par4);
            crashreportcategory.addCrashSection("h", (Object) par5);
            throw new ReportedException(crashreport);
        }
    }

    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] par1ArrayOfBiomeGenBase, final int par2, final int par3,
        final int par4, final int par5, final boolean par6) {
        IntCache.resetIntCache();
        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }
        if (par6 && par4 == 16 && par5 == 16 && (par2 & 0xF) == 0x0 && (par3 & 0xF) == 0x0) {
            final BiomeGenBase[] abiomegenbase1 = this.biomeCache.getCachedBiomes(par2, par3);
            System.arraycopy(abiomegenbase1, 0, par1ArrayOfBiomeGenBase, 0, par4 * par5);
            return par1ArrayOfBiomeGenBase;
        }
        final int[] aint = this.biomeIndexLayer.getInts(par2, par3, par4, par5);
        for (int i1 = 0; i1 < par4 * par5; ++i1) {
            par1ArrayOfBiomeGenBase[i1] = BiomeGenBase.getBiome(aint[i1]);
        }
        return par1ArrayOfBiomeGenBase;
    }

    public boolean areBiomesViable(final int par1, final int par2, final int par3, final List par4List) {
        IntCache.resetIntCache();
        final int l = par1 - par3 >> 2;
        final int i1 = par2 - par3 >> 2;
        final int j1 = par1 + par3 >> 2;
        final int k1 = par2 + par3 >> 2;
        final int l2 = j1 - l + 1;
        final int i2 = k1 - i1 + 1;
        final int[] aint = this.genBiomes.getInts(l, i1, l2, i2);
        try {
            for (int j2 = 0; j2 < l2 * i2; ++j2) {
                final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[j2]);
                if (!par4List.contains(biomegenbase)) {
                    return false;
                }
            }
            return true;
        } catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", (Object) this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", (Object) par1);
            crashreportcategory.addCrashSection("z", (Object) par2);
            crashreportcategory.addCrashSection("radius", (Object) par3);
            crashreportcategory.addCrashSection("allowed", (Object) par4List);
            throw new ReportedException(crashreport);
        }
    }

    public ChunkPosition findBiomePosition(final int p_150795_1_, final int p_150795_2_, final int p_150795_3_,
        final List p_150795_4_, final Random p_150795_5_) {
        IntCache.resetIntCache();
        final int l = p_150795_1_ - p_150795_3_ >> 2;
        final int i1 = p_150795_2_ - p_150795_3_ >> 2;
        final int j1 = p_150795_1_ + p_150795_3_ >> 2;
        final int k1 = p_150795_2_ + p_150795_3_ >> 2;
        final int l2 = j1 - l + 1;
        final int i2 = k1 - i1 + 1;
        final int[] aint = this.genBiomes.getInts(l, i1, l2, i2);
        ChunkPosition chunkposition = null;
        int j2 = 0;
        for (int k2 = 0; k2 < l2 * i2; ++k2) {
            final int l3 = l + k2 % l2 << 2;
            final int i3 = i1 + k2 / l2 << 2;
            final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[k2]);
            if (p_150795_4_.contains(biomegenbase) && (chunkposition == null || p_150795_5_.nextInt(j2 + 1) == 0)) {
                chunkposition = new ChunkPosition(l3, 0, i3);
                ++j2;
            }
        }
        return chunkposition;
    }

    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }

    static {
        allowedBiomes = Arrays.asList(BiomeGenTropicraft.tropics, BiomeGenTropicraft.rainforestPlains);
    }
}
