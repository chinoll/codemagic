package com.chinoll.codemagic.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class CMContainer extends Container {

    public CMContainer() {
        super();
    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public void updateNBT(EntityPlayer player,ItemStack itemstack,NBTTagCompound NBT) {
        itemstack.setTagInfo("filename",NBT);
    }
}
