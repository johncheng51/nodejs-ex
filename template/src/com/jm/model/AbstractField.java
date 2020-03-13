package com.jm.model;

import com.jm.util.Util;
import java.util.*;

public abstract class AbstractField {


    protected String name;
    protected String nameRest;
    protected String type;
    protected boolean _isObj;
    protected String objName = "";
    protected String classType;
    protected AbstractModel model;
    protected static Map<String, Integer> map = new Hashtable();
    protected List<ClassField> myList=new ArrayList();

    public AbstractField(AbstractModel model,String name, String value) {
        this.model=model;
        getName(name);
        if (!_isObj) processField(nameRest, value);
        else processObject(nameRest, value);
    }

    protected String getIndex(String key) {
        Integer index = map.get(key);
        if (index == null) index = 0;
        map.put(key, index + 1);
        if (index == 0) return key;
        else return key + index;
    }

    protected void getName(String data) {
        String myName = Util.getLast(data, ".");
        int n = data.indexOf(myName);
        nameRest = (n <= 0) ? "" : data.substring(0, n - 1);
        if (myName.startsWith("$")) {
            name = myName.substring(1);
            _isObj = true;
        } else name = myName;
    }

    protected abstract void processField(String rest, String value);

    protected abstract void processObject(String nameRest, String value);


    protected String q(String text) {
        return "'" + text + "'";
    }

    protected String getInt(String text) {
        if (text.length() < 1) return null;
        text = text.substring(1);
        try {
            text = Integer.parseInt(text) + "";
            return text;
        } catch (Exception e) {
            text = null;
            return text;
        } 
       
    }

    protected String[] splitCommon(String text) {
        return Util.split(text, ",");
    }

    protected String[] splitd(String text) {
        return Util.split(text, ".");
    }

    protected String cap(String text) {
        if (text.length() == 0) return text;
        return Util.cap(text.toLowerCase());
    }

    public boolean isObj() {
        return _isObj;
    }

    public String getUname() {
        return getName().toUpperCase();
    }

    public String getLname() {
        return getName().toLowerCase();
    }

    public String getCname() {
        return getName().toUpperCase().charAt(0) + getName().substring(1);
    }

    public String getType() {
        if (isObj()) return classType;
        return type == null ? "NULL" : type;
    }

    public String getLtype() {
        return type.toLowerCase();
    }

    public String objName() {
        return objName;
    }

    public String getName() {

        if (isObj()) return objName;
        else return name;
    }


/**
 *  Setup Map for FreeMarker
 */


}
