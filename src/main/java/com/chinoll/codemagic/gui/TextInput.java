package com.chinoll.codemagic.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.LogicOp;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

import java.util.function.Predicate;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

@SideOnly(Side.CLIENT)
public class TextInput extends Gui {
    private final int id;
    private final FontRenderer fontRenderer;
    public int x;
    public int y;
    public int width;
    public int height;
    private String text = "";
    private int maxStringLength = 32;
    private int cursorCounter;
    private boolean enableBackgroundDrawing = true;
    private boolean canLoseFocus = true;
    private boolean isFocused;
    private boolean isEnabled = true;
    public int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;
    private boolean visible = true;
    private GuiResponder guiResponder;
    private Predicate<String> validator = s -> true;

    public TextInput(int p_i45542_1_, FontRenderer p_i45542_2_, int p_i45542_3_, int p_i45542_4_, int p_i45542_5_, int p_i45542_6_) {
        this.id = p_i45542_1_;
        this.fontRenderer = p_i45542_2_;
        this.x = p_i45542_3_;
        this.y = p_i45542_4_;
        this.width = p_i45542_5_;
        this.height = p_i45542_6_;
    }

    public void setGuiResponder(GuiResponder p_setGuiResponder_1_) {
        this.guiResponder = p_setGuiResponder_1_;
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    public void setText(String p_setText_1_) {
        if (this.validator.test(p_setText_1_)) {
            if (p_setText_1_.length() > this.maxStringLength) {
                this.text = p_setText_1_.substring(0, this.maxStringLength);
            } else {
                this.text = p_setText_1_;
            }

            this.setCursorPositionEnd();
        }
    }

    public String getText() {
        return this.text;
    }

    public String getSelectedText() {
        int lvt_1_1_ = Math.min(this.cursorPosition, this.selectionEnd);
        int lvt_2_1_ = Math.max(this.cursorPosition, this.selectionEnd);
        return this.text.substring(lvt_1_1_, lvt_2_1_);
    }

    public void setValidator(Predicate<String> p_setValidator_1_) {
        this.validator = p_setValidator_1_;
    }

    public void writeText(String p_writeText_1_) {
        String lvt_2_1_ = "";
        String lvt_3_1_ = ChatAllowedCharacters.filterAllowedCharacters(p_writeText_1_);
        int lvt_4_1_ = Math.min(this.cursorPosition, this.selectionEnd);
        int lvt_5_1_ = Math.max(this.cursorPosition, this.selectionEnd);
        int lvt_6_1_ = this.maxStringLength - this.text.length() - (lvt_4_1_ - lvt_5_1_);
        if (!this.text.isEmpty()) {
            lvt_2_1_ = lvt_2_1_ + this.text.substring(0, lvt_4_1_);
        }

        int lvt_7_2_;
        if (lvt_6_1_ < lvt_3_1_.length()) {
            lvt_2_1_ = lvt_2_1_ + lvt_3_1_.substring(0, lvt_6_1_);
            lvt_7_2_ = lvt_6_1_;
        } else {
            lvt_2_1_ = lvt_2_1_ + lvt_3_1_;
            lvt_7_2_ = lvt_3_1_.length();
        }

        if (!this.text.isEmpty() && lvt_5_1_ < this.text.length()) {
            lvt_2_1_ = lvt_2_1_ + this.text.substring(lvt_5_1_);
        }

        if (this.validator.test(lvt_2_1_)) {
            this.text = lvt_2_1_;
            this.moveCursorBy(lvt_4_1_ - this.selectionEnd + lvt_7_2_);
            this.setResponderEntryValue(this.id, this.text);
        }
    }

    public void setResponderEntryValue(int p_setResponderEntryValue_1_, String p_setResponderEntryValue_2_) {
        if (this.guiResponder != null) {
            this.guiResponder.setEntryValue(p_setResponderEntryValue_1_, p_setResponderEntryValue_2_);
        }

    }

    public void deleteWords(int p_deleteWords_1_) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(p_deleteWords_1_) - this.cursorPosition);
            }
        }
    }

    public void deleteFromCursor(int p_deleteFromCursor_1_) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean lvt_2_1_ = p_deleteFromCursor_1_ < 0;
                int lvt_3_1_ = lvt_2_1_ ? this.cursorPosition + p_deleteFromCursor_1_ : this.cursorPosition;
                int lvt_4_1_ = lvt_2_1_ ? this.cursorPosition : this.cursorPosition + p_deleteFromCursor_1_;
                String lvt_5_1_ = "";
                if (lvt_3_1_ >= 0) {
                    lvt_5_1_ = this.text.substring(0, lvt_3_1_);
                }

                if (lvt_4_1_ < this.text.length()) {
                    lvt_5_1_ = lvt_5_1_ + this.text.substring(lvt_4_1_);
                }

                if (this.validator.test(lvt_5_1_)) {
                    this.text = lvt_5_1_;
                    if (lvt_2_1_) {
                        this.moveCursorBy(p_deleteFromCursor_1_);
                    }

                    this.setResponderEntryValue(this.id, this.text);
                }
            }
        }
    }

    public int getId() {
        return this.id;
    }

    public int getNthWordFromCursor(int p_getNthWordFromCursor_1_) {
        return this.getNthWordFromPos(p_getNthWordFromCursor_1_, this.getCursorPosition());
    }

    public int getNthWordFromPos(int p_getNthWordFromPos_1_, int p_getNthWordFromPos_2_) {
        return this.getNthWordFromPosWS(p_getNthWordFromPos_1_, p_getNthWordFromPos_2_, true);
    }

    public int getNthWordFromPosWS(int p_getNthWordFromPosWS_1_, int p_getNthWordFromPosWS_2_, boolean p_getNthWordFromPosWS_3_) {
        int lvt_4_1_ = p_getNthWordFromPosWS_2_;
        boolean lvt_5_1_ = p_getNthWordFromPosWS_1_ < 0;
        int lvt_6_1_ = Math.abs(p_getNthWordFromPosWS_1_);

        for(int lvt_7_1_ = 0; lvt_7_1_ < lvt_6_1_; ++lvt_7_1_) {
            if (!lvt_5_1_) {
                int lvt_8_1_ = this.text.length();
                lvt_4_1_ = this.text.indexOf(32, lvt_4_1_);
                if (lvt_4_1_ == -1) {
                    lvt_4_1_ = lvt_8_1_;
                } else {
                    while(p_getNthWordFromPosWS_3_ && lvt_4_1_ < lvt_8_1_ && this.text.charAt(lvt_4_1_) == ' ') {
                        ++lvt_4_1_;
                    }
                }
            } else {
                while(p_getNthWordFromPosWS_3_ && lvt_4_1_ > 0 && this.text.charAt(lvt_4_1_ - 1) == ' ') {
                    --lvt_4_1_;
                }

                while(lvt_4_1_ > 0 && this.text.charAt(lvt_4_1_ - 1) != ' ') {
                    --lvt_4_1_;
                }
            }
        }

        return lvt_4_1_;
    }

    public void moveCursorBy(int p_moveCursorBy_1_) {
        this.setCursorPosition(this.selectionEnd + p_moveCursorBy_1_);
    }

    public void setCursorPosition(int p_setCursorPosition_1_) {
        this.cursorPosition = p_setCursorPosition_1_;
        int lvt_2_1_ = this.text.length();
        this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, lvt_2_1_);
        this.setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public boolean textboxKeyTyped(char p_textboxKeyTyped_1_, int p_textboxKeyTyped_2_) {
        if (!this.isFocused) {
            return false;
        } else if (GuiScreen.isKeyComboCtrlA(p_textboxKeyTyped_2_)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        } else if (GuiScreen.isKeyComboCtrlC(p_textboxKeyTyped_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        } else if (GuiScreen.isKeyComboCtrlV(p_textboxKeyTyped_2_)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.getClipboardString());
            }

            return true;
        } else if (GuiScreen.isKeyComboCtrlX(p_textboxKeyTyped_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.isEnabled) {
                this.writeText("");
            }

            return true;
        } else {
            switch(p_textboxKeyTyped_2_) {
                case 14:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.isEnabled) {
                            this.deleteWords(-1);
                        }
                    } else if (this.isEnabled) {
                        this.deleteFromCursor(-1);
                    }

                    return true;
                case 199:
                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(0);
                    } else {
                        this.setCursorPositionZero();
                    }

                    return true;
                case 203:
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                        } else {
                            this.setSelectionPos(this.getSelectionEnd() - 1);
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    } else {
                        this.moveCursorBy(-1);
                    }

                    return true;
                case 205:
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                        } else {
                            this.setSelectionPos(this.getSelectionEnd() + 1);
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    } else {
                        this.moveCursorBy(1);
                    }

                    return true;
                case 207:
                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(this.text.length());
                    } else {
                        this.setCursorPositionEnd();
                    }

                    return true;
                case 211:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.isEnabled) {
                            this.deleteWords(1);
                        }
                    } else if (this.isEnabled) {
                        this.deleteFromCursor(1);
                    }

                    return true;
                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(p_textboxKeyTyped_1_)) {
                        if (this.isEnabled) {
                            this.writeText(Character.toString(p_textboxKeyTyped_1_));
                        }

                        return true;
                    } else {
                        return false;
                    }
            }
        }
    }

    public boolean mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) {
        boolean lvt_4_1_ = p_mouseClicked_1_ >= this.x && p_mouseClicked_1_ < this.x + this.width && p_mouseClicked_2_ >= this.y && p_mouseClicked_2_ < this.y + this.height;
        if (this.canLoseFocus) {
            this.setFocused(lvt_4_1_);
        }

        if (this.isFocused && lvt_4_1_ && p_mouseClicked_3_ == 0) {
            int lvt_5_1_ = p_mouseClicked_1_ - this.x;
            if (this.enableBackgroundDrawing) {
                lvt_5_1_ -= 4;
            }

            String lvt_6_1_ = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(lvt_6_1_, lvt_5_1_).length() + this.lineScrollOffset);
            return true;
        } else {
            return false;
        }
    }

    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
                drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
            }

            int lvt_1_1_ = this.isEnabled ? this.enabledColor : this.disabledColor;
            int lvt_2_1_ = this.cursorPosition - this.lineScrollOffset;
            int lvt_3_1_ = this.selectionEnd - this.lineScrollOffset;
            String lvt_4_1_ = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            boolean lvt_5_1_ = lvt_2_1_ >= 0 && lvt_2_1_ <= lvt_4_1_.length();
            boolean lvt_6_1_ = this.isFocused && this.cursorCounter / 6 % 2 == 0 && lvt_5_1_;
            int lvt_7_1_ = this.enableBackgroundDrawing ? this.x + 4 : this.x;
            int lvt_8_1_ = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
            int lvt_9_1_ = lvt_7_1_;
            if (lvt_3_1_ > lvt_4_1_.length()) {
                lvt_3_1_ = lvt_4_1_.length();
            }

            if (!lvt_4_1_.isEmpty()) {
                String lvt_10_1_ = lvt_5_1_ ? lvt_4_1_.substring(0, lvt_2_1_) : lvt_4_1_;
                lvt_9_1_ = this.fontRenderer.drawStringWithShadow(lvt_10_1_, (float)lvt_7_1_, (float)lvt_8_1_, lvt_1_1_);
                System.out.println(lvt_10_1_);
            }

            boolean lvt_10_2_ = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int lvt_11_1_ = lvt_9_1_;
            if (!lvt_5_1_) {
                lvt_11_1_ = lvt_2_1_ > 0 ? lvt_7_1_ + this.width : lvt_7_1_;
            } else if (lvt_10_2_) {
                lvt_11_1_ = lvt_9_1_ - 1;
                --lvt_9_1_;
            }

            if (!lvt_4_1_.isEmpty() && lvt_5_1_ && lvt_2_1_ < lvt_4_1_.length()) {
                lvt_9_1_ = this.fontRenderer.drawStringWithShadow(lvt_4_1_.substring(lvt_2_1_), (float)lvt_9_1_, (float)lvt_8_1_, lvt_1_1_);
                System.out.println(lvt_4_1_.substring(lvt_2_1_));
            }

            if (lvt_6_1_) {
                if (lvt_10_2_) {
                    Gui.drawRect(lvt_11_1_, lvt_8_1_ - 1, lvt_11_1_ + 1, lvt_8_1_ + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
                } else {
                    //this.fontRenderer.drawStringWithShadow("_", (float)lvt_11_1_, (float)lvt_8_1_, lvt_1_1_);
                }
            }

            if (lvt_3_1_ != lvt_2_1_) {
                int lvt_12_1_ = lvt_7_1_ + this.fontRenderer.getStringWidth(lvt_4_1_.substring(0, lvt_3_1_));
                this.drawSelectionBox(lvt_11_1_, lvt_8_1_ - 1, lvt_12_1_ - 1, lvt_8_1_ + 1 + this.fontRenderer.FONT_HEIGHT);
            }

        }
    }

    private void drawSelectionBox(int p_drawSelectionBox_1_, int p_drawSelectionBox_2_, int p_drawSelectionBox_3_, int p_drawSelectionBox_4_) {
        int lvt_5_2_;
        if (p_drawSelectionBox_1_ < p_drawSelectionBox_3_) {
            lvt_5_2_ = p_drawSelectionBox_1_;
            p_drawSelectionBox_1_ = p_drawSelectionBox_3_;
            p_drawSelectionBox_3_ = lvt_5_2_;
        }

        if (p_drawSelectionBox_2_ < p_drawSelectionBox_4_) {
            lvt_5_2_ = p_drawSelectionBox_2_;
            p_drawSelectionBox_2_ = p_drawSelectionBox_4_;
            p_drawSelectionBox_4_ = lvt_5_2_;
        }

        if (p_drawSelectionBox_3_ > this.x + this.width) {
            p_drawSelectionBox_3_ = this.x + this.width;
        }

        if (p_drawSelectionBox_1_ > this.x + this.width) {
            p_drawSelectionBox_1_ = this.x + this.width;
        }

        Tessellator lvt_5_3_ = Tessellator.getInstance();
        BufferBuilder lvt_6_1_ = lvt_5_3_.getBuffer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(LogicOp.OR_REVERSE);
        lvt_6_1_.begin(7, DefaultVertexFormats.POSITION);
        lvt_6_1_.pos((double)p_drawSelectionBox_1_, (double)p_drawSelectionBox_4_, 0.0D).endVertex();
        lvt_6_1_.pos((double)p_drawSelectionBox_3_, (double)p_drawSelectionBox_4_, 0.0D).endVertex();
        lvt_6_1_.pos((double)p_drawSelectionBox_3_, (double)p_drawSelectionBox_2_, 0.0D).endVertex();
        lvt_6_1_.pos((double)p_drawSelectionBox_1_, (double)p_drawSelectionBox_2_, 0.0D).endVertex();
        lvt_5_3_.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public void setMaxStringLength(int p_setMaxStringLength_1_) {
        this.maxStringLength = p_setMaxStringLength_1_;
        if (this.text.length() > p_setMaxStringLength_1_) {
            this.text = this.text.substring(0, p_setMaxStringLength_1_);
        }

    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    public void setEnableBackgroundDrawing(boolean p_setEnableBackgroundDrawing_1_) {
        this.enableBackgroundDrawing = p_setEnableBackgroundDrawing_1_;
    }

    public void setTextColor(int p_setTextColor_1_) {
        this.enabledColor = p_setTextColor_1_;
    }

    public void setDisabledTextColour(int p_setDisabledTextColour_1_) {
        this.disabledColor = p_setDisabledTextColour_1_;
    }

    public void setFocused(boolean p_setFocused_1_) {
        if (p_setFocused_1_ && !this.isFocused) {
            this.cursorCounter = 0;
        }

        this.isFocused = p_setFocused_1_;
        if (Minecraft.getMinecraft().currentScreen != null) {
            Minecraft.getMinecraft().currentScreen.setFocused(p_setFocused_1_);
        }

    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public void setEnabled(boolean p_setEnabled_1_) {
        this.isEnabled = p_setEnabled_1_;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    public void setSelectionPos(int p_setSelectionPos_1_) {
        int lvt_2_1_ = this.text.length();
        if (p_setSelectionPos_1_ > lvt_2_1_) {
            p_setSelectionPos_1_ = lvt_2_1_;
        }

        if (p_setSelectionPos_1_ < 0) {
            p_setSelectionPos_1_ = 0;
        }

        this.selectionEnd = p_setSelectionPos_1_;
        if (this.fontRenderer != null) {
            if (this.lineScrollOffset > lvt_2_1_) {
                this.lineScrollOffset = lvt_2_1_;
            }

            int lvt_3_1_ = this.getWidth();
            String lvt_4_1_ = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), lvt_3_1_);
            int lvt_5_1_ = lvt_4_1_.length() + this.lineScrollOffset;
            if (p_setSelectionPos_1_ == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, lvt_3_1_, true).length();
            }

            if (p_setSelectionPos_1_ > lvt_5_1_) {
                this.lineScrollOffset += p_setSelectionPos_1_ - lvt_5_1_;
            } else if (p_setSelectionPos_1_ <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - p_setSelectionPos_1_;
            }

            this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, lvt_2_1_);
        }

    }

    public void setCanLoseFocus(boolean p_setCanLoseFocus_1_) {
        this.canLoseFocus = p_setCanLoseFocus_1_;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean p_setVisible_1_) {
        this.visible = p_setVisible_1_;
    }
}

