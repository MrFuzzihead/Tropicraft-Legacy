package net.tropicraft.proxy;

import net.minecraft.client.model.ModelBiped;

public interface ISuperProxy {

    void registerBooks();

    void initRenderHandlersAndIDs();

    void initRenderRegistry();

    ModelBiped getArmorModel(final int p0);

    void preInit();
}
