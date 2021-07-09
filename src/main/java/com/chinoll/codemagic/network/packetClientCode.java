package com.chinoll.codemagic.network;

import com.chinoll.codemagic.gui.CMContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class packetClientCode implements IMessage {
    public NBTTagCompound NBT;
    public packetClientCode() {
    }
    public packetClientCode(NBTTagCompound nbt) {
        NBT = nbt;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        NBT = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeTag(byteBuf,NBT);
    }
//    @SideOnly(Side.SERVER)
    public static class ServerHandler implements IMessageHandler<packetClientCode,IMessage> {
        private Logger logger = LogManager.getLogger();

        @Override
        public IMessage onMessage(packetClientCode message, MessageContext ctx) {
            String filename = message.NBT.getString("filename");
            EntityPlayer player = Minecraft.getMinecraft().player;
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        ItemStack item = player.getActiveItemStack();
                        item.setTagInfo("filename", message.NBT);
                        logger.info("nbt filename:{}",message.NBT.getString("filename"));
                        ((CMContainer)player.openContainer).updateNBT(player,item,message.NBT);
                    }
                });
            return null;
        }
    }
    public static class ClientHandler implements IMessageHandler<packetClientCode,IMessage> {
        private Logger logger = LogManager.getLogger();

        @Override
        public IMessage onMessage(packetClientCode message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                String filename = message.NBT.getString("filename");
                logger.info("update nbt!\nfilename:{}",filename);
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                      @Override
                      public void run() {
                          EntityPlayer player = Minecraft.getMinecraft().player;
                          ItemStack item = player.getActiveItemStack();
                          item.setTagInfo("filename", message.NBT);
                      }});
            }
            return null;
        }
    }
}
