package com.chinoll.codemagic.core.items;

import com.chinoll.codemagic.CodeMagic;
import com.chinoll.codemagic.gui.GuiHanler;
import com.chinoll.codemagic.magic.scriptEngine;
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
//import com.chinoll.codemagic.magic.scriptEngine;


//@Mod.EventBusSubscriber(modid = CodeMagic.MODID)
public class wandItem extends Item {
    private double magic_effect;
    private Logger logger = LogManager.getLogger();
    public wandItem setMagicEffect(double effect) {
        magic_effect = effect;
        return this;
    }

    protected String cleanCode(String code) {
        //过滤循环
        while(code.matches("[for]|[while]|[repeat]|[until]")) {
            code = code.replace("for","")
                    .replace("while","")
                    .replace("repeat","")
                    .replace("until","");
        }
        return code;
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
            CodeMagic.scriptQueue.add(new scriptEngine(cleanCode(NBT.getString("source code")), player.getUniqueID()));
        }
        return true;
    }
    public void updateNBT(EntityPlayer player,ItemStack itemstack,NBTTagCompound NBT) {
        itemstack.setTagInfo("filename",NBT);
    }
}