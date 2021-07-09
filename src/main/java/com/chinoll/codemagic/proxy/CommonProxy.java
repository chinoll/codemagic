package com.chinoll.codemagic.proxy;
import com.chinoll.codemagic.gui.GuiHanler;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.chinoll.codemagic.core.items.eventRegister;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event){
        new eventRegister();
        new GuiHanler();

    }
    public void Init(FMLInitializationEvent event){
    }
    public IThreadListener getThreadListener(MessageContext context) {
        if (context.side.isServer())
            return context.getServerHandler().player.mcServer;
        else
            return null;
    }
}


