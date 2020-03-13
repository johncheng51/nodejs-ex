package com.jm.render;
import com.jm.util.*;

public class HtmlRender extends AbsRender {
   
    @Override
    public String printItem() {
        StringBuffer sb = new StringBuffer();
        String template = "<input type='%s'  name='%s' id='%s' ";
        String text = Util.printf(template, new String[] { type,getName(), getName() });
        sb.append(text);
        sb.append(getAttStr());
        template = " value='%s' />\n";
        String s = (value == null) ? "" : value.trim();
        sb.append(Util.print1f(template, s));
        return sb.toString();
    }
    
    @Override
    public String printAction(){
        StringBuffer sb = new StringBuffer();
        String template = "<input type='%s' value='%s' name='%s' ";
        String text = Util.printf(template, new String[] { type,label0(),getName() });                 
        sb.append(text);
        sb.append(getAttStr()+"/>");
        return sb.toString();
    }

  
}
