package com.chinoll.codemagic.core.items;

import com.chinoll.codemagic.CodeMagic;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = CodeMagic.MODID)
public class ItemRegister {
    public static wandItem[] wand_item;
    public static ItemStack[] wand_stack;
    private static final String[] wand_name = {"red_wand","blue_wand","orange_wand","purple_wand","green_wand"};
    private static final double[] magic_effect = {0.1,0.2,0.3,0.4,0.5};
    public static final KeyBinding MY_HOTKEY = new KeyBinding("key.codemagic.hotkey_1", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_F6, "key.category.codemagic");
    public static CreativeTabs CodeMagicTab = new CreativeTabs("codemagic") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.DIAMOND);
        }
    };
    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        wand_item = new wandItem[wand_name.length];
        wand_stack = new ItemStack[wand_name.length];
        for (int i = 0;i < wand_name.length;i++) {
            wand_item[i] = (wandItem) new wandItem()
                    .setHasSubtypes(true)
                    .setUnlocalizedName(CodeMagic.MODID + "." + wand_name[i])
                    .setCreativeTab(CodeMagicTab)
                    .setRegistryName(CodeMagic.MODID  + ":" + wand_name[i]);
            wand_item[i].setMagicEffect(magic_effect[i]);
            wand_stack[i] = new ItemStack(wand_item[i]);
            event.getRegistry().register(wand_stack[i].getItem());
        }
    }
}
