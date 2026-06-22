package net.tropicraft.entity.koa;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.ai.jobs.JobQuestGiver;
import net.tropicraft.registry.TCItemRegistry;

import CoroUtil.componentAI.jobSystem.JobBase;

public class EntityKoaShaman extends EntityKoaBase {

    public EntityKoaShaman(final World par1World) {
        super(par1World);
        this.agent.jobMan.addJob((JobBase) new JobQuestGiver(this.agent.jobMan));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.agent.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(60.0);
    }

    public int getCooldownRanged() {
        return 10;
    }

    public IEntityLivingData onSpawnWithEgg(final IEntityLivingData par1EntityLivingData) {
        this.agent.entInv.setSlotContents(0, new ItemStack(TCItemRegistry.bambooSpear));
        this.agent.entInv.setSlotContents(1, new ItemStack((Item) TCItemRegistry.swordZirconium));
        this.agent.entInv.syncToClient();
        return super.onSpawnWithEgg(par1EntityLivingData);
    }
}
