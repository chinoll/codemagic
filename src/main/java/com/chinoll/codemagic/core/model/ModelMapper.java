package com.chinoll.codemagic.core.model;

import com.chinoll.codemagic.CodeMagic;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import com.chinoll.codemagic.core.items.ItemRegister;
import java.util.Objects;
//import org.antlr.v4.runtime.ParserRuleContext;

@Mod.EventBusSubscriber(value = Side.CLIENT,modid = CodeMagic.MODID)
public class ModelMapper {
    @SubscribeEvent
    public static void onRegisterModel(ModelRegistryEvent event) {
//        ParserRuleContext ctx = new ParserRuleContext();
//        System.out.println(ctx);
        for(ItemStack stack:ItemRegister.wand_stack)
            Register(stack.getItem());
    }
    private static void Register(Item item) {
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),"inventory"));
    }
}
