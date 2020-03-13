package com.jm.render;

import com.jm.mgr.*;
import com.jm.model.LookupList;
import com.jm.ui.*;
import com.jm.util.*;
import java.lang.reflect.Method;
import java.util.*;

public abstract class BasicRender {
    public final static String ERRORREPORT = "errorreport";
    public final static String TD        = "td";
    public final static String NOFUNC    = "nofunc";
    public final static String APPE      = "app-";
    public final static String APP       = "app";
    public final static String REACT     = "react";
    public final static String ROUTE     = "route";
    public final static String DEF       = "def";
    public final static String PRINTITEM = "printitem";
    public final static String PRINTACTION = "printaction";
    public final static String PRINTAJAX = "printajax";
    public final static String PRINTFMT = "printfmt";
    public final static String PRINTLINK = "printlink";
    public final static String PRINTSELECT = "printselect";
    public final static String VALUE       = "value";
    public final static String TYPE        = "type";
    public final static String ATTR        = "attr";
    public final static String NAME        = "name";
    public final static String DATA        = "data";
    public final static String PIE         = "|";
    public final static String COMMON      = ",";
    public final static String RT          = "\r\n";
    protected UILayout layout;
    protected String comment;
    protected String type;
    protected String name;
    protected String value;
    protected Hashtable objHash;
    protected Map<String, String> attsMap;
    protected Vector selectData;
    protected boolean isTd;
    protected boolean logical;
    protected String orgType;
    protected List<UILayout> list;
    protected HashSet            removeList = new HashSet();
    protected Map<String,String> addList    = new Hashtable();
    protected ThreadMgr threadMgr           = ThreadMgr.instance();
    protected UIrenderMgr renderMgr         = UIrenderMgr.instance();
    protected Map<String, String> funcMap;
    protected String beanName = threadMgr.getBeanName();
    protected String renderType = "";

    
    protected void writeFunc(String[] sa, String value0) {
        boolean isAn = value0.startsWith("@");
        value0 = isAn ? funcMap.get(value0.substring(1)) : value0;
        String template = "public %s get%s(){\n %s}\n";
        String text = Util.printf(template, new String[] { sa[0], Util.cap(sa[1]), value0 });
        threadMgr.append(text);
    }

    protected void writeMethod(String key, Map map) {
        String text = renderMgr.getMethod(key, map);
        text=text.replaceAll("\n"," ").replaceAll("\r"," ");
        threadMgr.append(text+RT);
        log(text);
    }

    protected String att(String key) {
        String text = layout.getAttsMap().get(key);
        removeList.add(key);
        return text;
    }
    
    protected void changeKey(String oldKey,String newKey,String value){
        removeList.add(oldKey);
        addList.put(newKey,value);
    }

    private void init() {
        type = layout.getType();
        name = layout.getName();
        objHash = layout.getObjHash();
        value = layout.getValue();
        attsMap = layout.getAttsMap();
        selectData = layout.getSelectData();
        isTd = layout.isTd();
        list = layout.getList();
        if (list == null) list = new ArrayList();
        logical = layout.logical();
        orgType = layout.getOrgType();
    }

    public String array() { return appendChild(isTd);}

    protected int size() {
        if (list == null)  return 0;
        else  return list.size();
    }

    protected String getName() {return layout.getName();}
    public String getAttStr() {return getAttStr(layout, false);}

    public String getAttStr(UILayout parent, boolean isTd) {
        Hashtable<String, String> dot = new Hashtable();
        Hashtable<String, String> nh = layout.mergeContext(parent, isTd);
        if (nh == null) return "";
        removeList.forEach((key) -> nh.remove(key));
        nh.putAll(addList);
        StringBuffer sb = new StringBuffer();
        String value = null;
        for (String key : nh.keySet()) {
            value = nh.get(key);
            if (key.indexOf(".") > 0) {
                dot.put(key, value);
                continue;
            }

            String checks = value.toLowerCase();
            if (checks.equals("none") || checks.equals("xxxxx"))
                continue;
            String[] sa = new String[] { key, value };
            layout.checkTd(isTd, sa);
            key = sa[0];
            value = sa[1];
            sb.append(key + "=\'" + value + "'" + " ");
        }
        if (dot.size() > 0 && !isTd) putDotAttr(dot);
        return sb.toString();
    }

    private void putDotAttr(Hashtable<String, String> dot) {
        if (layout.notd()) return;
        for (String key : dot.keySet()) {
            String[] sa = WUtil.split(key, ".");
            boolean isTD = sa[0].equals(TD);
            String value = dot.get(key);
            String[] ssa = WUtil.split(value, "|");
            if (isTD)
                layout.addTdAttChild(sa[1], ssa);
        }
    }

