package net.tropicraft.registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.tropicraft.Tropicraft;
import net.tropicraft.entity.EntityTCItemFrame;
import net.tropicraft.entity.hostile.EntityAshenHunter;
import net.tropicraft.entity.hostile.EntityEIH;
import net.tropicraft.entity.hostile.EntityLostMask;
import net.tropicraft.entity.hostile.EntityTreeFrogBlue;
import net.tropicraft.entity.hostile.EntityTreeFrogRed;
import net.tropicraft.entity.hostile.EntityTreeFrogYellow;
import net.tropicraft.entity.hostile.EntityTropiCreeper;
import net.tropicraft.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.entity.hostile.SpiderAdult;
import net.tropicraft.entity.hostile.SpiderChild;
import net.tropicraft.entity.hostile.SpiderEgg;
import net.tropicraft.entity.koa.EntityKoaFisher;
import net.tropicraft.entity.koa.EntityKoaHunter;
import net.tropicraft.entity.koa.EntityKoaShaman;
import net.tropicraft.entity.koa.EntityKoaTrader;
import net.tropicraft.entity.passive.EntityIguana;
import net.tropicraft.entity.passive.EntityTreeFrogGreen;
import net.tropicraft.entity.passive.Failgull;
import net.tropicraft.entity.passive.VMonkey;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.entity.placeable.EntitySnareTrap;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.entity.placeable.EntityWallStarfish;
import net.tropicraft.entity.projectile.EntityCoconutGrenade;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.entity.projectile.EntityPoisonBlot;
import net.tropicraft.entity.projectile.EntityTropicraftLeafballNew;
import net.tropicraft.entity.underdasea.EntityEagleRay;
import net.tropicraft.entity.underdasea.EntityManOWar;
import net.tropicraft.entity.underdasea.EntityMarlin;
import net.tropicraft.entity.underdasea.EntitySeaTurtle;
import net.tropicraft.entity.underdasea.EntitySeaUrchin;
import net.tropicraft.entity.underdasea.EntitySeaUrchinEgg;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import net.tropicraft.entity.underdasea.EntityStarfish;
import net.tropicraft.entity.underdasea.EntityStarfishEgg;
import net.tropicraft.entity.underdasea.EntityTropicalFish;
import net.tropicraft.entity.underdasea.EntityTurtleEgg;

import CoroUtil.entity.EntityTropicalFishHook;
import cpw.mods.fml.common.registry.EntityRegistry;

public class TCEntityRegistry {

    /** Entity class -> network id, in registration order. Exposed so the in-game test can assert the contract. */
    private static final Map<Class<? extends Entity>, Integer> REGISTERED = new LinkedHashMap<Class<? extends Entity>, Integer>();

    public static Map<Class<? extends Entity>, Integer> getRegistered() {
        return Collections.unmodifiableMap(REGISTERED);
    }

