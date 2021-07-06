package com.chinoll.codemagic.core.items;

import com.chinoll.codemagic.CodeMagic;
import com.chinoll.codemagic.gui.GuiHanler;
import com.chinoll.codemagic.magic.Snowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

import java.util.regex.Pattern;

//@Mod.EventBusSubscriber(modid = CodeMagic.MODID)
public class wandItem extends Item {
    private double magic_effect;
    private Logger logger = LogManager.getLogger();
    public wandItem setMagicEffect(double effect) {
        magic_effect = effect;
        return this;
    }

    protected String clean_code(String code) {
        //过滤循环
//        Pattern p = Pattern.compile("[for]|[while]|[repeat]|[until]");
        while(code.matches("[for]|[while]|[repeat]|[until]")) {
            code = code.replace("for","")
                    .replace("while","")
                    .replace("repeat","")
                    .replace("until","");
        }
        return code;
    }
    protected void runScript(EntityLivingBase player,String code) {
        Globals globals = JsePlatform.standardGlobals();
        globals.set("snowball",new Snowball.snowball(player.world,player));
        code = clean_code(code);
        LuaValue chunk = globals.load(code);
        chunk.call();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);

        if(!world.isRemote) {
            BlockPos pos = player.getPosition();
            int id = GuiHanler.GUI_DEMO;

            player.openGui(CodeMagic.instance,id,world,pos.getX(),pos.getY(),pos.getZ());
        }
        return new ActionResult<>(EnumActionResult.SUCCESS,item);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase player, ItemStack itemstack) {
        if (!player.world.isRemote) {
            NBTTagCompound NBT = player.getActiveItemStack().getOrCreateSubCompound("filename");
            try {
                runScript(player, NBT.getString("source code"));
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        return true;
    }
    public void updateNBT(EntityPlayer player,ItemStack itemstack,NBTTagCompound NBT) {
        itemstack.setTagInfo("filename",NBT);
    }
}