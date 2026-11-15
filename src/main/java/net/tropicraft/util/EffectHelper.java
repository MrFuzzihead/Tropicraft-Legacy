package net.tropicraft.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;

import cpw.mods.fml.relauncher.Side;

public class EffectHelper {

    public static List<EffectEntry> listEntities;

    public static void addEntry(final EntityLivingBase entity, final int effectTime) {
        final EffectEntry entry = new EffectEntry(entity);
        entry.setEffectTime(effectTime);
        EffectHelper.listEntities.add(entry);
    }

    public static void removeEntry(final EntityLivingBase entity) {
        final Iterator<EffectEntry> it = EffectHelper.listEntities.iterator();
        while (it.hasNext()) {
            final EffectEntry entry = it.next();
            if (entry.getEntity() == entity) {
                entry.cleanup();
                it.remove();
            }
        }
    }

    /**
     * Advances every effect that belongs to {@code side}.
     * <p>
     * The list is a process-wide static, and in single player the integrated server and the client live in the same
     * JVM: both the client tick and the server tick used to drain the whole list, so every effect counted down twice
     * per tick - a 5 second paralysis lasted 2.5 seconds in single player but the full 5 on a dedicated server.
     * {@link EffectEntry#tick()} already decides what to do per side from {@code worldObj.isRemote}, so an entry is
     * only advanced by the tick that owns its world.
     * </p>
     */
    public static void tick(final Side side) {
        final Iterator<EffectEntry> it = EffectHelper.listEntities.iterator();
        while (it.hasNext()) {
            final EffectEntry entry = it.next();
            final EntityLivingBase entity = entry.getEntity();
            if (entity == null || entity.isDead || entry.isFinished()) {
                entry.cleanup();
                it.remove();
            } else if (entity.worldObj.isRemote == side.isClient()) {
                entry.tick();
            }
        }
    }

    static {
        EffectHelper.listEntities = new ArrayList<EffectEntry>();
    }
}
