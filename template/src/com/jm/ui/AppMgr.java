package com.jm.ui;


import com.jm.gen.ModelGen;
import com.jm.model.TextMap;
import com.jm.xml.XmlBlockForUI;
import com.jm.xml.XmlList;
import java.util.*;
import com.jm.util.*;

public class AppMgr {
    private Map<String, String> strMap = new Hashtable();
    private Map<String, UILayout> eleMap = new Hashtable();
    private Map<String, Object> cssMap = new Hashtable();
    private Map<String, String> macroMap = new Hashtable();
    private Map<String, Object> propMap = new Hashtable();
    private Set<String> skipSet = new HashSet();
    private Map<String, Map<String, String>> renderMap = new Hashtable();
    public static final String FOLDER = ".folder";
    public static final String ALL    = "all";
    public static final String MAP = "map";
    public static final String CONFIGFolder = "config";
    public static final String MAPTABLE = "mapTable.xml";
    public static final String UICONFIG = "uiconfig.xml";
    public static final String MACRO = "macro";
    public static final String CSS = "css";
    public static final String PROP = "prop";
    public static final String RENDER = "render";
    public static final String SKIP = "skip.";
    private UIFileMgr fileMgr = UIFileMgr.instance();
    private boolean haveRead = false;

    private void init() {
        XmlBlockForUI block = readCSS();
        XmlBlockForUI rm = block.getBlock(RENDER);
        rm.getMap().keySet().forEach((key) -> {
                Map<String, String> map = new Hashtable();
                Arrays.asList(rm.getArray(key)).forEach(line -> { Arrays.asList(Util.split(line, ",")).forEach(word -> {
                            String[] pair = Util.split(word, "->");
                            map.put(pair[0], pair[1]);
                        }); });
                renderMap.put(key, map);

            });
        getSkip();
    }

    private void getSkip() {
        for (int i = 0;; i++) {
            String text = getP(SKIP + i);
            if (text == null)   break;
            String[] words = Util.split(text, ",");
            for (String word : words)
                skipSet.add(word.toLowerCase());
        }
    }

    public boolean isSkip(String key) {
        final String mkey=key.toLowerCase();
        boolean yes= skipSet.stream().anyMatch(x->{
            return mkey.startsWith(x);});
        return yes;
                                       
    }

    public String tranl(String group, String key) {
       Map<String,String> map= renderMap.get(group);
       if (map==null) return key;
       String value=map.get(key);
       return value!=null? value:key;
    }

    public String findMacro(String key) {
        String alt = macroMap.get(key);
        if (alt == null) return key;
        else   return alt;
    }


    private XmlBlockForUI readCSS() {
        String text = fileMgr.readFile(CONFIGFolder, UICONFIG);
        XmlBlockForUI block = new XmlBlockForUI(text);
        String[] lines = block.getArray(CSS);
        cssMap = getMap(lines, true);
        lines = block.getArray(PROP);
        propMap = getMap(lines, false);
        String[] macros = block.getArray(MACRO);
        for (String line : macros) {
            if (Util.isBlank(line))
                continue;
            String[] pair = Util.split(line, "->");
            macroMap.put(pair[0].trim(), pair[1].trim());
        }
        return block;

    }


    private Map<String, Object> getMap(String[] lines, boolean isMap) {
        Map<String, Object> result = new Hashtable();
        for (String line : lines) {
            if (Util.isBlank(line))  continue;
            int n = line.indexOf("=");
            String key0 = line.substring(0, n);
            String value = line.substring(n + 1);
            Object object = isMap ? WUtil.readHash(value) : value;
            result.put(key0.toLowerCase().trim(), object);
        }
        return result;
    }

    public Map<String, String> getContext(String key) {
        return (Map<String, String>) cssMap.get(key);
    }


    private void readMap() {
        if (haveRead)   return;
        String text = fileMgr.readFile(CONFIGFolder, MAPTABLE);
        XmlList xmlList = new XmlList(text, MAP);
        Map<String, TextMap> map = xmlList.getTextmTable();
        for (String key : xmlList.getList()) {
            TextMap textMap = map.get(key);
            add(key, textMap);
        }
        haveRead = true;
    }

    private void add(String key, TextMap textMap) {
        UIParser uiParser = UIParser.instance();
        String type = textMap.getMap().get("type");
        String cmdTable = "s string c element t template";
        char c = Util.findCommand(cmdTable, type);
        Object result = null;
        switch (c) {
        case 'c':
            eleMap.put(key, uiParser.compile(textMap.getText()));
            break;
        case 's':
            strMap.put(key, textMap.getText());
            break;
        case 't':
            result = new UITempl(textMap.getText());
            eleMap.put(key, (UILayout) result);
            break;
        }
    }

    public UILayout getLayout(String name, String param) {
        readMap();
        UIParser uiParser = UIParser.instance();
        param = (param == null) ? "" : param;
        UILayout layout = eleMap.get(name + param);
        if (layout == null) {
            String text = strMap.get(name);
            text = replaceMacro(param, text);
            if (text == null) {

                return uiParser.create(UILayout.UI_ERROR, name + " is not exist in MAP table", name);
            }
            layout = uiParser.compile(text);
            eleMap.put(name + param, layout);
        }
        UILayout c1 = (UILayout) layout.clone();
        return c1;
    }

    public String[] getAllMap() {
        readMap();
        String[] sa = new String[strMap.size()];
        strMap.keySet().toArray(sa);
        return sa;
    }


    public String getFolder(String key) {
       String result=getP(key+FOLDER);
       result=(result==null)? getP(ALL+FOLDER):result;
       return result;
    }
    
    public String getP(String key) {
        return (String) propMap.get(key);
    }


    public static String replaceMacro(String values, String s) {
        int on, nn;
        String result = s;
        String sub = null;
        String[] sa = split(values);
        while (true) {
            on = result.indexOf("###");
            if (on == -1)
                break;
            nn = result.charAt(on + 3) - '0';
            sub = sa.length < (nn + 1) ? "" : sa[nn];
            result = result.substring(0, on) + sub + result.substring(on + 4);
        }
        return result;
    }

    private static String[] split(String s) {
        if (s == null)
            return new String[0];
        return WUtil.split(s, "|");
    }
    private static AppMgr mgr;
    private static Object lock = new Object();

    public synchronized static AppMgr service() {
        if (mgr != null)
            return mgr;
        {
            synchronized (lock) {
                if (mgr != null)
                    return mgr;
                mgr = new AppMgr();
                mgr.init();
                return mgr;
            }
        }
    }

    public ModelGen getModel() {
        return null;
    }


}