    public static void init() {
        registerEntity(EntityChair.class, 0, "beachChair", 120, 10, true);
        registerEntity(EntityUmbrella.class, 1, "beachUmbrella", 120, 10, false);
        registerEntity(EntitySeahorse.class, 2, "Seahorse", 120, 3, true);
        registerEntity(EntityDart.class, 3, "Dart", 120, 4, true);
        registerEntity(EntityIguana.class, 4, "Iguana", 80, 3, true);
        registerEntity(EntityEIH.class, 5, "Easter Island Head", 80, 3, true);
        registerEntity(EntityTreeFrogGreen.class, 6, "TreeFrogGreen", 80, 3, true);
        registerEntity(EntityTreeFrogRed.class, 7, "TreeFrogRed", 80, 3, true);
        registerEntity(EntityTreeFrogBlue.class, 8, "TreeFrogBlue", 80, 3, true);
        registerEntity(EntityTreeFrogYellow.class, 9, "TreeFrogYellow", 80, 3, true);
        registerEntity(EntityPoisonBlot.class, 10, "PoisonBlot", 120, 2, true);
        registerEntity(EntitySeaTurtle.class, 11, "SeaTurtle", 80, 3, true);
        registerEntity(EntityTurtleEgg.class, 12, "SeaTurtleEgg", 80, 5, false);
        registerEntity(EntityEagleRay.class, 13, "EagleRay", 80, 1, true);
        registerEntity(EntityAshenHunter.class, 14, "AshenHunter", 80, 3, true);
        registerEntity(EntityCoconutGrenade.class, 15, "CoconutBomb", 120, 5, true);
        registerEntity(EntityTropiCreeper.class, 16, "TropiCreeper", 80, 3, true);
        registerEntity(EntityTropiSkeleton.class, 17, "TropiSkeleton", 80, 3, true);
        // id 18 is unused: it was burned by a duplicate EntityTropiCreeper registration that FML
        // silently rejected (the duplicate line is gone, the id is not reused - see the note above init()).
        registerEntity(SpiderAdult.class, 19, "SpiderAdult", 48, 3, true);
        registerEntity(SpiderChild.class, 20, "SpiderChild", 48, 3, true);
        registerEntity(SpiderEgg.class, 21, "SpiderEgg", 48, 3, true);
        registerEntity(VMonkey.class, 22, "VMonkey", 64, 3, true);
        registerEntity(EntityKoaHunter.class, 23, "KoaHunter", 64, 3, true);
        registerEntity(EntityKoaFisher.class, 24, "KoaFisher", 64, 3, true);
        registerEntity(EntityKoaTrader.class, 25, "KoaTrader", 64, 3, true);
        registerEntity(EntityKoaShaman.class, 26, "KoaShaman", 64, 3, true);
        registerEntity(EntityTropicalFishHook.class, 27, "TropiFishHook", 64, 1, true);
        registerEntity(EntityTCItemFrame.class, 28, "TCItemFrame", 64, 10, false);
        registerEntity(EntityTropicraftLeafballNew.class, 29, "TropiLeafball", 64, 1, true);
        registerEntity(EntityTropicalFish.class, 30, "TropicalFish", 80, 3, true);
        registerEntity(EntityLostMask.class, 31, "LostMask", 64, 3, true);
        registerEntity(EntityMarlin.class, 32, "Marlin", 80, 3, true);
        registerEntity(EntitySnareTrap.class, 33, "SnareTrap", 80, 20, false);
        registerEntity(Failgull.class, 34, "Failgull", 64, 3, true);
        registerEntity(EntityManOWar.class, 35, "MOW", 64, 3, true);
        registerEntity(EntitySeaUrchin.class, 36, "SeaUrchin", 64, 3, true);
        registerEntity(EntitySeaUrchinEgg.class, 37, "SeaUrchinEgg", 64, 3, false);
        registerEntity(EntityStarfish.class, 38, "Starfish", 64, 3, false);
        registerEntity(EntityStarfishEgg.class, 39, "StarfishEgg", 64, 3, false);
        registerEntity(EntityWallStarfish.class, 40, "WallStarfish", 160, Integer.MAX_VALUE, false);
    }

    /**
     * @param entityId the network id FML uses for this entity type. <b>Never change or reuse one.</b> The value
     *                 travels in {@code FMLMessage.EntitySpawnMessage} and is what the client resolves through
     *                 {@code EntityRegistry.lookupModSpawn}, so changing it silently breaks spawning whenever a client
     *                 and a
     *                 server disagree about what an id means - which is exactly what call-order assignment used to do.
     */
    private static void registerEntity(final Class<? extends Entity> entityClass, final int entityId,
        final String entityName, final int trackingRange, final int updateFrequency,
        final boolean sendsVelocityUpdates) {
        if (REGISTERED.put(entityClass, entityId) != null) {
            throw new IllegalStateException(
                "Entity " + entityClass.getName()
                    + " is registered twice; the second registration would be "
                    + "rejected by FML and the id burned silently, which is how id 18 went missing.");
        }
        for (final Map.Entry<Class<? extends Entity>, Integer> entry : REGISTERED.entrySet()) {
            if (entry.getKey() != entityClass && entry.getValue()
                .intValue() == entityId) {
                throw new IllegalStateException(
                    "Entity id " + entityId
                        + " is used by both "
                        + entry.getKey()
                            .getName()
                        + " and "
                        + entityClass.getName());
            }
        }
        EntityRegistry.registerModEntity(
            entityClass,
            entityName,
            entityId,
            Tropicraft.instance,
            trackingRange,
            updateFrequency,
            sendsVelocityUpdates);
    }
}
