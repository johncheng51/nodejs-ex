package com.jm.process;

import com.jm.json.*;
import com.jm.model.*;
import com.jm.util.Util;
import java.io.File;
import java.util.*;

public class ProcessWeb extends AbstractProcess {
    public static final String FOLDER = "folder";
    public static final String SPRING = "spring";
    public static final String SERVLET = "servlet";
    public static final String PATTERN = "pattern";
    public static final String DOT     = ".";
    public static final String MAP     = "map";
    public static final String MVC     = "mvc";
    public static final String VALUE   = "value";
    public static final String HEAD    = "head";
    public static final String END     = "end";
    public static final String WELCOME = "welcome";
    public static final String PACKAGE = "package";
    public static final String JAVA    = "java";
    public static final String WEBXML  = "web.xml";
    public static final String WEBINF  = "/WEB-INF";
    public static final String SERVLETDOT = ".servlet.";
    private static final String SRC        = "src";
    public static final String HYBERCONFIG = "hibernatecfg";
    public static final String WEB         = "web";
    public static final String SPRINGWEB   = "springweb";
    public static final String UNDER       = "/";
    public static final String INDEXSTART  = "indexStart";
    public static final String INDEXEND    = "indexEnd";
    public static final String LINK        = "link";
    public static final String INDEXHTML   = "index.html";
    public static final String SERVLETXML   = "-servlet.xml";
    public File currentFolder;
    private SpringXML springXML;
    private JasonData servlet;
    private boolean isSpring=false;
    public ProcessWeb(String text) {
        super(text);
    }

    @Override
    protected boolean appendApp(XmlBody xmlBody) {
        String folder = xmlBody.get(FOLDER);
        currentFolder = new File(folder);
        return false;
    }

    public void webinf(XmlResult result) {
        processMethod(result);
        addWeb(END);
        writeFile(WEBINF, WEBXML);
        doSpringXML();
        writeServlet();
    }


    public void hibernateconfig(XmlResult result) {
        pack = result.get(PACKAGE);
        JasonData jd = JasonParse.jasonData(result.value());
        Map<String, String> map = jd.getMap();
        map.put(PACKAGE,pack);
        String text=FreeMarkModel.ofMap(HYBERCONFIG,map);
        append(text);
        String file = path(pack) + UNDER + HYBERCONFIG+".xml";
        writeFile(SRC, file);
    }

    public void writeFile(String file){
        writeFile("",file);
    }
    @Override
    public void writeFile(String folder, String file) {
        String myFolder = currentFolder + UNDER + folder;
        super.writeFile(myFolder, file);
    }


    public void writeJava(String pack, String className) {
        String file = path(pack) + UNDER + className + DOT+JAVA;
        writeFile(SRC, file);
    }

    public void writeSource(String pack, String className) {
        String file = path(pack) + UNDER + className;
        writeFile(SRC, file);
    }
   
    private void doSpringXML() {
        if (springXML == null) return;
        isSpring=true;
        String file = springXML.name + SERVLETXML;
        addWeb(MVC,springXML.pack);
        writeFile(WEBINF, file);
        addWeb(JAVA,
        new String[] {PACKAGE,"classname","method","path"},
        new Object[] {springXML.pack,springXML.className,springXML.name,springXML.path});
        writeJava(springXML.pack, springXML.className);
    }
    public void addWeb(String key){addWeb(key,"");}
    public void addWeb(String key,String value){
        String text=swapMgr.getTemplate(key,isSpring? SPRINGWEB:WEB);
        text=freeMarker.tranWithText(text, VALUE, value);
        log(text);
        append(text);
    }
    
    public void addWeb(String key,String[] keys,Object[] value){
        String text=swapMgr.getTemplate(key,isSpring? SPRINGWEB:WEB);
        Map map=new Hashtable();
        int count=0;
        for (String key1:keys){
            map.put(key1,value[count]);
            count++;
        }
        text=freeMarker.tranWithText(text,map);
        log(text);
        append(text);
    }


    public void name(String value){addWeb(HEAD,value);   }
    public void welcome(String value) {addWeb(WELCOME,value);}

    

    public void spring(String value) {
        springXML = new SpringXML();
        Map<String, String> map = JasonParse.getMap(value);
        String currentName = map.get(NAME);
        springXML.name = currentName;
        springXML.resolve = map.get("resolve");
        springXML.pack = map.get(PACKAGE);
        springXML.className = map.get("class");
        springXML.path =map.get("url");
        String[] words = Util.split(map.get(MAP) + "/*", ",");
        Arrays.asList(words);
        isSpring=true;
        addWeb(SERVLET,
        new String[] {"currentname","classname","words"},
        new Object[] {currentName,springXML.className,Arrays.asList(words)});
        isSpring=false;
    }


    public void servlet(String value) {
        JasonData jasonData = JasonParse.jasonData(value);
        servlet = jasonData;
        for (String key : jasonData.getList()) {
            String text = jasonData.get(key);
            String currentName = text;
            String className = pack + SERVLETDOT + currentName;
            String[] words = Util.split(key + "/*", ",");
            Arrays.asList(words);
            addWeb(SERVLET,
            new String[] {"currentname","classname","words"},
            new Object[] {currentName,className,Arrays.asList(words)});
                
        }
    }

    private void writeServlet() {
        if (servlet == null) return;
        JasonData jd = servlet;
        for (String key : jd.getList()) {
            String text = jd.get(key);
            String currentName = text;
            addWeb(JAVA,currentName);
            writeJava(pack + DOT+SERVLET, currentName);
        }
        addWeb(INDEXSTART); 
        for (String key : jd.getList()) {
            addWeb(LINK,key);
            
        }
        addWeb(INDEXEND); 
        writeFile(INDEXHTML);
    }
    
    
    
    public  static void main(String[] args){
        //new ProcessWeb("webServlet");
        //new ProcessWeb("webSpring");
        //new ProcessWeb("hibernateconfig");
        new ProcessWeb("webSpring");
    }

    public static class SpringXML {
        public String name;
        public String resolve;
        public String pack;
        public String className;
        public String path;
        public Map getMap(){
           Map<String,String> map=new Hashtable();
           map.put("classname",className);
           map.put("package",pack);
           map.put("resolve",resolve);
           map.put("name",name);
           return map;
        }
    }


}
