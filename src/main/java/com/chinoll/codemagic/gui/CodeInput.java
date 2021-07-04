package com.chinoll.codemagic.gui;

import net.minecraft.client.gui.FontRenderer;
import org.apache.logging.log4j.LogManager;

import java.util.*;
import org.apache.logging.log4j.Logger;

public class CodeInput {
    private int x,y,width,height;
    private final FontRenderer fontRenderer;
    private final TextInput[] code_input;
    private final int count;
    private ArrayList<String> text_array;
    private int firstLine,endLine;
    private int cursorX,cursorY;    //编辑框光标位置
    public static final Logger logger = LogManager.getLogger();
    CodeInput(FontRenderer fontRenderer, int x, int y, int width, int height) {
        this.fontRenderer = fontRenderer;
        count = height/9;
        code_input = new TextInput[(int)count];
        cursorX = 0;
        firstLine = endLine = cursorY = 1;
        text_array = new ArrayList<String>(Collections.emptyList());
        text_array.add("");
        for(int i = 0;i < count;i++) {
            code_input[i] = new TextInput(i+1,fontRenderer,x,y+i*9,width,9);
            code_input[i].setMaxStringLength(256);
            code_input[i].setEnableBackgroundDrawing(false);
            code_input[i].setText("");
            code_input[i].setCanLoseFocus(false);
        }
//        cursorY = cursorX = 0;
    }
    public void setText(String str) {
        String[] s;
        if (str.length() > 0) {
            str += " ";
            s = str.split("\n");
                if (str.charAt(str.length() - 2) == '\n') {
                    s[s.length - 1] = "";
                    this.text_array = new ArrayList<String>(Arrays.asList(s));
                    s[s.length - 1] = "|";
                } else {
                    s[s.length - 1] = s[s.length - 1].substring(0, s[s.length - 1].length() - 1);
                    this.text_array = new ArrayList<String>(Arrays.asList(s));
                    s[s.length - 1] += "|";
                }
            this.cursorY = this.text_array.size();
            this.cursorX = s[0].length();
            if (s.length > count) {
                s = Arrays.copyOfRange(s, s.length - count, s.length);
                firstLine = Math.max(s.length - count, 1);
            }
            endLine = cursorY;
            for(int i = 0;i < count;i++) {
                if (i < s.length)
                    code_input[i].setText(s[i]);
                else
                    code_input[i].setText("");
            }
        } else {
            this.text_array = new ArrayList<String>(Collections.emptyList());
            firstLine = endLine = 0;
            for (int i = 0; i < count; i++) {
                code_input[i].setText("");
            }
        }
    }

