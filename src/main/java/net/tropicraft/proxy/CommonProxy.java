package net.tropicraft.proxy;

import net.minecraft.client.model.ModelBiped;

public abstract class CommonProxy implements ISuperProxy {

    @Override
    public void registerBooks() {}

    @Override
    public void initRenderHandlersAndIDs() {}

    @Override
    public void initRenderRegistry() {}

    @Override
    public ModelBiped getArmorModel(final int id) {
        return null;
    }

    @Override
    public void preInit() {}
}
