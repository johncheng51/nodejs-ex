package com.jm.ui;

import com.jm.util.Util;
import java.util.*;

public class UITable extends UILayout implements Cloneable {

    int xsize = 0, ysize = 0;
    int cx = 0;
    int cy = 0;
    boolean over = false;

    public UITable(String param) {
        String[] sa = Util.split(param, "|");
        xsize = Integer.parseInt(sa[0]);
        ysize = Integer.parseInt(sa[1]);
        process();
        if (sa.length < 3)  setType(UILayout.UI_ARRAY);
        else   setType(sa[2]);
    }

    private void process() {
        
        for (int i = 0; i < ysize; i++)
            super.add(uiParser.create(UILayout.UI_ROW));
    }

    @Override
    public UILayout add(UILayout c) {
        if (over) return this;
        UILayout row = list.get(cy);
        if (row.list == null)
            row.list = new ArrayList();
        row.list.add(c);
        c.parent = row;
        cx++;
        if (xsize == 2) {
            String s = (cx == 1) ? "right" : "left";
            c.addTdAtts("align=" + s);
        }
        if (cx == xsize) {
            cx = 0;
            cy++;
        }
        if (cy == ysize)
            over = true;
        return row;
    }

    public void remove(UILayout c) {
        if (over)      return;
        cx = cx - 1;
        if (cx < 0) {
            cy = cy - 1;
            cx = xsize - 1;
        }
        UILayout row = list.get(cy);
        row.remove(c);
    }

    public Object clone() {
        return super.clone();
    }

    public static void main(String[] args) {
        String text =
            "<table><ctable param='2|2'>" + 
            "<assign value|aa,bb=3/>" + 
            "aa<text name=aa require='aa'/>" +
            "cc<text name=bb require='bb'/>" + 
            "cc<text name=cc require='cc'/>" +
            "</ctable></table>";
        //String text="<text name='test' />";
        UIParser uiParse = UIParser.instance();
        UILayout layout = uiParse.compile(text);
        System.out.println(layout.printJS("Dept"));
        System.out.println(layout.printTree(" "));
        System.out.println(layout);
    }
}
