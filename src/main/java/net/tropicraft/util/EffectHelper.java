package net.tropicraft.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;

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

    public static void tick() {
        final Iterator<EffectEntry> it = EffectHelper.listEntities.iterator();
        while (it.hasNext()) {
            final EffectEntry entry = it.next();
            if (entry.getEntity() == null || entry.getEntity().isDead || entry.isFinished()) {
                entry.cleanup();
                it.remove();
            } else {
                entry.tick();
            }
        }
    }

    static {
        EffectHelper.listEntities = new ArrayList<EffectEntry>();
    }
}
