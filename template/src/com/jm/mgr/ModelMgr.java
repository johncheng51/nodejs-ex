package com.jm.mgr;

import com.jm.model.*;
import com.jm.model.XmlBody;
import com.jm.util.Util;
import com.jm.xml.XmlBlock;
import java.util.*;

public class ModelMgr implements DoUpdate {
    private final static String CLASS="class";
    private final static String PATH ="path";
    private final static String DOT =".";
    private Map<String, Map>     topMap = new Hashtable();
    private Map<String, XmlBody> bodyMap;

    private ModelMgr() {}

    private static final String MODELS = FileMgr.CONFIG+"/models.xml";
    private FileMgr fileMgr = FileMgr.instance();

    private Map getMap(String type) {
        Map map = topMap.get(type);
        if (map == null) {
            map = new Hashtable();
            topMap.put(type, map);
        }
        return map;
    }

    private void init() {
        String text = fileMgr.readFile(this, MODELS);
        bodyMap = XmlBlock.getXmlBody(text);
        process(bodyMap);
    }
    
    public ClassModel make(String key){
       XmlBody xmlBody=bodyMap.get(key);
       return new ClassModel(xmlBody);
    }
    
    public ClassModel create(String text){
        bodyMap = XmlBlock.getXmlBody(text);
        Object[] a=bodyMap.keySet().toArray();
        XmlBody xmlBody=bodyMap.get(a[0]);
        return  new ClassModel(xmlBody);
    }

    private void process(Map<String, XmlBody> map) {
        String[] keys = new String[map.size()];
        map.keySet().toArray(keys);
        for (String key : keys) {
            XmlBody xmlBody = map.get(key);
            String type = xmlBody.get("type");
            char c = Util.findCommand("c class p path", type);
            switch (c) {
                case 'c':
                    ClassModel model = new ClassModel(xmlBody);
                    getMap(type).put(key, model);
                    break;
                case 'p':
                    PathModel pmodel = new PathModel(xmlBody);
                    getMap(type).put(key, pmodel);
            }

        }
    }

    public ClassModel getClassModel(String key) {
        String discol="";
        String[] pair=Util.split(key, DOT);
        if (pair.length>=2){
            discol=pair[0];
            key=pair[1];
        }
        else discol=key.toUpperCase();
        Map<String, ClassModel> map = getMap(CLASS);
        ClassModel model= map.get(key);
        if (model==null) {
            log(error(map,key));
            return null;
        }  
        model.setDiscol(discol);
        return model;
    }
    
    private String error(Map<String,ClassModel> map,String key){
        StringBuilder sb=new StringBuilder();
        sb.append("ERROR::["+key+"] not found in app/main/models\n");
        sb.append("Available models are\n");
        sb.append("=============================\n");
        map.forEach((k,v)->sb.append(k+" ")); 
        sb.append("\n=============================\n");
        return sb+""; 

    }
            
    private void log(String s) {
        System.out.println(s);
    }

    public PathModel getPathModel(String key) {
        Map<String, PathModel> map = getMap(PATH);
        return map.get(key);
    }

    private static ModelMgr mgr;
    private static Object lock = new Object();

    public synchronized static ModelMgr instance()

    {
        if (mgr != null) return mgr;
        synchronized (lock) {
            mgr = new ModelMgr();
            mgr.init();
            return mgr;
        }
    }

    public void update() {
    }


}
