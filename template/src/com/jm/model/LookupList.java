package com.jm.model;

import com.jm.json.JsonStyle;
import java.util.Vector;


public class LookupList implements Cloneable, JsonStyle, Comparable {
    public String value = "";
    public String desc = "";

    public LookupList() {}

    public LookupList(String db_val, String desc) {
        this.value = db_val;
        this.desc = desc;
    }

    public LookupList(String db_val) {
        this(db_val, db_val);
    }

    public static LookupList create(String text) {
        return new LookupList(text);
    }

    public static LookupList create(String db_val, String desc) {
        return new LookupList(db_val, desc);
    }

    public static Vector createBlank() {
        Vector v = new Vector();
        v.add(new LookupList("", "Please Pick One"));
        return v;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            return null;
        }
    }

    public static Vector make(String[] sa) {
        Vector v = createBlank();
        for (String key : sa) {
            LookupList ll = new LookupList(key);
            v.add(ll);
        }
        return v;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int compareTo(Object o) {
        LookupList ll = (LookupList) o;
        return ll.getValue().compareTo(this.getValue());

    }
}
