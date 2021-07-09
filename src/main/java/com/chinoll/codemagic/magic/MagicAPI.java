package com.chinoll.codemagic.magic;

import com.chinoll.codemagic.CodeMagic;
import com.chinoll.codemagic.network.packetClientCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyModifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.lwjgl.Sys;

import java.io.IOError;

public class MagicAPI {
    private Logger logger = LogManager.getLogger();
    public MagicAPI() {
    }
    public LuaValue call(LuaValue modname, LuaValue env) {
        return env;
    }

    public static class snowball extends ThreeArgFunction {
        public World world;
        public EntityLivingBase player;
        private Logger logger = LogManager.getLogger();

        public snowball(World world, EntityLivingBase player) {
            this.world = world;
            this.player = player;
        }

        public LuaValue call(LuaValue a,LuaValue b,LuaValue c) {
            if (!this.world.isRemote) {
                EntitySnowball ball = new EntitySnowball(world, player);
                try {
                    ball.shoot(player, player.rotationPitch, player.rotationYaw, (float) a.checkdouble(), (float) b.checkdouble(), (float) c.checkdouble());
                    world.spawnEntity(ball);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
