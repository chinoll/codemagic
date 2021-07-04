package com.chinoll.codemagic.gui;

import com.chinoll.codemagic.CodeMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiHanler implements IGuiHandler {
    public static final int GUI_DEMO = 1;
    public static Logger logger = LogManager.getLogger();
    public GuiHanler() {
        logger.info("init gui");
        NetworkRegistry.INSTANCE.registerGuiHandler(CodeMagic.instance,this);
    }
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,int x,int y,int z) {
        switch (ID) {
            case GUI_DEMO:
                return new CMContainer();
            default:
                return null;
        }
    }
    @Override
    public Object getClientGuiElement(int ID,EntityPlayer player,World world,int x,int y,int z) {
        switch (ID) {
            case GUI_DEMO:
                return new CMGuiContainer(new CMContainer());
            default:
                return null;
        }
    }
}
