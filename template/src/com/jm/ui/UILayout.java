package com.jm.ui;
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import javax.servlet.http.*;
import com.jm.util.*;

public class UILayout extends AbstractUI implements Cloneable {
  

    public UILayout() {}  
    public UILayout(String t) {
        type = t.toLowerCase();
        if (!checkSymbol(type)) {
            orgType = type;
            type = UILayout.UI_ERROR;
            haveError=true;
        }
    }
    

    public void setValidateStr(String key, String s) {
        UILayout[] ca = findAllObjects(key);
        executeLoop(ca, s, "setValidateStr");
    }

    public void setTextLocation(int n) {
        textLocation = n;
    }

    public int getTextLocation() {
        return textLocation;
    }

    public ArrayList<UILayout> getList() {
        return list;
    }

    public String getAtts() {
        return attributes;
    }

    public void clearAtts() {
        attributes = "";
    }


    public String getTabName() {
        return tabName;
    }

    public void setTabName(String s) {
        tabName = s;
    }

    public void setRequire(String s) {
        require = s;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void clearList() {
        list = null;
    }

    public void clearTree() {
        objHash = null;
    }


    public Hashtable getObjHash() {
        UILayout root = getRoot();
        if (root.objHash == null)
            root.createObjName();
        return root.objHash;
    }

    public Hashtable getNameHash() {
        Hashtable oh = getObjHash();
        return (Hashtable) oh.get(TABLE_NAME);
    }


    public void setTitle(String s) {
        title = s;
    }

    public void setTitle(String key, String s) {
        UILayout[] ca = findAllObjects(key);
        for (int j = 0; j < ca.length; j++)
            ca[j].title = s;
    }

    /**
     * Get the root of UILayout
     */
    private UILayout getRoot() {
        int count = 0;
        UILayout c = this;
        while (true) {
            if (c.parent == null)   return c;
            c = c.parent;
            count++;
            if (count > 30)
                catchException("Error finding root");
        }
        //return null;
    }

    /**
     * create root object serving for
     * child element
     */
    public void createObjName() {
        removeNameTable();
        objHash = new Hashtable();
        Hashtable nameHash = new Hashtable();
        objHash.put(TABLE_NAME, nameHash);
        prepareKeys();
        getLogicKeys();
        String[] sa = getValidKeys();
        objHash.put(TABLE_VALIDKEY, sa);
        Hashtable h = new Hashtable();
        objHash.put(MACRO, h);
        addMacroAtts();
    }

    /**
     * remove all childs name table
     */
    public void removeNameTable() {
        parentName = null;
        for (int i = 0; i < size(); i++) {
            UILayout c = list.get(i);
            c.parent = this;
            c.removeNameTable();
            objHash = null;
        }
    }

    private void addMacroAtts() {
        Hashtable macro = getMacroTable();
        Map<String, String> h = WUtil.readHash(macroAtts);
        macro.putAll(h);
        for (int i = 0; i < size(); i++)
            list.get(i).addMacroAtts();

    }

    private Hashtable getMacroTable() {
        Hashtable objHash = getObjHash();
        return (Hashtable) objHash.get(MACRO);
    }


    public void setFlagStr(String value) {
        flagStr = value;
    }

    public void setFlagStr(String key, String s) {
        UILayout[] ca = findAllObjects(key);
        for (int j = 0; j < ca.length; j++)
            ca[j].flagStr = s;
    }

    public String getFlagStr() {
        if (flagStr.length() == 0)
            return "NO";
        else
            return flagStr;
    }


    public void setComment(String s) {
        comment = s;
    }


    public void setSuffix(String s) {
        if (size() == 0) {
            suffix = s;
            return;
        }
        for (int i = 0; i < size(); i++) {
            UILayout UILayout = (UILayout) list.get(i);
            UILayout.setSuffix(s);
        }
    }


    public void setCol(int n) {
        addTdAttsNthChild("colspan=" + n, 0);
    }

   

    private String[] getKeys() {
        Hashtable obj = getObjHash();
        return (String[]) obj.get(TABLE_VALIDKEY);
    }


    public UILayout add(UILayout c) {
        checkList();
        list.add(c);
        return this;
    }

    public UILayout add(UILayout c0, UILayout c1) {
        checkList();
        list.add(c0);
        list.add(c1);
        return this;
    }

    public void add(UILayout[] ca) {
        checkList();
        for (int i = 0; i < ca.length; i++)
            list.add(ca[i]);
    }


    /**
     * add object at location i position
     * when i=-1 it would insert at first
     * @param i
     */
    public UILayout addAt(UILayout c, int i) {
        checkList();
        list.add(i, c);
        return this;
    }

    public UILayout add(int i, int j, UILayout c0) {
        UILayout c = list.get(i);
        c.addAt(c0, j);
        return this;
    }

    public UILayout add(int i, UILayout c0) {
        UILayout c = list.get(i);
        c.add(c0);
        return this;
    }

    public UILayout addChild(UILayout c) {
        checkList();
        list.addAll(c.list);
        return this;
    }

    public void remove(int i, int n) {
        for (int j = i; j < i + n; j++)
            list.remove(i);
    }

    public void remove(UILayout c) {
        list.remove(c);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toLowerCase();
    }

    public String getName() {
        return getName(this) + suffix;
    }

    public String getName(UILayout c) {
        Hashtable oh = getObjHash();
        String sname = (String) oh.get(c);
        if (c.type.equals(UILayout.UI_RADIOBUTTON))
            sname = removeSuffix(sname);
        sname=(sname == null)? c.gname():sname;
        sname=(Util.isBlank(sname))? c.type:sname;
        return sname;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    

    public String getValue() {
        return gvalue();
    }

    

    /**
     * Here we have several type of set
     * value function
     * setValues(String keys,String values)
     * setValues(String keys,Vector values)
     * setValue(String)
     * setValue(Hashtable)
     * setValue(String[])
     * setValue(String name,String value)
     * setValue(String name,String[] value)
     * setValue(String name,Vector value)
     * setValue(Object value)
     */
    public void setValues(String fields, String values) {
        String[] ks = WUtil.split(fields, "|");
        String[] vs = WUtil.split(values, "|");
        for (int i = 0; i < ks.length; i++)
            setValue(ks[i], vs[i]);
    }

    public void setValues(String fields, Vector v) {
        String[] ks = WUtil.split(fields, "|");
        for (int i = 0; i < ks.length; i++)
            setValue(ks[i], v.get(i));
    }

    

    public void setValue(Vector v, String name, int start) {
        Object obj = null;
        for (int i = 0; i < v.size(); i++) {
            obj = v.get(i);
            String prefix = name + (start + i) + "_";
            setValue(obj, prefix);
            loadLogic(obj, prefix);
        }
    }

    public void setValue(String name, String value) {
        UILayout[] ca = findAllObjects(name);
        executeLoop(ca, value, "setValue");
    }

    public void setData(String name, Vector v) {
        UILayout c = this.findObject(name, false);
        c.setValue(v);
    }

    public void loadObject(Object obj, String prefix) {
        Hashtable nameHash = getNameHash();
        String value = null;
        Vector vValue = null;
        String key = null, skey = null, ckey = null;
        UILayout c = null;
        String[] fields = getSchemaFields(obj);
        boolean isSelect = false;
        for (int i = 0; i < fields.length; i++) {
            key = fields[i];
            skey = fields[i] + "_DATA";
            ckey = prefix + fields[i];
            c = (UILayout) nameHash.get(ckey);
            if (c == null || c.type.equals(UILayout.UI_CHECKBOX) ||
                c.type.equals(UILayout.UI_RADIOBUTTON))
                continue;
            isSelect = c.type.equals(UILayout.UI_SELECT) ? true : false;
            vValue = null;
            value = null;
            value = getCString(obj, fields[i]);
            c.setValue(ckey, value);
            if (isSelect) {
                vValue = getCVector(obj, skey);
                c.setValue(ckey, vValue);
            }
        }
        loadObjectLogic(obj, prefix);
    }

    public void loadObjectLogic(Object o, String prefix) {
        String[] fields = getSchemaFields(o);
        for (int i = 0; i < fields.length; i++) {
            String key = fields[i];
            String value = getCString(o, fields[i]);
            setLogic(prefix + key, value);
        }
    }

    private String getCString(Object o, String field) {
        String value = WUtil.getSProperty(o, field);
        return value;
    }

    private Vector getCVector(Object o, String field) {
        Vector value = (Vector) WUtil.getProperty(o, field);
        return value;
    }


    private String[] getSchemaFields(Object obj) {
        String result = "";
        try {
            Field f = obj.getClass().getDeclaredField("schema_fields");
            result = (String) f.get(obj);
        } catch (Exception e) {
            return null;
        }
        String[] obFields = WUtil.split(result, "|");
        String[] dbFields = new String[obFields.length];
        for (int i = 0; i < obFields.length; i++) {
            String[] aa = WUtil.split(obFields[i], "^");
            boolean have2 = aa.length > 1;
            obFields[i] = have2 ? aa[0] : obFields[i];
            dbFields[i] = have2 ? aa[1] : obFields[i];
        }
        return obFields;
    }


    public void setValue(Object obj, String sname) {
        String aname = sname;
        Hashtable nameHash = getNameHash();
        String value = null;
        Vector vValue = null;
        String key = null;
        String skey = null;
        Field field = null;
        UILayout c = null;
        if (sname == null || sname.length() == 0)
            aname = gname();
        Field[] fa = WebSession.getFields(obj);
        boolean isSelect = false;
        for (int i = 0; i < fa.length; i++) {
            field = fa[i];
            key = sname + field.getName();
            skey = field.getName() + "_DATA";
            c = (UILayout) nameHash.get(key);
            if (c == null || c.type.equals(UILayout.UI_CHECKBOX) ||
                c.type.equals(UILayout.UI_RADIOBUTTON))
                continue;
            isSelect = c.type.equals(UILayout.UI_SELECT) ? true : false;
            try {
                vValue = null;
                value = null;
                value = (String) field.get(obj);
                if (isSelect) {
                    field = obj.getClass().getField(skey);
                    vValue = (Vector) field.get(obj);
                }
            } catch (Exception e) {
            }
            if (value != null)
                c.setValue(value);
            if (vValue != null)
                c.setValue(vValue);
        }
        loadLogic(obj, sname);
    }

    /**
     * @param bean   copy bean to UILayout value
     * @param fields field separated by "|"
     * @param prefix
     */

    public void setValue(Object bean, String fields, String prefix) {
        String key = "";
        String pf = (prefix == null || prefix.equals("")) ? "" : prefix + "_";
        String[] sa = WUtil.split(fields, "|");
        for (int i = 0; i < sa.length; i++) {
            key = pf + sa[i];
            String value = WUtil.getSProperty(bean, sa[i]);
            setValue(sa[i], value);
        }
    }

    /**
     *
     * @param bean   copy bean to UILayout logic
     * @param fields
     * @param prefix
     */
    public void setLogic(Object bean, String fields, String prefix) {
        String key = "";
        String pf = (prefix == null || prefix.equals("")) ? "" : prefix + "_";
        String[] sa = WUtil.split(fields, "|");
        for (int i = 0; i < sa.length; i++) {
            key = pf + sa[i];
            String value = WUtil.getSProperty(bean, sa[i]);
            setLogic(sa[i], value);
        }
    }


    /**
     * se for option select data,or radio button
     * if object is Vector then set select data
     * or object is Boolean then set for logical data
     */
    public void setValue(String name, Object value) {
        UILayout[] ca = findAllObjects(name);
        for (int i = 0; i < ca.length; i++) {
            if (value instanceof String)
                ca[i].setValue((String) value);
            else if (value instanceof Vector)
                ca[i].setValue((Vector) value);
            else if (value instanceof Boolean)
                ca[i].setValue((Boolean) value);
        }
    }

    public void setValue(Vector value) {
        selectData = value;
    }

    public void setValue(Boolean value) {
        logical = value.booleanValue();
    }

    public void setValue(Hashtable h) {
        Enumeration en = h.keys();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            UIData data = (UIData) h.get(key);
            if (data.isLogic()) setLogic(key, data.value);
            else setValue(key, data.value);
            if (data.isSelect()) setValue(key, data.list);
        }
    }

    public void loadLogic(Object o, String sname) {
        Field field = null;
        Field[] fa = WebSession.getFields(o);
        for (int i = 0; i < fa.length; i++) {
            field = fa[i];
            String key = sname + field.getName();
            try {
                setValue((String) field.get(o));
            } catch (Exception e) {
            }
            setLogic(key, gvalue());
        }
    }

    public void setLogic(String name, String value) {
        UILayout c = null;
        Hashtable nameHash = getNameHash();
        String sn = findRadio(name, value);
        if (sn != null) {
            c = (UILayout) nameHash.get(sn);
            c.setValue(new Boolean(true));
        }
        sn = findCheck(name);
        if (sn != null) {
            c = (UILayout) nameHash.get(sn);
            boolean flag = value.equalsIgnoreCase("YES") || value.equalsIgnoreCase("Y");
            c.setValue(new Boolean(flag));
        }
    }

    private String findCheck(String name) {
        Hashtable oh = getObjHash();
        String[] checks = (String[]) oh.get(TABLE_CHECK);
        for (int i = 0; i < checks.length; i++) {
            if (!checks[i].toLowerCase().endsWith(name.toLowerCase()))
                continue;
            return checks[i];
        }
        return null;
    }

    /**
     * Hashtable objHash.get(TABLE_RADIO) have all full radio's  name
     * find the name of radio that match with value
     * @return the full name of
     * example: ("bb_","Yes")
     * @param value
     * @param name: prefix name
     */
    private String findRadio(String name, String value) {
        Hashtable oh = getObjHash();
        Hashtable nh = getNameHash();
        String[] radios = (String[]) oh.get(TABLE_RADIO);
        for (int i = 0; i < radios.length; i++) {
            if (!radios[i].toLowerCase().startsWith(name.toLowerCase()))
                continue;
            UILayout c = (UILayout) nh.get(radios[i]);
            if (c.gvalue().equalsIgnoreCase(value))
                return radios[i];
        }
        return null;
    }

    /**
     *
     * @return all the name of radiobutton with partial name
     * @param name
     */
    private String[] findRadio(String name) {
        Hashtable oh = getObjHash();
        String[] result = null;
        HashSet hs = new HashSet();
        String[] radios = (String[]) oh.get(TABLE_RADIO);
        for (int i = 0; i < radios.length; i++) {
            int n = radios[i].lastIndexOf("_");
            if (n == -1)
                continue;
            String rname = radios[i].substring(0, n);
            if (!rname.toLowerCase().endsWith(name.toLowerCase()))
                continue;
            hs.add(radios[i]);
        }
        result = new String[hs.size()];
        hs.toArray(result);
        return result;
    }

    public void setRequire(String key, String s) {
        UILayout[] ca = findAllObjects(key);
        for (int j = 0; j < ca.length; j++)
            ca[j].require = s;
    }

   
    public void setReqpt(String name, String s) {
        UILayout[] ca = findAllObjects(name);
        executeLoop(ca, s, "setReqpt");
    }


    private void catchException(String msg) {

        try {
            throw new Exception();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            StringBuffer errorReport = uiRender.getErrorReport();
            errorReport.append("<BR><B>" + msg + "</B><BR>\n");
            e.printStackTrace(new PrintWriter(sw));
            String result = sw.toString();
            if (result.length() > 600)
                result = result.substring(0, 600);
            errorReport.append(result);
        }
    }

    private String getAllNames(String key) {
        Hashtable objH = getObjHash();
        String result = "";
        UILayout[] ca = findAllObjects(key);
        for (int i = 0; i < ca.length; i++)
            result += (String) objH.get(ca[i]) + ",";
        result = removeLast(result);
        return result;
    }

    private String getAllNameBG(String key) {
        String result = "";
        Hashtable nameH = getNameHash();
        String[] sa = getValidKeys();
        for (int i = 0; i < sa.length; i++) {
            if (!sa[i].startsWith(key))
                continue;
            UILayout c = (UILayout) nameH.get(sa[i]);
            if (c == null)
                continue; // is radio type
            result += "" + c.id + ",";
        }
        result = removeLast(result);
        return result;
    }

    private String removeLast(String s) {
        if (s == null || s.length() == 0)
            return s;
        return s.substring(0, s.length() - 1);
    }

    private String removeSuffix(String text) {
        int n = text.lastIndexOf("_");
        if (n>0) return text.substring(0, n);
        else return text;
    }

    private String checkRadio(String s) {
        int n = s.lastIndexOf("_radio");
        boolean isRadio = n >= 0;
        if (isRadio)
            return s.substring(0, n);
        else
            return null;
    }

    /**
     * @param str: find the object with match
     * @return
     */
    public UILayout findObject(String key, boolean isGroup) {
        String s = findKey(key, isGroup);
        if (s == null) {
            log("XXXXX Can not find the key=" + key);
            return null;
        }
        Hashtable h = getNameHash();
        UILayout c = (UILayout) h.get(s);
        return c;
    }

    public UILayout[] findAllObjects(String sname) {
        if (sname == null)
            return null;
        String rname = checkRadio(sname);
        boolean isRadio = rname != null;
        Vector v = new Vector();
        Hashtable nameH = getNameHash();
        String[] sa = null;
        if (isRadio)
            sa = findRadio(rname);
        else
            sa = getAllKeys();
        if (sa.length == 0)
            log("XXXXX Can not find the key=" + sname);
        for (int i = 0; i < sa.length; i++) {
            if (!isRadio && !sa[i].toLowerCase().endsWith(sname.toLowerCase()))
                continue;
            UILayout c = (UILayout) nameH.get(sa[i]);
            v.add(c);
        }
        UILayout[] ca = new UILayout[v.size()];
        v.copyInto(ca);
        return ca;
    }

    private String findKey(String sname, boolean inGroup) {
        if (sname == null)
            return null;
        String[] sa = null;
        String rname = checkRadio(sname);
        boolean isRadio = rname != null;
        if (isRadio) {
            sa = findRadio(rname);
            return sa[0];
        }
        String gname = null;
        Hashtable nameH = getNameHash();
        sa = getAllKeys();
        for (int i = 0; i < sa.length; i++) {
            if (!sa[i].toLowerCase().endsWith(sname.toLowerCase()))
                continue;
            UILayout c = (UILayout) nameH.get(sa[i]);
            if (inGroup) {
                gname = c.group;
                if (!gname.equals(group))
                    continue;
            }
            return sa[i];
        }
        return null;
    }

    /**
     * set Context for the type
     */
    public void setContext(String allKey, String value) {
        if (context == null)  context = new Hashtable();
        String[] sa = WUtil.split(allKey, ",");
        for (int i = 0; i < sa.length; i++) {
            String key = sa[i];
            Hashtable ah = (Hashtable) context.get(key);
            if (ah == null) {
                ah = new Hashtable();
                context.put(key, ah);
            }
            Map<String, String> sh = WUtil.readHash(value);
            ah.putAll(sh);
        }
    }


    public UILayout[] getObject(String name) {
        UILayout[] c = findAllObjects(name);
        return c;
    }


    /**
     * Methods to add Attributes for component
     * addAtt(String key,String value)
     * addAtt(String[] key-value-pair)
     * addAtts(String  key-value-pair)
     * addAttChild(String key,String[] values)
     * addAttNthChild(int i,String)
     * addAtts(String name,String s)
     */

    public void removeAtt(String key) {
        StringBuffer sb=new StringBuffer();
        attsMap.remove(key);
        attsMap.forEach((k,v)->{
            sb.append(k+"='"+v+"' ");             
        });
        attributes=sb+""; 
        
    }
    public void addAtt(String key, String value) {
        attsMap.put(key, value);
        if (key.equals("data"))  return;
        attributes += key.toLowerCase() + "='" + value + "'  ";
    }

    public void addAtt(String[] sa) {
        for (int i = 0; i < sa.length / 2; i++)
            addAtt(sa[i * 2], sa[i * 2 + 1]);
    }

    /**
     * set Child attribute with value
     * @return : void
     */
    public void addAttChild(String key, String[] sa) {
        for (int i = 0; i < size(); i++) {
            UILayout c = (UILayout) list.get(i);
            if (sa[i] == null || sa[i].length() == 0)
                continue;
            if (i == sa.length)
                break;
            c.addAtt(key, sa[i]);
        }
    }

    /**
     * Assign all Nth UILayout of Child element
     */
    public void addAttNthChild(int n, String s) {
        UILayout c = (UILayout) list.get(n);
        for (int i = 0; i < c.list.size(); i++) {
            UILayout cc = (UILayout) c.list.get(i);
            cc.addAtts(s);
        }
    }

    public void addAtts(String s) {
        executeAtt(s, "addAtt");
    }

    public void addAtts(String name, String s) {
        UILayout[] ca = findAllObjects(name);
        for (int i = 0; i < ca.length; i++)
            ca[i].executeAtt(s, "addAtt");
    }


    private void executeAtt(String value, String cmd) {
        String rc = "";
        Class cl = rc.getClass();
        Hashtable<String, String> h = WUtil.readHash(value);
        try {
            Method m = getClass().getMethod(cmd, new Class[] { cl, cl });
            Enumeration en = h.keys();
            while (en.hasMoreElements()) {
                Object o = en.nextElement();
                m.invoke(this, new Object[] { o, h.get(o) });
            }
        } catch (Exception e) {
            catchException("Can not find function=" + cmd + " in UILayout class");
        }
    }

    private void executeLoop(UILayout[] ca, String value, String cmd) {
        String rc = "";
        Class cl = rc.getClass();
        try {
            Method m = getClass().getMethod(cmd, new Class[] { cl });
            for (int i = 0; i < ca.length; i++) {
                m.invoke(ca[i], new Object[] { value });
            }
        } catch (Exception e) {
            catchException("Can not find function=" + cmd + " in UILayout class");
        }
    }

    /**
     * Methods to add Attributes for component
     * addTdAtt(String key,String value)
     * addTdAtt(String[] key-value-pairs)
     * addTdAtts(String "key=value"-string pair)
     * addTdAtts(String name,"key=value"-string pair)
     * addTdAttChild(String key,String[] values)
     */
    public void addTdAtt(String key, String value) {
        key = key.toLowerCase() + "=";
        tdAttributes += " " + key + value;
    }

    public void addTdAtt(String[] sa) {
        for (int i = 0; i < sa.length / 2; i++)
            addTdAtt(sa[i * 2], sa[i * 2 + 1]);
    }

    public void addTdAtts(String s) {
        executeAtt(s, "addTdAtt");
    }

    public void addTdAtts(String name, String s) {
        UILayout[] ca = findAllObjects(name);
        for (int i = 0; i < ca.length; i++)
            ca[i].executeAtt(s, "addTdAtt");
    }

    /**
     * set Child TD attribute with value
     * @return : void
     */
    public void addTdAttChild(String key, String[] sa) {
        for (int i = 0; i < size(); i++) {
            UILayout c = list.get(i);
            if (i == sa.length)
                break;
            if (sa[i] == null)
                continue;
            c.addTdAtt(key, sa[i]);
        }
    }

    public void addTdAttsNthChild(String s, int n) {
        UILayout c = list.get(n);
        c.addTdAtts(s);
    }


    @Override
    public String toString() {
        if (uiRender==null) return "";
        else return uiRender.toString();

    }

    /**
     * example require
     * <text require="{#name1},{#name2}...^^^{title}" />
     * <row><assign require|name="{#name1},{#name2}...^^^{title}"/><text /></row>
     *
     * @return
     * @param tableName
     */
    public String printJS(String tableName) {
        String[] sa;
        String qq = "\"", qc = ",", str;
        String fieldName = null;
        String fieldType = null;
        String value = null;
        String sname = null;
        boolean isRadio, isOk = false;
        Hashtable nameH = getNameHash();
        setMacro(JSTABLE, tableName);
        String line;
        StringBuffer sb = new StringBuffer();
        sb.append("//0:Element name 1:field type 2:value 3:require 4:title 5:validate String 6:flag\n");
        sb.append(tableName + "= [\n");
        String[] allKeys = getKeys();
        int size = allKeys.length;
        for (int i = 0; i < size; i++) {
            fieldName = allKeys[i];
            isRadio = false;
            UILayout c = (UILayout) nameH.get(fieldName);
            if (c == null) {
                sa = findRadio(fieldName);
                c = (UILayout) nameH.get(sa[0]);
                isRadio = true;
            }
            sname = c.getName();
            if (isRadio)       sname = fieldName;
            fieldType = c.getType();
            value = c.getValue();
            value = (value == null) ? "" : value;
            if (c.type.equals(UI_CHECKBOX) && !c.logical) value = "";
            line = "[" + qq + sname + qq + qc;    //Field 0 name
            line += qq + fieldType + qq + qc;     //Field 1 type
            String kvalue=checkQ(value);     
            kvalue=Util.isBlank(kvalue)? "X":kvalue;
            line += qq + kvalue+ qq + qc;        //Field 2 value
            str = removeReturn(c.swapKey(c.require));
            int n=str.indexOf("^^^");
            if (n>=0) str=str.substring(0,n);
            line += qq + str + qq + qc;           //Field 3 require
            line += qq + c.title + qq + qc;       //Field 4 Title
            line += qq + c.validateStr + qq + qc; //Field 5 validation
            str = removeReturn(c.swapKey(c.getFlagStr()));
            str = str.equals("NO")? str:"YES";
            line += qq + str + qq; //Field 6 Flag
            line += "]";
            line = removeCR(line);
            if (i != size - 1)   line += ",\n";
            sb.append(line);
        }
        sb.append("];");
        return sb.toString();
    }

    private String checkQ(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '"')
                result += "\\\"";
            else
                result += s.charAt(i);
        }
        return result;
    }

    private String removeCR(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 10 || c == 13 || c == '\t')
                continue;
            sb.append(s.charAt(i));
        }
        return "" + sb;
    }

    private String removeReturn(String s) {
        char c;
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (c == '\n' || c == '\r' || c == '\t')
                continue;
            result += c;
        }
        return result;
    }


    public void addToOnLoad(String s) {
        if (onLoadTable == null)
            onLoadTable = new Vector();
        onLoadTable.add(s);
    }

    public void addToOnLoad(String key, String s) {
        {
            UILayout[] ca = findAllObjects(key);
            for (int j = 0; j < ca.length; j++)
                ca[j].addToOnLoad(s);
        }
    }
   

    public void checkTd(boolean isTd, String[] sa) {
        String key = sa[0];
        if (isTd)
            return;
        sa[1] = swapKey(sa[1]);
        int n = sa[0].indexOf(".");
        if (n < 0)
            return;
        sa[0] = sa[0].substring(0, n);
        if (key.endsWith(".load"))
            addToOnLoad("document.forms[0]." + getName() + "." + sa[0] + "()");
        return;
    }

    public Hashtable mergeContext(UILayout c, boolean isTd) {
        String key = (isTd) ? c.type + "." + "td" : c.type;
        Hashtable gh = c.getContext(key);
        String str = (!isTd) ? c.attributes : c.tdAttributes;
        Hashtable ch = WUtil.readHash(str);
        Hashtable result = new Hashtable();
        if (gh != null)  result.putAll(gh);
        if (ch != null)  result.putAll(ch);
        return result;
    }

    private Hashtable getCpContext(String key) {
        boolean isTd = key.indexOf(".td") > 0;
        String allkey = isTd ? "all.td" : "all";
        if (context == null)
            return null;
        return addHash((Hashtable) context.get(allkey), (Hashtable) context.get(key));
    }

    private Hashtable getContext0(String key) {
        boolean isTd = key.indexOf(".td") > 0;
        String allkey = isTd ? "all.td" : "all";
        Hashtable result = null;
        if (parent == null) {
            result = addHash(result, cssMap(allkey));
            result = addHash(result, cssMap(key));
            return result;
        }
        result = addHash(result, parent.getContext(allkey));
        result = addHash(result, parent.getContext(key));
        return result;
    }

    private Hashtable cssMap(String key) {
        return (Hashtable) AppMgr.service().getContext(key);
    }

    private Hashtable addHash(Hashtable h1, Hashtable h2) {
        if (h1 == null && h2 == null)
            return null;
        if (h1 == null)
            return h2;
        if (h2 == null)
            return h1;
        Hashtable result = new Hashtable();
        result.putAll(h1);
        result.putAll(h2);
        return result;
    }

    private Hashtable getContext(String key) {
        UILayout c = this;
        while (true) {
            if (c.context != null) {
                Hashtable ht = addHash(null, getContext0(key));
                ht = addHash(ht, c.getCpContext(key));
                return ht;
            }
            c = c.parent;
            if (c == null)
                break;
        }
        return cssMap(key);
    }

   

    /**
     * recursive get the name associate with UILayout
     * @input h Hashtable
     * @return void
     */
    private void prepareKeys() {
        String sname = null;
        sname = mergeName();
        Hashtable nh = getNameHash();
        Hashtable oh = getObjHash();
        if (sname != null && sname.trim().length() != 0 && nh.get(sname) == null) {
            nh.put(sname, this);
            oh.put(this, sname);
        }
        for (int i = 0; i < size(); i++) {
            UILayout UILayout = list.get(i);
            UILayout.setParentName(sname);
            UILayout.prepareKeys();
        }
    }

    private String mergeName() {
        String sname = null;
        boolean nameIsNull = gname().length() == 0;
        boolean parentIsNull = parentName == null || parentName.length() == 0;
        if (nameIsNull && parentIsNull)   sname = null;
        else if (parentIsNull)  sname =   gname();
        else if (nameIsNull) sname = parentName;
        else sname = parentName + "_" + gname();
        return sname;
    }

    public String printTree(String space) {
        UILayout p = parent;
        StringBuffer sb = new StringBuffer();
        sb.append(space + "T[" + type + "] name[" + gname() + "] A[" + attributes + "] V[" + gvalue() + "] new[" + newParam +
                  "]");
        if (p == null)   sb.append(" Parent=null\n");
        else sb.append(space + " Parent|Type=" + p.type + " name=" + p.gname() + "\n");
        for (int i = 0; i < size(); i++) {
            UILayout c = list.get(i);
            sb.append(c.printTree(space + "   "));
        }
        return sb.toString();
    }


    public void setMacro(String key, String value) {
        if (objHash != null)
            setTopMacro(key, value);
        String key1 = "$" + key.toLowerCase();
        String value1 = "'" + value + "'";
        macroAtts += "  " + key1 + "=" + value1;
    }

    public void setTopMacro(String key, String value) {
        Hashtable macro = getMacroTable();
        String key1 = "$" + key.toLowerCase();
        macro.put(key1, value);
    }

    private int macroCount = 0;

    public String getMacro(String key) {
        macroCount++;
        if (macroCount == 4) {
            catchException("Recursive Error for key=" + key);
            macroCount = 0;
            return "";
        }
        Hashtable macro = getMacroTable();
        if (macro == null) {
            catchException("Macro table is not available");
            return "";
        }
        String value = (String) macro.get("$" + key.toLowerCase());
        if (value == null) {
            catchException("Can not find macro key $" + key);
            return "XXXXX";
        }
        value = swapKey(value);
        macroCount = 0;
        return value;
    }

    /**
     * All the static methods start from Here
     *
     */
    public String printAllkeys() {
        String[] sa = getAllKeys();
        StringBuffer sb = new StringBuffer();
        sb.append("<!----- Key table \n");
        for (int i = 0; i < sa.length; i++)
            sb.append(i + " " + sa[i] + "\n");
        sb.append("Key table  END--->\n");
        return sb.toString();
    }


    public String[] getAllKeys() {
        Hashtable nh = getNameHash();
        Set hs = nh.keySet();
        String[] sa = new String[hs.size()];
        hs.toArray(sa);
        Arrays.sort(sa);
        return sa;
    }

    /**
     * only valid key will print
     * @return
     */
    public String printValidKeys() {
        StringBuffer sb = new StringBuffer();
        String[] sa = getKeys();
        sb.append("<!----- Valid Key table \n");
        for (int i = 0; i < sa.length; i++)
            sb.append(i + " " + sa[i] + "\n");
        sb.append("Key table  END--->\n");
        return sb.toString();
    }


    public Object clone() {
        try {
            UILayout c = (UILayout) super.clone();
            if (context != null) {
                c.context = new Hashtable();
                c.context.putAll(context);
            }
            if (onLoadTable != null) {
                c.onLoadTable = new Vector();
                c.onLoadTable.addAll(onLoadTable);
            }
            if (size() == 0)
                return c;
            int n = size();
            c.list = new ArrayList();
            for (int i = 0; i < n; i++) {
                UILayout c1 = (UILayout) list.get(i);
                c.list.add((UILayout) c1.clone());
            }
            return c;
        } catch (Exception e) {
            catchException("Clone Error");
        }
        return null;
    }

   

    public String printModel(String className) {
        StringBuilder sb = new StringBuilder();
        String template = "<model type=class name=%s table=%s>\n";
        sb.append(Util.printf(template, new String[] { className, className.toUpperCase() }));
        removeNameTable();
        String[] keys = getKeys();
       
        for (String fieldName : keys) {
            UILayout c = findObject(fieldName, false);
            if (c==null) continue;
            if ( VALIDCHOICE.indexOf(c.getType())<0) continue;
            char last=c.getType().toCharArray()[c.getType().length()-1];
            String result=last=='s'? ":sa":":s";
            sb.append(fieldName + result+"\n");
            }
        sb.append("</model>");
        return sb + "";
    }


    private String[] getValidKeys() {
        
        HashSet set = new HashSet();
        Hashtable nameHash = getNameHash();
        String[] keys = new String[nameHash.size()];
        nameHash.keySet().toArray(keys);
        for (int i = 0; i < keys.length; i++) {
            String name = keys[i];
            UILayout c = (UILayout) nameHash.get(name);
            boolean isOk = VALIDCHOICE.indexOf(c.type) >= 0;
            if (c.type.equals(UILayout.UI_RADIOBUTTON)) {
                int n = name.lastIndexOf("_");
                if (n>0) name = name.substring(0, n);
            }
            if (!isOk)  continue;
            set.add(name);
        }
        String[] result = new String[set.size()];
        set.toArray(result);
        Arrays.sort(result);
        for (int i = 0; i < result.length; i++) {
            UILayout c = (UILayout) nameHash.get(result[i]);
            if (c != null) {
                c.setId(i);
                continue;
            }
            String[] sa = findRadio(result[i]);
            for (int j = 0; j < sa.length; j++) {
                c = (UILayout) nameHash.get(sa[j]);
                c.setId(i);
            }
        }
        return result;
    }

    private void getLogicKeys() {
        HashSet checkSet = new HashSet();
        HashSet radioSet = new HashSet();
        Hashtable oh = getObjHash();
        Hashtable nh = getNameHash();
        String[] keys = new String[nh.size()];
        nh.keySet().toArray(keys);
        for (int i = 0; i < keys.length; i++) {
            String name = keys[i];
            UILayout c = (UILayout) nh.get(name);
            if (c.type.equals(UILayout.UI_CHECKBOX))
                checkSet.add(name);
            if (c.type.equals(UILayout.UI_RADIOBUTTON))
                radioSet.add(name);
        }
        String[] result = new String[checkSet.size()];
        checkSet.toArray(result);
        oh.put(TABLE_CHECK, result);
        result = new String[radioSet.size()];
        radioSet.toArray(result);
        oh.put(TABLE_RADIO, result);
    }

    /**
     * replace the key with value
     *     $id: replace with current Id
     *   $name: replace with current name
     * $prefix: get first word of name
     *  $group: print all member with separate "."
     * start with all
     */
    public String replaceKey(String skey, boolean isGroup) {
        String result = "XXXXX";
        String nkey = null, key = null;
        UILayout c0 = null;
        boolean isQQ = false, isQ = false;
        boolean isAll = false;
        String[] cmd = new String[] {
            "i", "$id", "n", "$name", "v", "$value", "p", "$prefix", "g", "$group", "h", "$group_1", "j", "$" + JSTABLE
        };

        isAll = skey.startsWith("all_");
        if (isAll)
            skey = skey.substring(4);
        isQQ = skey.endsWith("_qq");
        isQ = skey.endsWith("_q");
        if (isQQ)
            key = skey.substring(0, skey.length() - 3);
        else if (isQ)
            key = skey.substring(0, skey.length() - 2);
        else
            key = skey;
        char c = findCommand(key, cmd);
        switch (c) {
        case 'e':
            break;
        case '@':
            result = key.substring(1);
            break;
        case '$':
            result = getMacro(key.substring(1));
            break;
        case 'j':
            result = getMacro(JSTABLE);
            break;
        case 'i':
            result = "" + id;
            break;
        case 'n':
            result = getName();
            break;
        case 'v':
            result = getValue();
            break;
        case 'g':
            result = getAllNameBG(WUtil.getPrefix(getName(), 1, "_"));
            break;
        case 'h':
            result = getAllNameBG(WUtil.getPrefix(getName(), 2, "_"));
            break;
        case 'p':
            result = WUtil.getPrefix(getName(), 1, "_");
            break;
        case '#':
            nkey = key.substring(1);
            c0 = findObject(nkey, isGroup);
            if (c0 != null)
                result = "" + c0.getId();
            break;
        default:
            if (isAll) {
                result = getAllNames(key);
                break;
            }
            c0 = findObject(key, isGroup);
            if (c0 != null)
                result = c0.getName();
            break;
        }
        if (isQQ)
            result = "\"" + result + "\"";
        else if (isQ)
            result = "'" + result + "'";
        return result;
    }

    private char findCommand(String cmd, String[] table) {
        if (cmd == null || cmd.length() == 0)
            return 'e';
        if (cmd.charAt(0) == '#')
            return '#';
        if (cmd.charAt(0) == '@')
            return '@';
        for (int i = 0; i < table.length / 2; i++) {
            if (!cmd.toLowerCase().equals(table[i * 2 + 1]))
                continue;
            char c = table[i * 2].charAt(0);
            return c;
        }
        if (cmd.charAt(0) == '$')
            return '$';
        else
            return 0;
    }


    
    public String printAll(String s) {
        StringBuffer sb = new StringBuffer();
        sb.append(printJS(s));
        sb.append(toString());
        sb.append(printOnLoad());
        return "" + sb;
    }

    public Hashtable createTable() {
        Hashtable h = new Hashtable();
        String[] sa = getValidKeys();
        for (int i = 0; i < sa.length; i++)
            h.put(sa[i], "");
        return h;
    }
  

    public String get(HttpServletRequest req, String name) {
        String s = req.getParameter(name);
        setMacro(name, s);
        return s;
    }

   

    public String getAddParam() {
        return addParam;
    }

    public String getAttributes() {
        return attributes;
    }

    public String getNoAdd() {
        return noAdd;
    }

    public void setAddParam(String addParam) {
        this.addParam = addParam == null ? null : addParam.trim();
    }

    public void setAttributes(String string) {
        attributes = string;
    }

    public void setNoAdd(String string) {
        noAdd = string;
    }

    public String getNewParam() {
        return newParam == null ? null : newParam.trim();
    }

    public void setNewParam(String string) {
        newParam = string;
    }


    public void setAttSet(Hashtable attSet) {
        this.attSet = attSet;
    }

    public Hashtable getAttSet() {
        return attSet;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }

    
    
    
}
