package com.jm.mgr;

import com.jm.model.ClassField;
import com.jm.model.ClassModel;
import com.jm.model.FreeMarkModel;
import com.jm.util.Util;
import com.jm.xml.LineHash;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by John on 4/1/2017.
 */
public class SwapMgr {

    private Map<String, Map<String, String>> curMap = new Hashtable();
    private Map<String, Map<String, String>> codeMap = new Hashtable();
    public static final String MARK  = FreeMarkModel.MARK;
    public static final String ANNO  ="anno";
    public static final String CODE  ="code";
    public static final String TABLE ="table";
    public static final String VALUE ="value";
    public static final String FIELDS ="fields";
    public static final String MAP    ="map";
    public static final String TXT   =".txt";
    private FreeMarker freeMarker=FreeMarker.instance();
    private FileMgr fileMgr = FileMgr.instance();

    private void init() 
    {
        
    }
    
    private Map<String,String> loadCodeMap(String template) {
        Map<String, String> map = codeMap.get(template);
        if (map != null) return map;
        String text = fileMgr.readFile(CODE,template);
        codeMap.put(template, LineHash.getMap(text));
        return codeMap.get(template); 
    }
    
    public String getCodeTemplate(String key,String template){
       String temp=loadCodeMap(template).get(key);
       if (temp==null) {
           log(key +" not found in "+template);
           throw new Error("");
       }
      return temp;
    }
    private Map<String,String> load(String template) {
        return load(template,true);
    }
    private synchronized Map<String,String> load(String template,boolean remove) {
        Map<String, String> map = curMap.get(template);
        if (map != null) return map;
        String text = fileMgr.readFile(ANNO,remove? r(template):template);
        curMap.put(template, LineHash.getMap(text));
        return curMap.get(template);
    }
    
    public String getTemplate(String key,String template){
        return load(template+TXT,false).get(key);
    }

    private String makeMark(String key) {
        return "//" + MARK + key + MARK;
    }

    private String r(String template){
       return Util.removeLast(template,4)+TXT;
    }

    public String replace(String text, String key, String template) {
        
        Map<String, String> map = load(template);
        String value = map.get(key);
        text = text.replace(makeMark(key), value);
        return text;
    }

    public String replace(String text, ClassModel model, String template) {
        text = replace(text, TABLE, model.table(), template);
        for (ClassField field : model.list()) {
            Map myMap=new Hashtable();
            myMap.put(VALUE,  field.getColname());
            myMap.put(MAP,  field.map());
            myMap.put(FIELDS,field.list());
            text = replaceField(text,field.getName(),
                    field.getOp(),template,myMap);
        }
        return text;
    }

    public String replace(String text, String name, String value, String template) {
          Map myMap=new Hashtable();
          myMap.put(VALUE,  value);
          return replaceField(text,name,name,template,myMap);
    }

    public String replaceField(String text, 
                               String name,
                               String op,
                               String template,
                               Map argmap) {
        Map<String, String> map = load(template);
        String newTemplate = map.get(op);
        
        if (newTemplate==null){
            String message="ERROR::"+op+" not found in app/anno/"+template;
            log(message);
            throw new RuntimeException(message);
        }
        String value = freeMarker.tranWithText(newTemplate,argmap);
        text = text.replace(makeMark(name), value);
        return text;
    }
    
    private void log(String message){
        System.out.println(message);
    }

    private static Object lock = new Object();
    private static SwapMgr mgr;

    public synchronized static SwapMgr instance() {
        if (mgr != null) return mgr;
        synchronized (lock) {
            mgr = new SwapMgr();
            mgr.init();
            return mgr;
        }
    }
}
