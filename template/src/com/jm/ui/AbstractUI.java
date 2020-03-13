package com.jm.ui;

import com.jm.render.AbsRender;
import com.jm.util.Util;
import com.jm.util.WUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public abstract class AbstractUI {
    public final static String UI_MSG       = "msg";
    public final static String UI_FORM      = "form";
    public final static String UI_MSGFMT    = "msgfmt";
    public final static String UI_TEXTOUT   = "textout";
    public final static String UI_CSS       = "css";
    public final static String UI_JS        = "js";
    public final static String UI_SCRIPT    = "script";
    public final static String UI_PURETEXT  = "puretext";
    public final static String UI_LABEL     = "label";
    public final static String UI_RULE      = "rule";
    public final static String UI_SIMPLE    = "simple";
    public final static String UI_MENU      = "menu";
    public final static String UI_ARRAY     = "array";
    public final static String UI_ROW       = "row";
    public final static String UI_TABLE     = "table";
    public final static String UI_TEXT      = "text";
    public final static String UI_SELECT    = "select";
    public final static String UI_SELECTS   = "selects";
    public final static String UI_SELECT0   = "select0";
    public final static String UI_LIST      = "list";
    public final static String UI_ERROR     = "error";
    public final static String UI_LINK      = "link";
    public final static String UI_BUTTON    = "button";
    public final static String UI_FILE        = "file";
    public final static String UI_SUBMIT      = "submit";
    public final static String UI_RESET       = "reset";
    public final static String UI_HIDDEN      = "hidden";
    public final static String UI_COMMENT     = "comment";
    public final static String UI_RADIOBUTTON = "radiobutton";
    public final static String UI_RADIOS      = "radios";
    public final static String UI_CHECKBOX    = "checkbox";
    public final static String UI_CHECKBOXS   = "checkboxs";
    public final static String UI_DIV         = "div";
    public final static String UI_TEXTAREA    = "textarea";
    public final static String UI_CONTEXT     = "context";
    public final static String UI_ASSIGN      = "assign";
    public final static String UI_PARAM       = "param";
    public final static String UI_INSERT      = "insert";
    public final static String UI_PASSWORD    = "password";
    public final static String UI_DATE        = "date";
    public final static String UI_NUMBER      = "number";
    public final static String UI_APP       = "app";
    public final static String UI_ITEMS     = "items";
    public final static String UI_ACTIONS   = "actions";
    public final static String UI_CONTAINER = "container";
    public final static int TYPE_LABEL      = 0;
    public final static int TYPE_SELECT     = 1;
    public final static int TYPE_TEXT       = 2;
    public final static int TYPE_RADIO      = 3;
    public final static int TYPE_DIV        = 4;
    public final static String VALIDCHOICE=
    "text file password select checkbox checkboxs radiobutton selects";
    //sub hashtable name
    public final static String TABLE_VALIDKEY = "validkey";
    public final static String TABLE_NAME = "name";
    public final static String TABLE_RADIO = "radio";
    public final static String TABLE_CHECK = "check";

    public final static String JSTABLE = "jstable";
    public final static String MACRO = "macro";

   
    protected boolean notd = false;
    protected String noAdd;
    protected String newParam;
    protected String addParam;
    protected String assign = "";
    protected int textLocation;
    protected int count = 0;
    protected boolean isTd = false;
    protected String group = "";
    //for radio button checked if true else false
    protected boolean logical = false;
    // for access the search name, the record of error

    protected String validateStr = "";
    // parent Object
    protected UILayout parent = null;
    protected String orgType = null;
    // list of Child WebLayout component
    protected ArrayList<UILayout> list = null;
    //all the type of attribute start from here
    protected Hashtable context = null;
    // attributes for web component such as input and select
    protected String attributes = "";
    protected String title = "";
    protected String tabName = "";
    protected String tdAttributes = "";
    //set require for array
    protected String require = "";
    //set JS cript array id
    protected int id;
    protected Hashtable objHash = null;
    protected String flagStr = "";
    //for option select data array
    protected Vector selectData;
    //data element type such text,select,row...
    protected String type;
    //field name
    private  String name;
    //field value
    private String value;
    //parent name of WebLayout
    protected String parentName = null;
    //comment
    protected String comment = null;
    // attach to the name when print
    protected String suffix = "";
    Map<String, String> attsMap = new Hashtable();
    protected String macroAtts = "";
    protected Hashtable attSet = null;
    protected AbsRender uiRender;
    protected Vector onLoadTable = null;
    protected UIParser uiParser=UIParser.instance();
    protected AppMgr appMgr=AppMgr.service();
    protected boolean haveError=false;
    
    protected boolean checkSymbol(String test) {
        if (appMgr.isSkip(test)) return true;
        try {
            Field a = getClass().getField("UI_" + test.toUpperCase());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    protected void checkList() {
        if (list == null)
            list = new ArrayList();
    }
    
    public void setAssign(String s) {
        assign = s;
    }

    public String getAssign() {
        return assign;
    }
    
    public void setCount(int in) {
        count = in;
    }

    public int getCount() {
        return count;
    }
    
    public void setGroup(String group) {
        this.group = group;
        for (int i = 0; i < size(); i++)
            list.get(i).setGroup(group);
    }

    public void setValidateStr(String s) {
        validateStr = s;
    }
    
    public int size() {
        if (list == null)  return 0;
        else  return list.size();
    }
    
    public String printOnLoad() {
        Vector table = getLoadTable();
        StringBuffer sb = new StringBuffer();
        sb.append("\n<SCRIPT language=Javascript1.2 >\n");
        for (int i = 0; i < table.size(); i++)
            sb.append("" + table.get(i) + ";\n");
        sb.append("</SCRIPT>\n");
        return sb.toString();
    }
    
    public Vector getLoadTable() {
        Vector v = new Vector();
        loadLoadTable(v);
        return v;
    }
    
    public void loadLoadTable(Vector v) {
        for (int i = 0; onLoadTable != null && i < onLoadTable.size(); i++) {
            String s = (String) onLoadTable.get(i);
            String result = swapKey(s);
            v.add(result);
        }

        for (int i = 0; i < size(); i++) {
            UILayout c = list.get(i);
            c.loadLoadTable(v);
        }
    }
    
    /**
     * leaf component of WebLayout
     * input type=text box
     * no extension of child of WebLayout
     */

    public String swapKey(String s) {
        boolean isGroup = false;
        StringBuffer sb = new StringBuffer();
        String key = "";
        boolean isKey = false;
        int size = s.length();
        char c;
        for (int i = 0; i < size; i++) {
            c = s.charAt(i);
            switch (c) {
            case '[':
            case '{':
                isKey = true;
                break;
            case ']':
                if (isNumber(key)) {
                    sb.append("[" + key + "]");
                    isKey = false;
                    key = "";
                    break;
                }
            case '}':
                isGroup = c == '}';
                isKey = false;
                sb.append(replaceKey(key, isGroup));
                key = "";
                break;
            default:
                if (isKey)
                    key += c;
                else
                    sb.append(c);
                break;
            }
        }
        return sb.toString();
    }
    
    

    private boolean isNumber(String s) {
        if (s == null)
            return true;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }
    
    
    public abstract String replaceKey(String key,boolean isGroup);
    public Vector getSelectData() {
        return selectData;
    }
    
    public Map<String, String> getAttsMap() {
        return attsMap;
    }
    

    public void printError() {
        StringBuffer sb = uiRender.getErrorReport();
        log("Error Report\n");
        log(sb + "");
        log("Error Report END\n");
    }
    
    public void log(String s) {
        WUtil.log(s + " IN WebLayout");
    }

    public boolean isTd() { return isTd;}
    public boolean logical() { return logical;}

    public void setUiRender(AbsRender uiRender) {
        this.uiRender = uiRender;
    }

   
    
    public boolean notd() {return notd;}
    public String getOrgType() {return orgType;}
    public boolean isError() {return haveError;}
    public String gname(){ return name==null? "":name;}
    public String gvalue(){ return value==null? "":value;}
    public void setName(String name) {this.name = name;}
    public boolean b(String name) {return Util.isBlank(name);}
    public void setValue(String value) {
        this.value = value;
    }
}
