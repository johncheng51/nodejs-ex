package com.jm.model;

import java.util.*;

public class  JasonData 
{
    private List<String> list;
    private Map<String,String> map;
    public JasonData(List list, Map map) 
    {
      this.list=list;
      this.map=map;  
    }

    public List<String> getList() {return list;}
    public Map<String, String> getMap() {return map;}
    public String get(String key) {return map.get(key);}
}
