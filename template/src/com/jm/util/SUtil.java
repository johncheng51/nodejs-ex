package com.jm.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.lang.reflect.*;


public class SUtil 
{
    public static final String CTXHASHTABLE = "CTXHASHTABLE";
    public static final String escQQ = "\\\"";
    public static final String OPTION_SFX = "SFX";
    public static final String OPTION_PFX = "PFX";
    public static final String OPTION_SSN = "SSN_TIN_CODE";
    public static final String OPTION_MARITAL_STAT = "MARITAL_STAT";
    public static final String OPTION_SP_EST_ANN_INC = "SP_EST_ANN_INC";
    public static final String OPTION_COUNTRY = "COUNTRY";
    public static final String OPTION_STATE = "STATE";
    public static final String OPTION_ID_DOC_TYPE = "ID_DOC_TYPE";
    public static final String OPTION_NOT_AVAILABLE = "NOT_AVAILABLE";
    public static final String OPTION_EE_RELT_TYPE = "BENE_RELT";
    public static final String OPTION_NUM3 = "NUM3";
    public static final String OPTION_NUM30 = "NUM30";

    public static String merge(Vector v, String fields) 
  {
        Field field = null;
        String s0 = null;
        StringBuffer sb = new StringBuffer();
        String[] fa = Util.split(fields, "|");
        for (int j = 0; j < v.size(); j++) {
            Object o = v.get(j);
            Class oc = o.getClass();
            for (int i = 0; i < fa.length; i++) {
                s0 = "";
                try {
                    field = oc.getField(fa[i]);
                    s0 = (String)field.get(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sb.append(strToHex(s0));
                if (i != fa.length - 1)
                    sb.append("|");
            }
            if (j != v.size() - 1)
                sb.append("^^^");
        }
        return sb.toString();
    }
   
    public static String hexToStr(String s) {
        StringBuffer sb = new StringBuffer();
        int n = 0;
        String s0;
        for (int i = 0; i < s.length() / 2; i++) {
            s0 = s.substring(i * 2, i * 2 + 2);
            n = Integer.parseInt(s0, 16);
            sb.append(new Character((char)n));
        }
        return sb.toString();
    }

    public static String strToHex(String s) {
        char c;
        if (s == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

   public static String merge(String[] aa) {
        String result = "";
        for (int i = 0; i < aa.length; i++) {
            String[] bb = Util.split(aa[i], "|");
            result += bb[0] + "|";
        }
        return Util.removeLast(result, 1);
    }

    /*public static Vector makeVector(String[] sa) {
        Vector v = new Vector();
        for (int i = 0; i < sa.length / 2; i++) {
            LookupList ll = new LookupList();
            ll.key = sa[2 * i];
            ll.desc = sa[2 * i + 1];
            v.add(ll);
        }
        return v;
    }

    public static Vector makeVector(String s) {
        Vector v = new Vector();
        String ss = "|" + s;
        String[] sa = Util.split(ss, "|");
        for (int i = 0; i < sa.length; i++) {
            LookupList ll = new LookupList();
            ll.key = sa[i];
            ll.desc = sa[i];
            v.add(ll);
        }
        return v;
    }*/

   public static String printBeanMsg(Exception e, char type, Object bean, 
                                      String name) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String et = "";
        switch (type) {
        case 'G':et = "Get Method";break;
        case 'g':et = "Get Method with arg of int"; break;
        case 's':et = "Set Method"; break;
        }
        return "Can not Find Method name=" + name + "<BR>bean_Class=" + 
            bean.getClass() + "<BR>type=" + et + "<BR>" + 
            sw.toString().substring(0, 400);
    }


    /* public static Vector createDummy(int n) {
        Vector v = new Vector();
        String str = "";
        for (int i = 0; i < n; i++)
            str += "-";
        LookupList lk = new LookupList();
        lk.key = str;
        lk.desc = str;
        v.add(lk);
        return v;
    }

    public static String getValue(Vector v)
    {
        LookupList lk = (LookupList)v.get(0);
        return lk.key;
    }*/

   
    public static synchronized Hashtable getContext(String key) {
        Hashtable ht = getTable();
        Hashtable ctx = (Hashtable)ht.get(CTXHASHTABLE);
        return (Hashtable)ctx.get(key);
    }

   private static Hashtable utilTable = null;
   private static synchronized Hashtable getTable() {
        if (utilTable == null) {
            utilTable = new Hashtable();
            //makeSample();
            Hashtable ct = new Hashtable();
            utilTable.put(CTXHASHTABLE, ct);
            loadConfig();
        }
        return utilTable;
    }

   private static void loadConfig() {
        //putGbContext("table","cellSpacing=0 cellPadding=0 border=0");
        //putGbContext("row","class=signupcellcolor");
        //putGbContext("label.td","class=signuptext vAlign=bottom align=left");
        //putGbContext("div.td","class=signuptext  vAlign=bottom align=left");
        //putGbContext("array.td","class=signuptext vAlign=bottom align=left");
        //putGbContext("text","onblur=validate(this.form,{$jstable},{$id})");
    }


    /*public static void makeSample() {
        createVector(OPTION_PFX, 
                     new String[] { "blank", " ", "Mr.", "Mr.", "Mrs.", "Mrs.", 
                                    "Dr.", "Dr." });
        createVector(OPTION_SFX, 
                     new String[] { "blank", " ", "Jr.", "Jr.", "Sr.", "Sr.", 
                                    "II", "II", "III", "III", "IV", "IV", "V", 
                                    "V", "VI", "VI", "VII", "VII" });
        createVector(OPTION_SSN, 
                     new String[] { "blank", " ", "SSN", "SSN", "TIN", 
                                    "TIN" });
        createVector(OPTION_EE_RELT_TYPE, 
                     new String[] { "blank", " ", "Brother", "Brother", 
                                    "Daughter", "Daughter", "Daughter-in-law", 
                                    "Daughter-in-law", "Father", "Father", 
                                    "Son", "Son", "Father-in-law", 
                                    "Father-in-law", "Mother-in-law", 
                                    "Mother-in-law", "Husband", "Husband" });
        createVector(OPTION_NUM3, 
                     new String[] { " ", " ", "0", "0", "1", "1", "2", "2", 
                                    "3", "3" });
        createVector(OPTION_NUM30, 
                     new String[] { "1", "1", "2", "2", "3", "3" });
    }*/

   /* private static void createVector(String name, String[] sa) {
        Vector v = new Vector();
        for (int i = 0; i < sa.length / 2; i++) {
            LookupList ll = new LookupList();
            ll.key = sa[2 * i];
            ll.desc = sa[2 * i + 1];
            v.add(ll);
        }
        save(name, "", v);
    }*/

   public static void save(Hashtable h) {
        Hashtable ht = getTable();
        ht.putAll(h);
    }

    public static void save(String name, String userType, Vector v) {
        Hashtable ht = getTable();
        String sname = name + "." + userType;
        ht.put(sname.toLowerCase(), v);
    }

    /**
     * put global context here
     * with different element type
     */
    public static void putGbContext(String key, String value) {
        Hashtable t = Util.readHash(value);
        Hashtable ht = getTable();
        Hashtable ct = (Hashtable)ht.get(CTXHASHTABLE);
        ct.put(key.toLowerCase(), t);
    }

     /**
     * put all
     */
    public static void putGbContext(Hashtable ht) {
        Enumeration en = ht.keys();
        while (en.hasMoreElements()) {
            String key = (String)en.nextElement();
            putGbContext(key, (String)ht.get(key));
        }
    }



}