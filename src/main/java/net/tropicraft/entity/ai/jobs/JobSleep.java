package net.tropicraft.entity.ai.jobs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.tropicraft.item.tool.ItemTropicraftPickaxe;

import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobSleep extends JobBase {

    public boolean sleeping;

    public JobSleep(final JobManager jm) {
        super(jm);
        this.sleeping = true;
    }

    public boolean shouldExecute() {
        return true;
    }

    public boolean shouldContinue() {
        return !this.sleeping;
    }

    public boolean hookHit(final DamageSource ds, final int damage) {
        if (ds.getEntity() instanceof EntityPlayer) {
            this.sleeping = false;
            final ItemStack is = ((EntityPlayer) ds.getEntity()).getCurrentEquippedItem();
            return is != null && (is.getItem() instanceof ItemPickaxe || is.getItem() instanceof ItemTropicraftPickaxe);
        }
        return false;
    }

    public void onIdleTickAct() {
        if (!this.sleeping) {
            super.onIdleTickAct();
        }
    }
}
