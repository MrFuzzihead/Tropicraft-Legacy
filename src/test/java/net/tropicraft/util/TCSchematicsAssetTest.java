package net.tropicraft.util;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.Assert;
import org.junit.Test;

/**
 * Guards the shipped Koa village schematic.
 * <p>
 * The village only ever generated nothing because this file was not on the classpath and CoroUtil's {@code Build}
 * swallows the resulting exception - a failure that is invisible in a build and only shows up as a beach with no
 * village in it. These assertions make the asset, its format and its block translations build-time facts:
 * <ul>
 * <li>the resource is where {@link TCSchematics} looks for it,</li>
 * <li>it is a CoroUtil "newFormat" version 1.1 build, which is the only digest {@code Build.readNBT} parses here,</li>
 * <li>the block/data arrays are the size the declared dimensions imply, and</li>
 * <li>every block id the schematic actually uses has an entry in its {@code blockTranslationMap}.</li>
 * </ul>
 */
public class TCSchematicsAssetTest {

    private static final String NAME = "koavillage";
    private static final String ASSET = TCSchematics.ASSET_PATH + NAME + TCSchematics.EXTENSION;

    private static final int WIDTH = 86;
    private static final int HEIGHT = 16;
    private static final int LENGTH = 76;

    private static NBTTagCompound read() throws Exception {
        final InputStream in = TCSchematicsAssetTest.class.getResourceAsStream(ASSET);
        Assert.assertNotNull("missing classpath resource " + ASSET, in);
        try {
            return CompressedStreamTools.readCompressed(in);
        } finally {
            in.close();
        }
    }

    @Test
    public void theVillageSchematicShipsOnTheClasspath() {
        Assert.assertNotNull("missing classpath resource " + ASSET, TCSchematicsAssetTest.class.getResource(ASSET));
    }

    @Test
    public void theVillageSchematicIsACoroUtilNewFormatBuild() throws Exception {
        final NBTTagCompound nbt = read();
        Assert
            .assertTrue("newFormat is what makes Build read int arrays instead of bytes", nbt.getBoolean("newFormat"));
        Assert.assertEquals(
            "only version 1.1 resolves block names through the registry",
            "1.1",
            nbt.getString("version"));
        Assert.assertEquals("Width", WIDTH, nbt.getShort("Width"));
        Assert.assertEquals("Height", HEIGHT, nbt.getShort("Height"));
        Assert.assertEquals("Length", LENGTH, nbt.getShort("Length"));
    }

    @Test
    public void theVillageSchematicMatchesTheAreaTheGenHelperScans() throws Exception {
        // TownKoaVillageGenHelper picks the site using these dimensions; if the asset and the scan disagree the
        // village is placed over terrain that was never checked for water or flatness.
        Assert.assertEquals(WIDTH, net.tropicraft.world.location.TownKoaVillageGenHelper.areaWidth);
        Assert.assertEquals(HEIGHT, net.tropicraft.world.location.TownKoaVillageGenHelper.areaHeight);
        Assert.assertEquals(LENGTH, net.tropicraft.world.location.TownKoaVillageGenHelper.areaLength);
    }

    @Test
    public void theBlockAndDataArraysMatchTheDeclaredDimensions() throws Exception {
        final NBTTagCompound nbt = read();
        final int expected = WIDTH * HEIGHT * LENGTH;
        Assert.assertEquals("Blocks", expected, nbt.getIntArray("Blocks").length);
        Assert.assertEquals("Data", expected, nbt.getIntArray("Data").length);
    }

    @Test
    public void everyBlockIdTheSchematicUsesHasATranslation() throws Exception {
        final NBTTagCompound nbt = read();
        final NBTTagCompound translations = nbt.getCompoundTag("blockTranslationMap");
        final Set<Integer> mapped = new HashSet<Integer>();
        for (final Object key : translations.func_150296_c()) {
            mapped.add(translations.getInteger((String) key));
        }
        final Set<Integer> used = new HashSet<Integer>();
        for (final int id : nbt.getIntArray("Blocks")) {
            used.add(id);
        }
        Assert.assertFalse("the schematic is empty", used.isEmpty());
        for (final int id : used) {
            Assert.assertTrue(
                "block id " + id + " is used but has no blockTranslationMap entry, so it builds as air",
                mapped.contains(id));
        }
    }

    /**
     * Tropicraft registers blocks as {@code GameRegistry.registerBlock(block, "tile." + name)} and then sets the
     * unlocalized name separately, so the <em>registry</em> name keeps a {@code tile.} prefix. {@code
     * CoroUtilBlock.getNameByBlock} resolves through the registry, which is why the map keys look the way they do.
     * Renaming them to {@code tropicraft:chunk} (the obvious "cleanup") silently turns every Koa block into air.
     */
    @Test
    public void tropicraftEntriesUseTheRegistryPrefixTheModActuallyRegisters() throws Exception {
        final NBTTagCompound translations = read().getCompoundTag("blockTranslationMap");
        for (final Object key : translations.func_150296_c()) {
            final String name = (String) key;
            if (name.startsWith("tropicraft:")) {
                Assert.assertTrue(
                    "Tropicraft's registry names carry a 'tile.' prefix (see TCBlockRegistry.registerBlock), so '"
                        + name
                        + "' will never resolve at runtime",
                    name.startsWith("tropicraft:tile."));
            }
        }
    }
}
