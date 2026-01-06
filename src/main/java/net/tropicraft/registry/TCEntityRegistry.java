package net.tropicraft.registry;

import net.minecraft.entity.*;
import net.tropicraft.*;
import net.tropicraft.entity.*;
import net.tropicraft.entity.hostile.*;
import net.tropicraft.entity.koa.*;
import net.tropicraft.entity.passive.*;
import net.tropicraft.entity.placeable.*;
import net.tropicraft.entity.projectile.*;
import net.tropicraft.entity.underdasea.*;

import CoroUtil.entity.*;
import cpw.mods.fml.common.registry.*;

public class TCEntityRegistry {

    private static int entityID;

    public static void init() {
        registerEntity(EntityChair.class, "beachChair", 120, 10, true);
        registerEntity(EntityUmbrella.class, "beachUmbrella", 120, 10, false);
        registerEntity(EntitySeahorse.class, "Seahorse", 120, 3, true);
        registerEntity(EntityDart.class, "Dart", 120, 4, true);
        registerEntity(EntityIguana.class, "Iguana", 80, 3, true);
        registerEntity(EntityEIH.class, "Easter Island Head", 80, 3, true);
        registerEntity(EntityTreeFrogGreen.class, "TreeFrogGreen", 80, 3, true);
        registerEntity(EntityTreeFrogRed.class, "TreeFrogRed", 80, 3, true);
        registerEntity(EntityTreeFrogBlue.class, "TreeFrogBlue", 80, 3, true);
        registerEntity(EntityTreeFrogYellow.class, "TreeFrogYellow", 80, 3, true);
        registerEntity(EntityPoisonBlot.class, "PoisonBlot", 120, 2, true);
        registerEntity(EntitySeaTurtle.class, "SeaTurtle", 80, 3, true);
        registerEntity(EntityTurtleEgg.class, "SeaTurtleEgg", 80, 5, false);
        registerEntity(EntityEagleRay.class, "EagleRay", 80, 1, true);
        registerEntity(EntityAshenHunter.class, "AshenHunter", 80, 3, true);
        registerEntity(EntityCoconutGrenade.class, "CoconutBomb", 120, 5, true);
        registerEntity(EntityTropiCreeper.class, "TropiCreeper", 80, 3, true);
        registerEntity(EntityTropiSkeleton.class, "TropiSkeleton", 80, 3, true);
        registerEntity(EntityTropiCreeper.class, "TropiCreeper", 80, 3, true);
        registerEntity(SpiderAdult.class, "SpiderAdult", 48, 3, true);
        registerEntity(SpiderChild.class, "SpiderChild", 48, 3, true);
        registerEntity(SpiderEgg.class, "SpiderEgg", 48, 3, true);
        registerEntity(VMonkey.class, "VMonkey", 64, 3, true);
        registerEntity(EntityKoaHunter.class, "KoaHunter", 64, 3, true);
        registerEntity(EntityKoaFisher.class, "KoaFisher", 64, 3, true);
        registerEntity(EntityKoaTrader.class, "KoaTrader", 64, 3, true);
        registerEntity(EntityKoaShaman.class, "KoaShaman", 64, 3, true);
        registerEntity(EntityTropicalFishHook.class, "TropiFishHook", 64, 1, true);
        registerEntity(EntityTCItemFrame.class, "TCItemFrame", 64, 10, false);
        registerEntity(EntityTropicraftLeafballNew.class, "TropiLeafball", 64, 1, true);
        registerEntity(EntityTropicalFish.class, "TropicalFish", 80, 3, true);
        registerEntity(EntityLostMask.class, "LostMask", 64, 3, true);
        registerEntity(EntityMarlin.class, "Marlin", 80, 3, true);
        registerEntity(EntitySnareTrap.class, "SnareTrap", 80, 20, false);
        registerEntity(Failgull.class, "Failgull", 64, 3, true);
        registerEntity(EntityManOWar.class, "MOW", 64, 3, true);
        registerEntity(EntitySeaUrchin.class, "SeaUrchin", 64, 3, true);
        registerEntity(EntitySeaUrchinEgg.class, "SeaUrchinEgg", 64, 3, false);
        registerEntity(EntityStarfish.class, "Starfish", 64, 3, false);
        registerEntity(EntityStarfishEgg.class, "StarfishEgg", 64, 3, false);
        registerEntity(EntityWallStarfish.class, "WallStarfish", 160, Integer.MAX_VALUE, false);
    }

    private static void registerEntity(final Class<? extends Entity> entityClass, final String entityName,
        final int trackingRange, final int updateFrequency, final boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(
            entityClass,
            entityName,
            TCEntityRegistry.entityID++,
            Tropicraft.instance,
            trackingRange,
            updateFrequency,
            sendsVelocityUpdates);
    }

    static {
        TCEntityRegistry.entityID = 0;
    }
}
