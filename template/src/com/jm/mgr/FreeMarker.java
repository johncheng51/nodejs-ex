package com.jm.mgr;

import com.jm.util.Util;
import freemarker.template.*;
import java.io.*;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FreeMarker extends AdminMgr {
    private Configuration config;
    private static final String TEMPLATELOC = "template";
    private static final String VALUE = "value";
    private Map<String, String> templateMap = new Hashtable();
    private FileMgr fileMgr = FileMgr.instance();

    private FreeMarker() {}
    private void init() {
        try {
            config = new Configuration(Configuration.VERSION_2_3_24);
            List<Path> paths = fileMgr.listFile(TEMPLATELOC);
            paths.stream().forEach(path -> {
                    String text = Util.readFile(path.toFile(),true);
                    String key = Util.getFirst(path.toFile().getName(), ".");
                    templateMap.put(key, text);
                });
        } catch (Exception e) {
        }
    }
    public String readTemplate(String template) {
        return templateMap.get(template);
    }
    public String readTemplate(String folder,String template){
        return fileMgr.readFile(folder, template+SwapMgr.TXT);
    }
    public String transByFileName(String name, Map map) {
        Template template = getTemplate(templateMap.get(name));
        StringWriter sw = new StringWriter();
        try {
            template.process(map, sw);
        } catch (Exception e) {
               throw new RuntimeException(e.getMessage());
        }
        return sw + "";
    }
    private Template getTemplate(String text) {
        Template template = null;
        try {
            template = config.getTemplate(text);
        } catch (Exception e) {}
        if (template != null)
            return template;
        try {
            template = new Template(text, new StringReader(text), config);
            return template;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }
    public String tranWithText(String templateStr,String[] values){
        Map<String, String> map = new Hashtable();
       
        for(int i=0;i<10;i++){
            char c='a';
            c+=(char) i;
            String word=(i<values.length)? values[i]:"NONE";
            if (i>=1) word=Util.cap(word);
            map.put(c+"",word);
        }
        return tranWithText(templateStr, map);
    }
    public String tranWithText(String templateStr,String value) {
        return tranWithText(templateStr,VALUE,value);
    }
    public String tranWithText(String templateStr, String key, String value) {
        Map<String, String> map = new Hashtable();
        map.put(key, value);
        return tranWithText(templateStr, map);
    }

    public String tranWithText(String templateStr, Map map) {
        Template template = getTemplate(templateStr);
        StringWriter sw = new StringWriter();
        try {
            template.process(map, sw);
        } catch (Exception e) {
            //throw new RuntimeException(e.getMessage());
            logger("ERROR:"+templateStr);
            
        }
        return sw + "";
    }

    private static FreeMarker mgr;
    private static Object lock = new Object();

    public static synchronized FreeMarker instance() {
        if (mgr != null)  return mgr;
        synchronized (lock) {
            mgr = new FreeMarker();
            mgr.init();
            return mgr;
        }
    }
    
    
    private static void logger(String text){
        System.out.println(text);
    }

    

}
