package com.jm.mgr;


import com.jm.util.Util;
import com.jm.xml.LineHash;
import java.util.*;


public class UIrenderMgr implements DoUpdate {
    private static final String ALL    = "all";
    private static final String METHOD = "method";
    private Map<String, Map<String, String>> renderMap = new Hashtable();
    private static final String RENDER = "render";
    private FileMgr fileMgr = FileMgr.instance();

    private UIrenderMgr() {}
    private void init() { }
    private synchronized Map<String,String> load(String template) {
        Map<String, String> map = renderMap.get(template);
        if (map != null) return map;
        String text = fileMgr.readFile(RENDER,template+SwapMgr.TXT);
        if (Util.isBlank(text)) return null;
        renderMap.put(template, LineHash.getMap(text));
        return renderMap.get(template);
    }

    private String get(String template, String subKey) {
        Map<String, String> map = load(template);
        if (map == null) map = load(ALL);
        String text = map.get(subKey);
        if (text==null){
            map = load(ALL);
            text = map.get(subKey);
        }
        String message = "Can not find the key [" + subKey + 
                         "] in the map " + "under file with render." + template;
        if (text == null) {
            log(message);
            return "";
        }
        return text;
    }

    public String tranl(String mainKey, String subKey, Map<String, String> map) {
        String temp = get(mainKey, subKey);
        return FreeMarker.instance().tranWithText(temp, map);
    }

    public String getMethod(String key, Map<String, String> map) {
        Map<String, String> cmap = load(METHOD);
        String text = cmap.get(key);
        return FreeMarker.instance().tranWithText(text, map);
    }

    private void log(String message) {
        System.out.println(message);
    }

    private static UIrenderMgr mgr;
    private static Object lock = new Object();

    public synchronized static UIrenderMgr instance() {
        if (mgr != null) return mgr;
        synchronized (lock) {
            mgr = new UIrenderMgr();
            mgr.init();
            return mgr;
        }
    }

    @Override
    public void update() {
        // TODO Implement this method
    }
}
