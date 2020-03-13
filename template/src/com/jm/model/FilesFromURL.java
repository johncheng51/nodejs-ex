package com.jm.model;

import com.jm.util.Util;
import com.jm.xml.XmlList;
import java.util.*;

public class FilesFromURL {
    private String url;

    public List<String> getFolder() {
        return urlFolder;
    }
    private List<String>  urlFolder=new ArrayList();
    private List<String> classes=new ArrayList();
    private List<String>  javaes=new ArrayList();
    private static final String LINK="a";
    public FilesFromURL(String url) {
       this.url=url;
       process();
    }
    
    private void process(){
       String text= Util.readUrlText(url);
       XmlList xmlList=new XmlList(text,LINK);
       xmlList.getTable().forEach((key,value)->{
            if (value.endsWith("/")) urlFolder.add(value); 
            else if (value.endsWith(".class")) classes.add(value);
            else if (value.endsWith(".java"))  javaes.add(value);
       });               
         
    }
    
    public Map<String,String>  loadJava(){
        Map<String,String> map=new Hashtable();
        javaes.stream().forEach(value->{
            map.put(value,Util.readUrlText(url+value));                       
        });
        return map;
    }
}
