package com.jm.render;

import java.util.Map;


public class StrutsRender extends AbsRender {
    
    @Override
    public String printItem() {return printItem(null);}
    public String printItem(String alt) {
        alt = alt == null ? getName() : alt;
        return freeTranALT(PRINTITEM, alt);
    }

    @Override
    public String printAction() {return printItem();}
    @Override 
    public String checkbox() {return printItem(); }
    
    @Override 
    public String form() 
    {
        String value=printList();
        Map map=createMapARGS(value);
        return freeTran("form",map);
    }
    
    @Override 
    public String msg() {
        String value=getName();
        Map map=createMapARGS(value);
        return freeTran("msg",map);
        
    }
    
    @Override 
    public String link() {
        String action=att("action");
        String value=layout.getValue();
        Map map=createMapARGS(value);
        map.put("action",action);
        return freeTran("link",map);
    }
}
