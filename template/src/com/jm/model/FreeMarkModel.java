package com.jm.model;

import com.jm.mgr.FreeMarker;
import java.util.*;

/**
 * Created by John on 3/27/2017.
 */
public class FreeMarkModel {
    private Map<Object, Object> workMap    = new Hashtable();
    public static final String MARK        = "@@@";
    public static final String LCLASS      = "lclass";
    public static final String CLASS       = "class";
    public static final String PACKAGE     = "package";
    public static final String FIELDS      = "fields";
    public static final String MARKTEXT    = "mark";
    public static final String CLASSNAME   = "classname";
    public static final String PACKAGENAME = "packagename";
    public static final String TABLE       = "table";
    public static final String PRIMARY     = "primary";
    public static final String PACK        = "pack";
    public FreeMarkModel(Map map) {
        workMap = map;
    }

    public FreeMarkModel(String className, String packageName) {
        className=className==null? CLASSNAME:className;
        packageName=packageName==null? PACKAGENAME:packageName;
        workMap.put(LCLASS, className.toLowerCase());
        workMap.put(CLASS, className);
        workMap.put(PACKAGE, packageName);
    }

    public FreeMarkModel(String name) {workMap.put(TABLE, name);}
    public void put(String key, Object value) {workMap.put(key, value);}

    public String getANG(List list,String template) {
        FreeMarker fm = FreeMarker.instance();
        workMap.put(FIELDS, list);
        String myTemplate=fm.readTemplate("ang", template);
        return fm.tranWithText(myTemplate, workMap);
    }

    public String getResult(List list, String template) {
        workMap.put(FIELDS, list);
        workMap.put(MARKTEXT, MARK);
        FreeMarker fm = FreeMarker.instance();
        return fm.transByFileName(template, workMap);
    }

    public String getResult(String key, String value, String template) {
        workMap.put(key, value);
        FreeMarker fm = FreeMarker.instance();
        return fm.transByFileName(template, workMap);
    }

    public String getResult(String template) {
        FreeMarker fm = FreeMarker.instance();
        return fm.transByFileName(template, workMap);
    }


    public static String ofMap(String template, Map map) {
        FreeMarkModel fm = new FreeMarkModel(map);
        return fm.getResult(template);
    }


    public static FreeMarkModel ofClass(String className, 
                                        String packageName,
                                        Map map) {

        FreeMarkModel freeMarker=new FreeMarkModel(className, packageName);
        freeMarker.workMap.putAll(map);
        return freeMarker;
    }

    public static FreeMarkModel ofDB(String table, String primary) {
        FreeMarkModel fm = new FreeMarkModel(table);
        fm.put(PRIMARY, primary);
        return fm;
    }

    public static FreeMarkModel ofHibernate(String table, String pack, String className) {
        FreeMarkModel fm = new FreeMarkModel(table);
        fm.put(CLASSNAME, pack + "." + className);
        fm.put(PACK, pack);
        return fm;
    }

}
