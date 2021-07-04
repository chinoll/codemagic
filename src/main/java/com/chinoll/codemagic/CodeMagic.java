package com.chinoll.codemagic;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import com.chinoll.codemagic.proxy.CommonProxy;
import com.chinoll.codemagic.network.packetClientCode;
//import com.chinoll.codemagic.network.packetHandler;
@Mod(modid = CodeMagic.MODID, name = CodeMagic.NAME, version = CodeMagic.VERSION)
public class CodeMagic
{

    @Mod.Instance(CodeMagic.MODID)
    public static CodeMagic instance;

    public static final String MODID = "codemagic";
    public static final String NAME = "CodeMagic Mod";
    public static final String VERSION = "0.0.1";
    private SimpleNetworkWrapper network;
    private static Logger logger;
        @SidedProxy(
            clientSide = "com.chinoll.codemagic.proxy.ClientProxy",
            serverSide = "com.chinoll.codemagic.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(new packetClientCode.ClientHandler(), packetClientCode.class,1, Side.SERVER);
        network.registerMessage(new packetClientCode.ServerHandler(), packetClientCode.class,0, Side.SERVER);
        proxy.preInit(event);
    }
    public static SimpleNetworkWrapper get_network() {
        return instance.network;
    }
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.Init(event);
    }
}
