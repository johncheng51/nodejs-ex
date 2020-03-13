package com.jm.util;


import com.jm.render.AbsRender;

import java.util.*;
public class ThreadUtil {
    private static Map<Thread,Map<String,Stack<String>>> curMap=new Hashtable();
    
    private static Map<String,Stack<String>> getMap(){
        Map map=curMap.get(Thread.currentThread());
        if (map==null){
            map=new Hashtable();
            curMap.put(Thread.currentThread(), map);
        }
        return map;
    }
       
    public static String getValue(String key){
        Stack<String> stack= getMap().get(key);
        if (stack==null) return "";
        String value=stack.pop();
        stack.push(value);
        return value;
    }
    
    public static void putValue(String key,String value){
        Stack<String> stack= getMap().get(key);
        if (stack==null) {
            stack=new Stack();
            getMap().put(key, stack);
        }
        stack.push(value);
    }
    
    public static void remove(String key){
        try {
        Stack<String> stack= getMap().get(key);
        stack.pop();}
        catch(Exception e){}
    }
    
    public static boolean isTable(){
        String value= getValue(AbsRender.ROW_TYPE);
        return value.equals(AbsRender.TABLE);
    }
}
