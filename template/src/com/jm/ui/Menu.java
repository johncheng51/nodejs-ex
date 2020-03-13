package com.jm.ui;

import com.jm.render.AbsRender;
import com.jm.util.Util;

import java.awt.MenuItem;

import java.util.*;

public class Menu {

    private static final String NAME = "name";
    private static final String PIE = "|";
    private static final String SEP = "^";
    private static final String COMMON = ",";
    private static final String MENUITEM     = "menu_item";
    private static final String MENUDROPBODY = "menu_dropdown_body";
    private static final String MENUDROPELE  = "menu_dropdown_element";
    private static final String MENUBODY     = "menu_body";
    private static final String SRC   = "src";
    private static final String TITLE = "title";
    private static final String VALUE = "value";
    private static final String LIST  = "list";
    private String text;
    private ArrayList<MenuItem> list = new ArrayList();
    private AbsRender absRender;
    private boolean isSingle;

    public Menu(String text, AbsRender absRender,boolean isSingle) {
        this.absRender = absRender;
        this.text = text;
        this.isSingle = isSingle;
        if (isSingle)  processSingle();
        else   processDropDown();
    }

    private void processSingle() {
        String[] lines = Util.split(text, SEP);
        for (String line : lines) {
            list.add(strToMenu(line));
        }
    }

    private void processDropDown() {
        String[] lines = Util.split(text, PIE);
        for (String line : lines) {
            String[] words = Util.split(line, SEP);
            MenuItem m = null;
            int count = 0;
            for (String key : words) {
                if (count == 0)
                    m = strToMenu(key);
                else
                    m.sub.add(strToMenu(key));
                count++;
            }
            list.add(m);
        }
    }

    private MenuItem strToMenu(String str) {
        String[] sa = Util.split(str, COMMON);
        MenuItem menuItem = new MenuItem();
        menuItem.value = sa[0];
        menuItem.src = sa.length == 1 ? null : sa[1];
        return menuItem;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (MenuItem mi : list)
            sb.append(isSingle? getSingleMenu(mi):getMenuDropDown(mi));
        return absRender.freeTranValue(MENUBODY, sb + "");

    }


    private String getSingleMenu(MenuItem menuItem) {
        Map map = absRender.createMap(SRC, menuItem.src);
        map.put(VALUE, menuItem.value);
        return absRender.freeTran(MENUITEM, map) + "\n";
    }

    private String getMenuDropDown(MenuItem menuItem) {
       
        List<MenuItem> list=menuItem.sub;
        String result="";
        for (MenuItem item:list){
            Map map = absRender.createMap(SRC, item.getSrc());
            map.put(VALUE,item.getValue());
            result+=absRender.freeTran(MENUDROPELE, map);
            }
        Map map = absRender.createMap(TITLE, menuItem.value);
        map.put(VALUE,result);
        result=absRender.freeTran(MENUDROPBODY, map);
        return result;
    }

    private static class MenuItem {
        public String value;
        public String src;
        public ArrayList<MenuItem> sub = new ArrayList();


        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getSrc() {
            return src;
        }
    }


}
