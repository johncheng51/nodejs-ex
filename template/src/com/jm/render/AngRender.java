package com.jm.render;

import com.jm.gen.ModelGen;
import com.jm.util.Util;
import java.io.File;
import java.util.*;


public class AngRender extends HtmlRender {
    public static final String   INPUT="input";
    public static final String  OUTPUT="output";
    public static final String  BUTTON="button";
    public static final String  METHOD="method";
    public static final String  PUBLISH="publisher";
    public static final String  CALLER="caller";
    public static final String    LOOP="loop";
    public static final String      FOR="for";
    public static final String      KEY="key";
    public static final String   PROP="prop";
    public static final String   MINUS="-";
    public static final String   MSG="msg";
    @Override
    public String app() {
        String key=threadMgr.getAngVar();
        if (key==null) return app0();
        Map map=createTypeMap();
        map.put(VALUE,key);
        return this.freeTran(APP, map);
    }
    
    public String app0(){
        int n = type.indexOf(MINUS);
        String className = type.substring(n + 1);
        ModelGen modelGen = new ModelGen();
        String folder = threadMgr.getRenderModel().getMainFolder();
        modelGen.setWorkFolder(new File(folder));
        String text = printModel(className, new String[] { });
        StringBuilder input = new StringBuilder();
        StringBuilder html = new StringBuilder();
        StringBuilder output = new StringBuilder();
        attsMap.forEach((key, value) -> {
                CheckType ct=new CheckType(key);
                switch(ct.type){
                default:            
                case '[':     
                input.append(freeTran(INPUT, createValueMap(ct.key)));
                html.append("\n" + key + ":{{" + key + "}}");
                    break;
                case '(':
                    String value1=value.substring(0,value.indexOf("("));
                    threadMgr.append(freeTran(CALLER, createValueMap(value1)));
                    input.append(freeTran(OUTPUT, createValueMap(ct.key)));
                    input.append(freeTran(METHOD, createValueMap(ct.key)));
                    output.append(freeTran(PUBLISH, createValueMap(ct.key)));
                    Map map=createValueMap(ct.key);
                    map.put("class",className);
                    html.append(freeTran(BUTTON, map));
                    break;
                }
            });
        modelGen.writeAng(text, input + "", html + "",output+"");
        return nofunc();
    }
    
        
    
    @Override    
    public String getAttStr() {
            String loop=att(LOOP);
            if (loop!=null) return loop(loop)+super.getAttStr();
            String ngIf=att("if");
            if (ngIf!=null) return ngIf(ngIf.trim())+super.getAttStr();
            return super.getAttStr();
            }    
        
    public String ngIf(String text) {
        int n=text.indexOf("}");
        String key=text.substring(1,n);
        String value=text.substring(n+1);
        Map map=createMap(
           new String[]{KEY,VALUE},
           new String[]{key,value}); 
        String result=freeTran("if",map);
        return result;
        
    }
        
    public String loop(String loop) {
        String key=removeLast(loop);
        threadMgr.setAngVar(key);
        String body=printList();
        Map map=createTypeMap();
        map.put(KEY,key);
        map.put("list",loop);
        map.put("body",body);
        return freeTran(FOR, map);
    }
        
    @Override
    public String printItem() {
        return freeTran(PRINTITEM,createTypeMap());
    }
    
    @Override
    public String msg(){
         Map map=createMap(
           new String[]{NAME},
           new String[]{this.getName()}); 
        return freeTran(MSG,map);
    }
    
    public String route_create() {
        String value=layout.getValue();
        ModelGen modelGen = new ModelGen();
        String folder = threadMgr.getRenderModel().getMainFolder();
        modelGen.setWorkFolder(new File(folder));
        String[] fileValue=Util.split(value,"|");
        String className=fileValue[0];
        String text = printModel(className, new String[] { });
        String param= (fileValue.length>=3)? fileValue[2]:null;
        modelGen.writeRoute(text,param);
        Map map=createMap(
           new String[]{"classname",NAME},
           new String[]{cap(className),className}); 
        threadMgr.add("imports",freeTran("imports",map));
        threadMgr.add("routes",freeTran("routes",map));
        threadMgr.add("components",freeTran("components",map));
        map=createMap(
           new String[]{NAME,VALUE},
           fileValue);               
        return freeTran("route",map);
    }
    
    /*
     * 
     */
    @Override
    public String select(){
        String data=att(DATA);
        String[] words=split(data,"|");
        StringBuffer sb=new StringBuffer();
        Arrays.asList(words).forEach((word)->{
           String[] pair=split(word,"->");
           String key=pair[0];
           String value=pair.length==1? key:pair[1];
            Map map=createMap(
               new String[]{KEY,VALUE},
               new String[] {key,value});
            sb.append(freeTran("optiondata",map));                          
        });
        Map map=createMap(
           new String[]{KEY,VALUE},
           new String[] {getName(),sb+""});
        String options=freeTran("selectdata",map);
        log(options);
        threadMgr.add(PROP,options);
        return freeTran("select",createMap(NAME,getName()));
    }
    

    @Override
    public String checkbox(){return printItem();}
    @Override
    public String radiobutton(){
           String value=layout.getValue();
           Map map=createMap(
               new String[]{NAME,VALUE},
               new String[]{getName(),value}); 
            return freeTran("radio",map);
        }
    public static class CheckType {
        private char type;
        private String key;
        private boolean yes;

        public CheckType(String key) {
            yes = true;
            char[] ca = key.toCharArray();
            if (ca[0] == '[' || ca[0] == '(')
                type = ca[0];
            else {
                yes = false;
                type = ' ';
            }
            this.key = yes ? key.substring(1, key.length() - 1) : key;
        }
    }


}
