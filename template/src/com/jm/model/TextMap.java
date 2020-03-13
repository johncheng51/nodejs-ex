package com.jm.model;

import java.util.Map;


public class TextMap 
{
    private String text;
    private Map<String,String> map;
    public TextMap(String text,Map map) 
    {
       this.text=text;
       this.map =map;
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getMap() {
        return map;
    }
}
