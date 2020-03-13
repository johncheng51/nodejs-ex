package com.jm.render;

import com.jm.util.Util;
import java.util.Map;

public class ExtjsRender extends HtmlRender{

    @Override
    public String printItem() {
       String label=getName().toUpperCase();
       Map map=createTypeMap();
       map.put("value",label);
       return freeTran("printitem",map);
    }
    
    @Override 
    public String checkbox() {return printItem(); }
    
    @Override
    public String popup() {
       String panel=att("panel");
       return freeTran(type,createValueMap(panel));
    }
    
    @Override
    public String form() {
       String var=att("var");
       String temp=att("temp");
       String items=this.printList();
       Map map=createMap("var",var);
       map.put("items",items);
        map.put("temp",temp);
        return freeTran(type,map);
    }
    
    @Override
    public String def() {
        String[] words=Util.split(layout.getValue(),",");
        String var=att("var");
        Map map=createMap("var",var);
        map.put("words",words);
        return freeTran(type,map);
    }
    
    public String items(){
        StringBuffer sb=new StringBuffer();
        sb.append("items:[");
        sb.append(printList(",\n"));
        sb.append("]");
        return sb+"";
    }
}
