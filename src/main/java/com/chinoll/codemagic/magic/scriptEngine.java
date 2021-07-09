package com.chinoll.codemagic.magic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.UUID;

public class scriptEngine {
    private final String code;
    private final Globals global;
    private final UUID uuid;
    public scriptEngine(String code,UUID uuid) {
        this.code = code;
        this.global = JsePlatform.standardGlobals();
        this.uuid = uuid;
    }

    private void setEnv(EntityPlayer player, World world) {
        this.global.set("snowball",new MagicAPI.snowball(world,player));

    }

    public void executeScript(EntityPlayer player,World world) {
        setEnv(player,world);
        LuaValue chunk = this.global.load(this.code);
        chunk.call();
    }
    public UUID getUUID() {
        return this.uuid;
    }
}