    public String toString() {
        init();
        StringBuffer errorReport = getErrorReport();
        if (errorReport.length() != 0) return "" + errorReport;
        String result = "";
        String mtype=findMatch(type);
        Method method = getMethod(mtype);
        method = (method == null) ? getMethod(NOFUNC) : method;
        if (comment != null) result += "\n<!--" + comment + "-->\n";
        result += getResult(method);
        if (comment != null) result += "<!--" + comment + " END-->\n";
        return result;

    }
    
    private String findMatch(String type){
        String[] table0={APPE,REACT,DEF};
        String[] table1={APP,REACT,DEF};
        int count=0;
        for (String word:table0){ 
            if (type.startsWith(word)) return table1[count];
            count++;
        }
        return type;
    }

    private Method getMethod(String text) {
        try {
            Method m = getClass().getMethod(text, new Class[] { });
            return m;
        } catch (Exception e) {
            log("Render::" + text + " not find");
        }

        return null;
    }

    private String getResult(Method method) {
        try {
            return (String) method.invoke(this, new Object[] { });
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    public String error() {
        StringBuffer sb = new StringBuffer();
        sb.append("<H5>Error name=" + name + " type=" + orgType + " reason=" + value + "</H5>");
        return sb.toString();
    }

    public StringBuffer getErrorReport() {

        StringBuffer sb = (StringBuffer) objHash.get(ERRORREPORT);
        if (sb == null) {
            sb = new StringBuffer();
            objHash.put(ERRORREPORT, sb);
        }
        return sb;
    }

    public String nofunc() {
        StringBuffer sb = new StringBuffer();
        String attr = getAttStr();
        String idName = (name == null) ? value : getName();
        sb.append("<" + type + " id='" +idName+"' "+ attr + ">\n");
        sb.append(layout.getValue());
        sb.append(printList("\n"));
        sb.append("</" + type + ">\n");
        return sb.toString();
    }
    
    public String nofunc(String attr) {
        StringBuffer sb = new StringBuffer();
        String idName = (name == null) ? value : getName();
        sb.append("<" + type + " id='" +idName+"' "+ attr + ">\n");
        sb.append(layout.getValue());
        sb.append(printList("\n"));
        sb.append("</" + type + ">\n");
        return sb.toString();
    }

    public String param() {return "";}
    public String msg() {return printItem();}
    public String msgfmt() {return printItem(); }
    public String text() { return printItem();  }
    public String date() {return printItem(); }
    public String number() {return printItem();}
    public String password() {return printItem(); }

    public String label0() {
        if (value == null)  return "";
        else    return value;
    }

    /**
     * leaf component of UILayout
     * input type=option select
     * no extension of child of UILayout
     */

    public String list() {
        String data = attsMap.get(DATA);
        Vector v = checkOption(data);
        selectData = (v == null) ? selectData : v;
        StringBuffer sb = new StringBuffer();
        sb.append("<ul class='boot-description'>");
        for (int i = 0; i < selectData.size(); i++) {
            LookupList l = (LookupList) selectData.elementAt(i);
            String message = "<li><span class='property'>Item " + l.getValue() + ":</span>" + l.getDesc() + "</li>";
            sb.append(message + "\n");
        }
        sb.append("</ul>");
        return sb + "";
    }

    private Vector checkOption(String data) {
        if (data == null)   return null;
        boolean havePie=data.indexOf(PIE)>0;
        String[] sa = Util.split(data, havePie? PIE:COMMON);
        Vector v = new Vector();
        for (String key : sa)   v.add(LookupList.create(key));
        return v;

    }

    public String rule() {

        StringBuffer sb = new StringBuffer();
        sb.append("<hr " + getAttStr() + "/>");
        return (sb.toString());
    }

    public String submit() {return printAction();}
    public String reset() {return printAction(); }
    public String file() {return printAction();   }
    public String def(){return "";}


    public String textarea() {
        StringBuffer sb = new StringBuffer();
        sb.append("<textarea ");
        sb.append(" name=" + getName());
        sb.append(" id=" + getName());
        sb.append(" " + getAttStr() + " >");
        sb.append(layout.getValue());
        for (int i = 0; i < size(); i++) {
            UILayout c = list.get(i);
            sb.append(c.getValue());
            sb.append("\n");
        }
        sb.append("</textarea>\n");
        return sb.toString();
    }

    public String options() {
        String data = attsMap.get(DATA);
        Vector v = checkOption(data);
        selectData = (v == null) ? selectData : v;
        String selected = "";
        StringBuffer sb = new StringBuffer();
        if (selectData == null) {
            selectData = WUtil.createDummy(5);
            value = WUtil.getValue(selectData);
        }
        if (selectData.size() == 0) {
            sb.append("<OPTION VALUE=''>Zero Element</OPTION>");
            return sb.toString();
        }
        if (!(selectData.elementAt(0) instanceof LookupList)) {
            sb.append("<OPTION VALUE='a'>Error LookupList</OPTION>");
            return sb.toString();
        }
        for (int i = 0; i < selectData.size(); i++) {
            selected = "";
            LookupList l = (LookupList) selectData.elementAt(i);
            if (l.getValue().equals(label0()))
                selected = "selected";
            sb.append("<OPTION VALUE='" + l.getValue() + "' " + selected + " >");
            sb.append(l.getDesc());
            sb.append("</OPTION>\n");
        }
        return sb + "";
    }

    public String select() {
        StringBuffer sb = new StringBuffer();
        sb.append("<SELECT  ");
        sb.append("name=" + getName() + " ");
        sb.append("id='" + getName() + "' ");
        sb.append(getAttStr());
        sb.append(">");
        sb.append(options());
        sb.append("</SELECT>");
        return sb.toString();
    }

    public String popup() {return "";}
    public String selects() {return "";}
    public String checkboxs() {return "";}

    public String insert() {
        return appendChild(false);
    }

    public String printList() {
        return printList("\n\r");
    }

    public String printList(String pattern) {
        StringBuffer sb = new StringBuffer();
        list.forEach(w -> sb.append(w + pattern));
        return Util.removeLast(sb + "", pattern.length());
    }

    public String appendChild(boolean isTR) {
            boolean isTable=ThreadUtil.isTable();
            String result= "";
            for (int i = 0; i < size(); i++) {
                UILayout uiLayout = list.get(i);
                boolean notHidden = !uiLayout.getType().equals(UILayout.UI_HIDDEN);
                boolean flag = isTR & notHidden;
                if (flag)  result+=appendElement(uiLayout);
                if (uiLayout!=null) result+=uiLayout;
                if (flag)  result+=(isTable? "</TD>":"</div>");
            }
            return result;
        }
    
    public String appendElement(UILayout ui){
        StringBuffer sb = new StringBuffer();
        boolean isTable=ThreadUtil.isTable();
        String tag=isTable? "<TD ":"<div class='col-md-3'";
        sb.append(tag);
        sb.append(isTable? getAttStr(ui, true):"");
        sb.append(">");
        return sb+"";
    }


    public String link() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n<A" + " " + getAttStr() + ">");
        sb.append(label0());
        sb.append("</A>");
        return sb.toString();
    }

    public String checkbox() {
        StringBuffer sb = new StringBuffer();
        String check = (logical) ? " CHECKED" : " ";
        sb.append("<INPUT type='checkbox'" + " " + getAttStr());
        sb.append("name='" + getName() + "' ");
        String cv = label0();
        cv = cv.length() == 0 ? "yes" : cv;
        sb.append("value='" + cv + "'" + check + " />");
        return sb.toString();
    }

    public String radiobutton() {
        StringBuffer sb = new StringBuffer();
        String check = (logical) ? " CHECKED" : " ";
        String sname = getName();
        sb.append("<INPUT type='radio'" + " " + getAttStr());
        sb.append("name=" + sname + " ");
        sb.append("value='" + label0() + "'" + check + " >");
        return sb.toString();
    }

    

    public String hidden() { return printAction(); }
    public String button() { return printAction();}
    public String form() {return nofunc();}


    /**
     * leaf component of UILayout start Here
     * Here we have
     * array: a group of UILayout without TD element
     * row  : a group of UILayout with TD element
     * type = label
     * no extension of child of UILayout
     */
    public String label() {
        int count = 0;
        StringBuffer sb = new StringBuffer();
        if (value == null)
            return "";
        if (value.startsWith("# "))
            value = value.substring(1);
        if (value.endsWith(" #"))
            value = Util.removeLast(value, 1);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            sb.append("" + c);
            if (c == ' ')
                count++;
            else
                count = 0;
            if (count > 1)
                sb.append("&nbsp;");
        }

        return "<label>"+sb.toString()+"</label>";
    }

