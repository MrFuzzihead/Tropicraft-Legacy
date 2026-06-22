package net.tropicraft.entity.koa;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

public class EntityKoaHunter extends EntityKoaBase {

    public EntityKoaHunter(final World par1World) {
        super(par1World);
    }

    public int getCooldownRanged() {
        return 10;
    }

    public IEntityLivingData onSpawnWithEgg(final IEntityLivingData par1EntityLivingData) {
        this.agent.entInv.setSlotContents(0, new ItemStack(TCItemRegistry.dagger));
        this.agent.entInv.setSlotContents(1, new ItemStack(TCItemRegistry.leafBall));
        this.agent.entInv.syncToClient();
        return super.onSpawnWithEgg(par1EntityLivingData);
    }
}
