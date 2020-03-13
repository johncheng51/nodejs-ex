package com.jm.mgr;

import com.jm.model.RenderModel;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

public class ThreadMgr {
    private static final String BEANNAME = "beanName";
    private static final String WRITER = "writer";
    private static final String RENDER = "render";
    private static final String WORKFOLDER = "workFolder";
    private static final String ANGVAR = "angvar";
    private static final String WORKMAP = "workmap";
    private ThreadLocal threadLocal = new ThreadLocal();

    private ThreadMgr() {}

    private Map<String, Object> map() {
        Map<String, Object> map = (Map<String, Object>) threadLocal.get();
        if (map == null)
            threadLocal.set(new Hashtable());
        map = (Map<String, Object>) threadLocal.get();
        return map;
    }

    public void setBeanName(String value) {
        map().put(BEANNAME, value);
    }

    public String getBeanName() {
        return (String) map().get(BEANNAME);
    }

    private StringBuilder writer(boolean clear) {
        StringBuilder sb = (StringBuilder) map().get(WRITER);
        if (sb == null || clear)
            sb = new StringBuilder();
        map().put(WRITER, sb);
        return sb;
    }

    public void append(String text) {
        writer(false).append(text);
    }

    public void createRenderModel(String type) {
        RenderModel renderModel = new RenderModel(type);
        map().put(RENDER, renderModel);
    }

    public RenderModel getRenderModel() {
        return (RenderModel) map().get(RENDER);
    }

    public String readText() {
        return readText(true);
    }

    public String readText(boolean include) {
        String result = writer(false) + "";
        writer(true);
        return result + (include ? "\n}" : "\n");
    }

    public void add(String key, String value) {
        Map<String, String> map = (Map<String, String>) map().get(WORKMAP);
        if (map == null) {
            map = new Hashtable();
            map().put(WORKMAP, map);
        }
        String data = map.get(key);
        if (data == null)
            map.put(key, value);
        else
            map.put(key, data + "\n" + value);
    }

    public Map<String, String> getMap() {
        Map<String, String> map = (Map<String, String>) map().get(WORKMAP);
        map().remove(WORKMAP);
        return map;
    }

    public void setWorkFolder(File folder) {
        map().put(WORKFOLDER, folder);
    }

    public File getWorkFolder() {
        return (File) map().get(WORKFOLDER);
    }

    public void setAngVar(String text) {
        map().put(ANGVAR, text);
    }

    public String getAngVar() {
        String text = (String) map().get(ANGVAR);
        map().remove(ANGVAR);
        return text;
    }

    private static Object lock = new Object();
    private static ThreadMgr mgr;

    public synchronized static ThreadMgr instance() {
        if (mgr != null) return mgr;
        synchronized (lock) {
            mgr = new ThreadMgr();
            return mgr;
        }
    }
}
