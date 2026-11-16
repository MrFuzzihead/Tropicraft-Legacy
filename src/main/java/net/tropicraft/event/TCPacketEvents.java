package net.tropicraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.tropicraft.util.EffectHelper;

import CoroUtil.packet.PacketHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCPacketEvents {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPacketFromServer(final FMLNetworkEvent.ClientCustomPacketEvent event) {
        try {
            final NBTTagCompound nbt = PacketHelper.readNBTTagCompound(event.packet.payload());
            final String packetCommand = nbt.getString("packetCommand");
            if (packetCommand.equals("effectAdd")) {
                EffectHelper
                    .addEntry((EntityLivingBase) Minecraft.getMinecraft().thePlayer, nbt.getInteger("effectTime"));
            } else if (packetCommand.equals("effectRemove")) {
                EffectHelper.removeEntry((EntityLivingBase) Minecraft.getMinecraft().thePlayer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
