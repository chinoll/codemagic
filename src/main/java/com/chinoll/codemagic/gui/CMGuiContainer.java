package com.chinoll.codemagic.gui;

import com.chinoll.codemagic.CodeMagic;
import com.chinoll.codemagic.network.packetClientCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.nio.charset.StandardCharsets;

@SideOnly(Side.CLIENT)
public class CMGuiContainer extends GuiContainer {
    private static Logger logger= LogManager.getLogger();
    private static final String TEXTURE_PATH = CodeMagic.MODID + ":" + "textures/gui/container/gui_demo.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
    public CodeInput inputField;
    public String defaultInputFieldText = "";
    public int curpos = 0;
    public GuiTextField titleFiled;
    private int focus;
    private int offsetX,offsetY;
    private static final int BUTTON_UP = 0;
    private static final int BUTTON_DOWN = 1;
    private static final packetClientCode packet= new packetClientCode(null);
//    protected EntityPlayer player = this.mc.player;
    CMGuiContainer(Container container) {
        super(container);
        this.xSize = 176;
        this.ySize = 133;

    }
    @Override
    public void initGui() {
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.inputField = new CodeInput(this.mc.fontRenderer, offsetX + 32, offsetY + 4, this.xSize - 38, this.ySize - 4);
        this.titleFiled = new GuiTextField(1, this.mc.fontRenderer, offsetX + 3, offsetY + 4, 25, 8);
        this.focus = 1;

        this.titleFiled.setMaxStringLength(32);
        this.titleFiled.setEnableBackgroundDrawing(false);
        this.titleFiled.setText("");
        this.titleFiled.setCanLoseFocus(false);

        this.buttonList.add(new GuiButton(BUTTON_UP, offsetX + 4, offsetY + 110, 25, 15, "save"));
        this.buttonList.add(new GuiButton(BUTTON_DOWN, offsetX + 4, offsetY + 110, 25, 15, "save"));

        this.buttonList.add(new GuiButton(BUTTON_UP, offsetX + 4, offsetY + 95, 25, 15, "load"));
        this.buttonList.add(new GuiButton(BUTTON_DOWN, offsetX + 4, offsetY + 95, 25, 15, "load"));

        ItemStack item = Minecraft.getMinecraft().player.getActiveItemStack();

        NBTTagCompound NBT = item.getOrCreateSubCompound("filename");
        String filename = NBT.getString("filename");
        if (filename.equals("")) {
            try {
                FileInputStream f = new FileInputStream(this.mc.player.getUniqueID().toString() + "-default_magic_script.dat");
                File file = new File(this.mc.player.getUniqueID().toString() + "-default_magic_script.dat");
                byte[] b = new byte[Math.toIntExact(file.length())];
                f.read(b);
                filename = new String(b);
                NBT.setString("filename",filename);
            } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        logger.info("client filename:{}",filename);
        try {
            FileInputStream stream = new FileInputStream(filename);
            File file = new File(filename);
            byte[] b = new byte[Math.toIntExact(file.length())];
            stream.read(b);
            this.inputField.setText(new String(b));
            stream.close();
            NBT.setString("source code",new String(b));
            CodeMagic.get_network().sendToServer(new packetClientCode(NBT));
            item.setTagInfo("filename",NBT);
            ((CMContainer)this.inventorySlots).updateNBT(Minecraft.getMinecraft().player,item,NBT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean check(char s) {
        boolean b = false;

        String tmp = Character.toString(s);
        tmp = tmp.replaceAll("\\p{P}", "");
        if (1 != tmp.length()) {
            b = true;
        }
        return b;
    }
    private void processKeyboard(char input,int keyID) {
        // TODO
        System.out.println(keyID);
        if (keyID != 0) {
            if (input >= 32 && input <= 126)
                this.inputField.addText(input);
            else if (keyID == 15)
                for(int i = 0;i < 4;i++)
                    this.inputField.addText(' ');
            else if (keyID == 28)
                this.inputField.addText('\n');
            else if (keyID == 14) {
                    this.inputField.deleteWord();
            } else if (keyID == 1) {
                String code = this.inputField.getText();
                byte[] code_utf8 = code.getBytes(StandardCharsets.UTF_8);
                code = new String(code_utf8, StandardCharsets.UTF_8);
                logger.info("file:{},source code:{}",this.titleFiled.getText(),code);
                byte[] name_utf8 = this.titleFiled.getText().getBytes(StandardCharsets.UTF_8);

                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(new String(name_utf8,StandardCharsets.UTF_8) + ".lua"));
                    BufferedWriter default_name = new BufferedWriter( new FileWriter(this.mc.player.getUniqueID().toString() + "-default_magic_script.dat"));
                    default_name.write(new String(name_utf8,StandardCharsets.UTF_8) + ".lua");
                    out.write(code);
                    out.close();
                    default_name.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ItemStack item = Minecraft.getMinecraft().player.getActiveItemStack();

                NBTTagCompound NBT = item.getOrCreateSubCompound("filename");
                NBT.setString("filename",new String(name_utf8,StandardCharsets.UTF_8) + ".lua");
                NBT.setString("source code",code);
//                packet.NBT = NBT;
                CodeMagic.get_network().sendToServer(new packetClientCode(NBT));
                this.mc.displayGuiScreen(null);
            }
        } else {
            if (input != '\0')
                this.inputField.addText(input);
        }
    }
    @Override
    public void keyTyped (char input,int keyID) {
//        System.out.println(keyID);
        if (focus == 1) {
            if (keyID == 205) {
                this.inputField.CursorRight();
                return;
            } else if (keyID == 203) {
                this.inputField.CursorLeft();
                return;
            } else if (keyID == 200) {
                this.inputField.CursorUp();
                return;
            } else if (keyID == 208) {
                this.inputField.CursorDown();
                return;
            }
            processKeyboard(input, keyID);
        } else if (focus == 0) {
            this.titleFiled.writeText(Character.toString(input));
        }
    }
    @Override
    public void mouseClicked(int mouseX,int mouseY,int mouseButton) {
        logger.info("mouseX:{},mouseY:{},offsetX:{},offsetY:{}",mouseX,mouseY,offsetX,offsetY);
        if ((mouseX >= this.offsetX && mouseX <= offsetX + 35) && (mouseY >= this.offsetY && mouseY <= this.offsetY + 12)) {
            this.focus = 0;
            this.titleFiled.mouseClicked(mouseX, mouseY, mouseButton);
        } else
            this.focus = 1;

        if (this.buttonList.get(0).mousePressed(this.mc,mouseX,mouseY)) {   //保存
            String code = this.inputField.getText();
            byte[] code_utf8 = code.getBytes(StandardCharsets.UTF_8);
            code = new String(code_utf8, StandardCharsets.UTF_8);
            logger.info("file:{},source code:{}",this.titleFiled.getText(),code);
            try {
                byte[] name_utf8 = this.titleFiled.getText().getBytes(StandardCharsets.UTF_8);
                BufferedWriter out = new BufferedWriter(new FileWriter(new String(name_utf8,StandardCharsets.UTF_8) + ".lua"));
                out.write(code);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.buttonList.get(2).mousePressed(this.mc,mouseX,mouseY)) {   //读取
            try {
                FileInputStream stream = new FileInputStream(this.titleFiled.getText() + ".lua");
                logger.info("filename:{}",this.titleFiled.getText() + ".lua");
                File file = new File(this.titleFiled.getText() + ".lua");
                try {
                    byte[] b = new byte[Math.toIntExact(file.length())];
                    stream.read(b);
                    this.inputField.setText(new String(b));
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        logger.info("focus:{}",this.focus);
    }
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
        this.inputField.drawTextBox();
        this.titleFiled.drawTextBox();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        // TODO
    }
}