    public String app() {return "";}
    public String react() {return "";}

    public abstract String printItem();

    public abstract String printAction();

    public String trans(String type) {
        return AppMgr.service().tranl(renderType, type);
    }

    protected String[] split(String value) {
        return split(value, ":");
    }
    
    protected String[] split(String value,String patt) {
        return Util.split(value, patt);
    }

    protected String[] splitdot(String value) {
        return split(value, ".");
    }

    protected String[] splitsp(String value) {
        return split(value, " ");
    }

    public void setFuncMap(Map<String, String> funcMap) {
        this.funcMap = funcMap;
    }

    public Map<String, String> getFuncMap() {
        return funcMap;
    }


    public String printf(String template, String text) {
        return Util.printf(template, new String[] { text });
    }

    public String printf(String template, String text, String text1) {
        return Util.printf(template, new String[] { text, text1 });
    }

    public String printf(String template, String text, String text1, String text2) {
        return Util.printf(template, new String[] { text, text1, text2 });
    }
    
    public Map createMap(String key,String value) {
        Map<String, String> map = new Hashtable();
        map.put(key, value);
        return map;
    }
    
    public Map createMap(String key1,String key2,String value1,String value2) {
        return createMap(new String[] {key1,key2},
                         new String[] {value1,value2});
    }
    
