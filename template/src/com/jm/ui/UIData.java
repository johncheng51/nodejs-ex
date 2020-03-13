package com.jm.ui;

import com.jm.util.Util;

import java.util.*;

public class UIData implements Cloneable {

    public static final char SELECT = 's';
    public static final char LIST = 'L';
    public static final char LABEL = 'l';
    public static final char BOOLEAN = 'b';
    public char type = ' ';
    public String key = "";
    public boolean flag = false;
    public String value = "";
    public Vector list = null;

    public UIData(String key) {
        this(key, ' ');
    }

    public UIData(String key, char type) {
        this.key = key;
        this.type = type;
    }

    public UIData(String key, String text) {
        this.key = key;
        String table = "L list s select l label b radiobutton b checkbox";
        type = Util.findCommand(table, text);


    }

    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
        }
        return null;
    }

    public static String getType(String type) {
        char a = Util.isBlank(type) ? 'l' : type.charAt(0);
        String[] table = new String[] {
            "t", UILayout.UI_TEXT, 
            "f", UILayout.UI_FILE, 
            "m", UILayout.UI_LABEL, 
            "l", UILayout.UI_LINK, 
            "b", UILayout.UI_BUTTON, 
            "r", UILayout.UI_RADIOBUTTON, 
            "s", UILayout.UI_SELECT, 
            "c", UILayout.UI_CHECKBOX
        };
        for (int i = 0; i < table.length; i++) {
            if ((a + "").equals(table[2 * i]))
                return table[2 * i + 1];
        }

        return UILayout.UI_LABEL;

    }

    public boolean isLogic() {
        return type == BOOLEAN;
    }

    public boolean isSelect() {
        return type == SELECT || type == LIST;
    }

    public boolean isLabel() {
        return type == LABEL;
    }
}