    public void CursorUp() {
        if (this.cursorY == firstLine) {
//            firstLine--;
            if (endLine > count)
                endLine = --endLine > 1 ? endLine : 1;
            firstLine = --firstLine > 1 ? firstLine : 1;
        }
        this.cursorY = --this.cursorY > 1 ? this.cursorY : 1;
        String string = this.text_array.get(this.cursorY);      //当前行
        StringBuffer buffer = new StringBuffer(this.text_array.get(this.cursorY - 1)); //上一行
        this.cursorX = string.length() < buffer.length() ? this.cursorX : buffer.length();
        buffer.insert(this.cursorX,"|");
        for(int i = firstLine;i <= endLine;i++) {
            if (i == this.cursorY)
                code_input[i - firstLine].setText(buffer.toString());
            else
                code_input[i - firstLine].setText(this.text_array.get(i - 1));
        }
    }
    public void CursorDown() {
        if (this.cursorY == endLine && endLine > count) {
                endLine = ++endLine > this.text_array.size() ? this.text_array.size() : endLine;
                firstLine++;
//                this.cursorY++;
        }
        this.cursorY = ++this.cursorY > this.text_array.size() ? this.text_array.size() : this.cursorY;
        String string = this.text_array.get(this.cursorY - 2);      //当前行
        StringBuffer buffer = new StringBuffer(this.text_array.get(this.cursorY - 1)); //下一行
        this.cursorX = Math.min(this.cursorX, buffer.length());
        buffer.insert(this.cursorX,"|");
        for(int i = firstLine;i <= endLine;i++) {
            if (i == this.cursorY)
                code_input[i - firstLine].setText(buffer.toString());
            else
                code_input[i - firstLine].setText(this.text_array.get(i - 1));
        }
    }
    public void CursorLeft() {
        StringBuffer buffer = new StringBuffer(this.text_array.get(this.cursorY - 1));
        String temp = this.text_array.get(this.cursorY - 1);
        if (--this.cursorX < 0)
            this.cursorX = 0;
        buffer.insert(this.cursorX,"|");
        this.text_array.set(this.cursorY - 1,new String(buffer));
        String[] s;
        if (this.text_array.size() > count)
            s = Arrays.copyOfRange(this.text_array.toArray(new String[0]), this.text_array.size() - count, this.text_array.size());
        else
            s = this.text_array.toArray(new String[0]);
        for(int i = 0;i < count;i++) {
            if (i < s.length)
                code_input[i].setText(s[i]);
            else
                code_input[i].setText("");
        }
        this.text_array.set(this.cursorY - 1,temp);
    }
    public void CursorRight() {
        StringBuffer buffer = new StringBuffer(this.text_array.get(this.cursorY - 1));
        String temp = this.text_array.get(this.cursorY - 1);
        if (++this.cursorX > temp.length())
            this.cursorX = temp.length();
        buffer.insert(this.cursorX,"|");
        this.text_array.set(this.cursorY - 1,new String(buffer));
        String[] s;
        if (this.text_array.size() > count) {
            s = Arrays.copyOfRange(this.text_array.toArray(new String[0]), this.text_array.size() - count, this.text_array.size());
//            firstLine = this.text_array.size() - count == 0 ? 1 : this.text_array.size() - count;
//            endLine = this.text_array.size();
        } else {
            s = this.text_array.toArray(new String[0]);
//            firstLine = 1;
//            endLine = this.text_array.size();
        }
        for(int i = 0;i < count;i++) {
            if (i < s.length)
                code_input[i].setText(s[i]);
            else
                code_input[i].setText("");
        }
        this.text_array.set(this.cursorY - 1,temp);
    }
    public void addText(char input) {
        StringBuffer buffer;

        if (input == '\n') {
            logger.info("end of line");
            logger.info(cursorY == this.text_array.size());
            if (cursorY == this.text_array.size()) {
//                this.cursorX = 0;
                this.cursorY++;
                if (this.cursorX == this.text_array.get(this.cursorY - 2).length() || this.cursorX == 0) {
                    this.text_array.add("");
                    if (this.text_array.size() <= count) {
                        this.code_input[endLine++].setText("|");
                        this.code_input[this.cursorY - 2].setText(this.text_array.get(this.cursorY - 2));
                    } else {
                        for (int i = firstLine; i < endLine; i++) {
                            this.code_input[i - firstLine].setText(this.text_array.get(i));
                        }
                        firstLine++;
                        endLine++;
                        this.code_input[endLine - firstLine].setText("|");
                    }
                } else {
                    buffer = new StringBuffer(this.text_array.get(this.cursorY - 2));
                    String sub1 = buffer.subSequence(0,this.cursorX).toString();
                    String sub2 = buffer.subSequence(this.cursorX,buffer.length()).toString();
                    this.text_array.set(this.cursorY - 2,sub1);
                    this.text_array.add(this.cursorY - 1,sub2);
                    if (this.text_array.size() > count)
                        firstLine++;
                    endLine++;
                    for (int i = firstLine; i <= endLine; i++) {
                        this.code_input[i - firstLine].setText(this.text_array.get(i - 1));
                        if (i == endLine)
                            this.code_input[i - firstLine].setText("|" + this.text_array.get(i - 1));
                    }
                }
            } else {
                buffer = new StringBuffer(this.text_array.get(this.cursorY - 1));
                String sub1 = buffer.subSequence(0,this.cursorX).toString();
                String sub2 = buffer.subSequence(this.cursorX,buffer.length()).toString();
                this.text_array.set(this.cursorY - 1,sub1);
                this.text_array.add(++this.cursorY - 1,sub2);
                endLine++;
                for(int i = firstLine;i <= endLine;i++) {
                    if (this.cursorY == i) {
                        this.code_input[i - firstLine].setText("|" + sub2);
                    } else {
                        this.code_input[i - firstLine].setText(this.text_array.get(i - 1));
                    }
                }
            }
            this.cursorX = 0;
            return;
        }
        buffer = new StringBuffer(this.text_array.get(this.cursorY - 1));
        buffer.insert(this.cursorX++,input);
        String temp = new String(buffer);
        buffer.insert(this.cursorX,"|");
        for(int i = this.firstLine;i <= this.endLine;i++) {
            if (this.cursorY == i)
                this.code_input[i - this.firstLine].setText(buffer.toString());
            else
                this.code_input[i - this.firstLine].setText(this.text_array.get(i - 1));
        }
        this.text_array.set(this.cursorY - 1,temp);
    }

    public void deleteWord() {
        StringBuffer buffer = new StringBuffer(this.text_array.get(this.cursorY - 1));
        if (this.cursorY  == 1 && this.cursorX == 0)
            return;
        else if (buffer.length() == 0) {
            this.text_array.remove(this.cursorY - 1);
            this.cursorY = --this.cursorY > 1 ? this.cursorY : 1;
            this.cursorX = this.text_array.get(this.cursorY - 1).length();
            for(int i = this.firstLine;i <= this.endLine;i++) {
                if (i == this.endLine)
                    this.code_input[i - this.firstLine].setText("");
                else
                    this.code_input[i - this.firstLine].setText(this.text_array.get(i - 1));
            }
            this.endLine = --this.endLine > 1 ? this.endLine : 1;
            return;
        }
        this.cursorX = --this.cursorX > 0 ? this.cursorX : 0;
        buffer.delete(this.cursorX,this.cursorX + 1);
        this.text_array.set(this.cursorY - 1,buffer.toString());
        buffer.insert(this.cursorX,"|");
        for(int i = this.firstLine;i <= this.endLine;i++) {
            if (this.cursorY == i)
                this.code_input[i - this.firstLine].setText(buffer.toString());
            else
                this.code_input[i - this.firstLine].setText(this.text_array.get(i - 1));
        }
    }
    public String getText() {
        StringBuilder source_code = new StringBuilder();
        for(String s:this.text_array) {
            source_code.append(s).append("\n");
        }
        logger.info("code:{}",source_code.toString());
        return source_code.toString();
    }
    public void drawTextBox() {
        for(TextInput t:this.code_input)
            t.drawTextBox();
    }
}