    public Map createMap(String[] keys,String[] values) {
        Map<String, String> map = new Hashtable();
        int count=0;
        for (String key:keys){
            map.put(key,values[count++]);
        }
        return map;
    }


    public Map createMap() {
        Map<String, String> map = new Hashtable();
        map.put(NAME, getName());
        map.put("beanname", threadMgr.getBeanName());
        map.put(TYPE, trans(type));
        map.put(ATTR, getAttStr());
        map.put("args", "");
        return map;
    }

    public Map createMapALT(String args) {
        Map<String, String> map = createMap();
        map.put("args", getArgs(args));
        return map;
    }
    public Map createValueMap(String value) {
        Map<String, String> map = new Hashtable();
        map.put(VALUE, value);
        return map;
    }
    
    public Map createTypeMap() {
        Map<String, String> map = new Hashtable();
        map.put(NAME,getName());
        map.put(TYPE, trans(type));
        map.put(ATTR, getAttStr());
        return map;
    }

    public Map createMapARGS(String value) {
        Map<String, String> map = createMap();
        map.put(VALUE, value);
        map.put("args", getArgs(value));
        return map;
    }

    public String getArgs(String text) {
        return text;
    }

    public String freeTran(String subkey, Map map) {
        return renderMgr.tranl(renderType, subkey, map);
    }

    public String freeTranALT(String subkey, String alt) {
        Map map = createMapALT(alt);
        return renderMgr.tranl(renderType, subkey, map);
    }

    public String freeTranValue(String subkey, String value) {
        Map map = createValueMap(value);
        return renderMgr.tranl(renderType, subkey, map);
    }

    public String freeTranLink(String subkey, String value, String image) {
        Map map = createValueMap(value);
        map.put("image", image);
        return renderMgr.tranl(renderType, subkey, map);
    }

    public String freeTranList(String subkey, String value, List words) {
        Map map = createValueMap(value);
        map.put("words", words);
        return renderMgr.tranl(renderType, subkey, map);
    }

    public String freeTranAjax(String subkey, String value, String[] pair) {
        Map map = createValueMap(value);
        map.put("render", pair[0]);
        map.put("execute", pair[1]);
        return renderMgr.tranl(renderType, subkey, map);
    }

    public String freeTranSelect(String subkey, String value, String listName) {
        Map map = createValueMap(value);
        map.put("argslist", getArgs(listName));
        return renderMgr.tranl(renderType, subkey, map);
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void setLayout(UILayout layout) {
        this.layout = layout;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public String cap(String text) {return Util.cap(text);}
    public String cap(String text,int n1,int n2) {
        char[] ca=text.toCharArray();
        String result="";
        for (int i=0;i<ca.length;i++)
        if (i==n1 || i==n2) result+=(ca[i]+"").toUpperCase();
        else result+=(ca[i]+"").toLowerCase();
        return result;
    }   
    
    //<actions execute:Login='s.login:f.fail:i.login' />
    private void makeAction(String[] methodAction, String[] values) {
        Map map = new Hashtable();
        map.put("action", cap(methodAction[1]));
        map.put("method", methodAction[0]);
        List list = new ArrayList();
        map.put("maps", list);
        Arrays.asList(values).forEach((value) -> {
                String[] keyValue = splitdot(value);
                String key = "";
                switch (keyValue[0]) {
                case "i":key = "INPUT";break;
                case "s":key = "SUCCESS";break;
                default: key = "ERROR";break;
                }
                Map map1 = new Hashtable();
                map1.put("key", key);
                map1.put(VALUE, keyValue[1]);
                list.add(map1);
            });
        writeMethod("makeaction", map);
    }

    public String actions() {
        attsMap.forEach((ma, value) -> {
                String[] methodAction = split(ma);
                String[] values = split(value);
                makeAction(methodAction, values);
            });
        return "";
    }
    
    public String printModel(String className,String[] keys) {
        StringBuilder sb = new StringBuilder();
        String template = "<model type=class name=%s table=%s>\n";
        sb.append(Util.printf(template, new String[] { className, className.toUpperCase() }));
        for (String key: keys) sb.append(key);
        sb.append("name:s");
        sb.append("</model>");
        return sb + "";
    }
    
    protected String removeLast(String names){
        return Util.removeLast(names);
    }
}
