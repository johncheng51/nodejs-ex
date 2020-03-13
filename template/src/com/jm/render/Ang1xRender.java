package com.jm.render;

import java.util.ArrayList;
import java.util.Map;

public class Ang1xRender extends AngRender {
    
    @Override    
    public String getAttStr() {
        ArrayList<String> list=new ArrayList();
        Map<String,String> map=layout.getAttsMap();
        map.forEach((key,value)->{
             if (key.equals("click")||
                 key.equals("controller"))  
             {
                 list.add(key);
                 list.add(value);
             }
        });
        if (list.size()>0) 
        {
            String key=list.get(0);
           this.changeKey(key,"ng-"+key,list.get(1));
        }
        return super.getAttStr();
        
    }
}
