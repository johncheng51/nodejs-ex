package com.jm.model;

import com.jm.xml.XmlBlock;
import java.util.Map;

public class UIModel {
    public static final String MAIN="main";
    public static final String TEMPLATE="template";
    public static final String FUNCTION="function";
    private String text;
    private Map<String,String> map;
    public UIModel(String text) {
        this.text=text;
        map= XmlBlock.getSimple(text); 
    }
    
    public String getMain() {
        return map.get(MAIN);
    }
    public String getTemplate() {
        return map.get(TEMPLATE);
    }
    public String getFUCTION() {
        return map.get(FUNCTION);
    }
}  
    
    
