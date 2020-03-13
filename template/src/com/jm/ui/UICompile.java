package com.jm.ui;

import com.jm.util.*;
import java.util.*;
import java.lang.reflect.*;

public class UICompile {

    public  static final String MACROTEXT="&";
    public  static final String RTN="\n";
    public  static final String PIE="|";
    public  static final String COMMON=",";
    private int currentPoint = 0;
    private int size = 0;
    private String longStr = null;
    private UILayout root = null;
    private UILayout current = null;
    private boolean inBraket;
    private Hashtable<String, String> currentAtts;
    private UIParser uiParse;
    private Map<String,String> textMacro=new Hashtable();
    private boolean haveError;
    
    
    public UICompile(String text, UIParser uiParse) {
        longStr = text;
        size = longStr.length();
        root = uiParse.create(UILayout.UI_ARRAY);
        this.uiParse = uiParse;
        current = root;
        process();
    }

    public UILayout getLayout() {return root;}

    private void process() {
        char c;
        String cmd = null;
        String ecmd = null;
        String att = null;
        StringBuffer sb = new StringBuffer();
        while (true) {
            c = getChar();
            //Set break here
            if (c=='E'){
                int a=2;
            }
            switch (c) {
            case '<':
                inBraket = true;
                try {loadLabel(sb);}
                catch(Exception e){}
                int textPosition = currentPoint;
                cmd = getWord();
                att = getAtts();
                if (cmd != null)
                    loadUILayout(cmd, att, textPosition);
                break;
            case '/':
                if (!inBraket) {
                    sb.append(c);
                    break;
                }
                ecmd = getWord();
                if (ecmd != null)
                    cmd = ecmd;
                if (!endUILayout(cmd))
                    return;
                break;
            case '>':
                inBraket = false;
            case '\r':
            case '\n':
                break;
            case 0:
                return;
            case '[':
                while ((c = getChar()) != 0) {
                    if (c == ']')
                        break;
                    sb.append(c);
                }
                break;
            default:
                sb.append(c);
            }
        }
    }

    private void loadLabel(StringBuffer sb) {
        String word = sb.toString().trim();
        sb.delete(0, sb.length());
        if (word.length() == 0)  return;
        String prefix = getPrefix(current.getTabName());
        String name = getName(current.getTabName()).toLowerCase();
        char mode = word.startsWith("line") ? 'l' : word.startsWith("rule") ? 'r' : (char) 0;
        if (name.equals(UILayout.UI_TABLE) && mode > 0) {
            String num = word.substring(4);
            if (num.length() == 0) num = "1";
            int number = 1;
            try {
                number = Integer.parseInt(num);
            } catch (Exception e) {
            }
            UILayout c = uiParse.create(UILayout.UI_ROW);
            switch (mode) {
            case 'l':
                c.add(uiParse.create(UILayout.UI_LABEL, "&nbsp;"));
                break;
            case 'r':
                UILayout wl = uiParse.create(UILayout.UI_RULE);
                wl.addAtt("class", "steplink");
                c.add(wl);
                break;
            }
            setAtt(c, "td.colspan", number + "");
            doAdd(c);
            return;
        }

        if (name.equals(UILayout.UI_SIMPLE)) {
            new UISimple(current,word,currentAtts,this);
            return;
        }
        if (name.equals(UILayout.UI_MENU)) {
            current.setValue(word);
            return;
        }
        if (!name.equals(UILayout.UI_ROW) && !name.equals(UILayout.UI_ARRAY) &&
            !name.equals(UILayout.UI_DIV) && !name.equals(UILayout.UI_TEXTAREA) && prefix == null)
        {
            current.setValue(word);
            return;
        }
        boolean havePie=word.indexOf(PIE)>0;
        String[] sa = split(word, havePie? PIE:COMMON);
        for (int i = 0; i < sa.length; i++)
            doAdd(uiParse.create(UILayout.UI_LABEL, convert(sa[i])));
    }


    


    private String convert(String s) {
        String result = s;
        while (true) {
            int n = result.indexOf("##");
            int len = result.length();
            if (n == -1)
                return result;
            char c = (n + 3 < len) ? result.charAt(n + 2) : (char) 0;
            if (c == 0 || c == ' ') {
                result = result.substring(0, n) + result.substring(n + 2, len);
                continue;
            }
            String left = result.substring(0, n);
            String right = result.substring(n + 3, len);
            switch (c) {
            case 'b':
                result = left + "<BR>" + right;
                break;
            default:
                result = left + right;
                break;
            }
        }
    }

    private void loadUILayout(String oldCmd, String atts, int textPosition) {
        if (!oldCmd.equals(UILayout.UI_CONTEXT)){
            atts=swapMacro(atts);
        }
        currentAtts = readHash(atts);
        UILayout child = null;
        String cmd = AppMgr.service().findMacro(oldCmd);
        if (cmd.equals(UILayout.UI_PURETEXT)) {
            String endTag = "</" + UILayout.UI_PURETEXT + ">";
            int n = longStr.indexOf(endTag, textPosition);
            int length = UILayout.UI_PURETEXT.length() + 1;
            String pureText = longStr.substring(textPosition + length, n);
            this.currentPoint = n;
            child = uiParse.create(UILayout.UI_PURETEXT);
            child.setValue(pureText);
        } else {
            child = createObject(cmd);
        }
        child.setCount(0);
        for (String key : currentAtts.keySet())
            setAtt(child, key, currentAtts.get(key));
        
        child.setTabName(oldCmd);
        child.setTextLocation(textPosition);
        saveParent(current);
        doAdd(child);
        current = child;

    }

    public void doAdd(UILayout c) {
        boolean isParam = (current instanceof UIParam) || (current instanceof UITempl);
        String tabname = current.getTabName();
        String prefix = getPrefix(tabname);
        if (prefix == null || prefix.equals("class") || isParam) {
            current.add(c);
            return;
        }
        int n = current.getCount();
        current.setCount(n + 1);
        int index = n % current.size();
        if (index < 0)
            index = 0 - index;
        ArrayList list = current.getList();
        UILayout row = (UILayout) list.get(index);
        list = row.getList();
        if (n >= 0) list.add(c);
        else list.add(0, c);
    }

    /**
     * @param cmd check command and
     * do final process for UILayout component
     */
    private boolean endUILayout(String cmd) {
        String group = current.group;
        if (current.group.length() != 0)
            current.setGroup(group);
        String start = current.getTabName();
        if (!cmd.equals(start)) {
            String msg = "no match for end string<br> start=" + start + "<br>end=" + cmd + "<br>";
            root.setType(UILayout.UI_ERROR);
            root.setValue(setError(msg));
            return false;
        }

        if (current instanceof UIParam) {
            UIParam pa = (UIParam) current;
            pa.removeParam();
        }
        UILayout c = checkSpecialTab();
        UILayout mp = popParent();
        if (c != null)    mp.remove(c);
        processAssign(current);
        boolean skip = setChildAtt(current);
        if (skip) mp.remove(current);
        current = mp;
        return true;
    }

    private boolean setChildAtt(UILayout c) {
        Hashtable table = readHash(c.getAtts());
        c.clearAtts();
        String[] sa = new String[table.size()];
        table.keySet().toArray(sa);
        String result[] = new String[1];
        ArrayList list = c.getList();
        for (int i = 0; i < sa.length; i++) {
            int n = isDot(sa[i], result);
            String key = result[0];
            String value = (String) table.get(sa[i]);
            String[] ssa = Util.split(value, "|");
            switch (n) {
            case 0:
                c.addAtt(sa[i], value);
                break;
            case 1:
                for (int j = 0; j < list.size(); j++) {
                    UILayout cw = (UILayout) list.get(j);
                    setAtt(cw, key, ssa[j]);
                }
                break;
            case 2:
                c.addTdAttChild(key, ssa);
                break;
            case 3:
                if (value.trim().equalsIgnoreCase("true"))
                    return true;
                break;
            }
        }
        return false;
    }

    /*
   * check with key as
   *  match with ".load"  return 0
   *  match with "td."    return 2
   *  match with "."      return 1
   *  match with "skip=y" return 3
   */
    private int isDot(String key, String[] result) {
        if (key.equals("skip"))
            return 3;
        if (key.indexOf(".load") > 0)
            return 0;
        if (key.startsWith("td.")) {
            result[0] = key.substring(3);
            return 2;
        }
        if (key.startsWith(".")) {
            result[0] = key.substring(1);
            return 1;
        }
        return 0;
    }

    /*
   * example
   * <assign onclick|test='submit(12)'  />
   * <assign value|test='2345'  />
   */
    private void processAssign(UILayout c) {
        String assign = c.getAssign();
        if (assign.equals(""))
            return;
        String key = null;
        String[] sa = null;
        String[] keys = null;
        String cmdT =
            "o onload o onload.0 o onload.1 " + "f flag v value r require t title  R reqpt " +
            "V validate l logic m macro ";
        c.setAssign("");
        c.clearTree();
        Hashtable table = readHash(assign);
        Enumeration en = table.keys();
        while (en.hasMoreElements()) {
            key = (String) en.nextElement();
            sa = split(key, "|");
            keys = split(sa[1], ",");
            String cmd = sa[0];
            for (int j = 0; j < keys.length; j++) {
                String k = keys[j];
                String v = (String) table.get(key);
                char cc = findCommand(cmdT, cmd);
                switch (cc) {
                case 'v':
                    c.setValue(k, v);
                    break;
                case 'R':
                    c.setReqpt(k, v);
                    break;
                case 't':
                    c.setTitle(k, v);
                    break;
                case 'V':
                    c.setValidateStr(k, v);
                    break;
                case 'r':
                    c.setRequire(k, v);
                    break;
                case 'l':
                    c.setLogic(k, v);
                    break;
                case 'm':
                    c.setMacro(k, v);
                    break;
                case 'f':
                    c.setFlagStr(k, v);
                    break;
                case 'o':
                    c.addToOnLoad(k, v);
                    break;
                default:
                    c.addAtts(k, cmd + "='" + v + "'");
                    break;
                }
            }
        }
    }

    private String setError(String msg) {
        StringBuffer sb = new StringBuffer();
        sb.append(msg);
        String start = longStr.substring(current.getTextLocation());
        String end = longStr.substring(currentPoint);
        start = start.length() > 200 ? start.substring(0, 200) : start;
        end = end.length() > 200 ? end.substring(0, 200) : end;
        sb.append("\n####START_TAG=" + start);
        sb.append("\n####END_TAG=" + end);
        haveError=true;
        currentPoint = size;
        return sb.toString();
    }

    /**
     * check any special tag here for
     * INSERT,CONTEXT,ASSIGN
     * @return
     */
    private UILayout checkSpecialTab() {
        String type = current.getType();
        boolean isCTX = type.equals(UILayout.UI_CONTEXT);
        boolean isASN = type.equals(UILayout.UI_ASSIGN);
        boolean isINS = type.equals(UILayout.UI_INSERT);
        boolean isCOMMENT = type.equals(UILayout.UI_COMMENT);
        if (!(isCTX || isASN || isINS || isCOMMENT))   return null;
        UILayout cp = getParent();
        String atts = current.getAtts();
        atts=atts.replaceAll(((char) 13)+""," ");
        if (isASN) {
            cp.setAssign(atts);
            return current;
        }
        if (isINS) {
            if (cp instanceof UIParam) {
                UIParam pam = (UIParam) cp;
                pam.setParentParam(current);
            }
            return current;
        }
        Hashtable h = readHash(atts);
        Enumeration en = h.keys();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String value = removeQQ((String) h.get(key));
            if (key.startsWith(MACROTEXT)) textMacro.put(key, value);
            else cp.setContext(key, value);
        }
        return current;
    }

    private String removeQQ(String s) {
        String result = s.trim();
        char c = result.charAt(0);
        if (c == '"' || c == '\'')
            result = result.substring(1);
        c = result.charAt(result.length() - 1);
        if (c == '"' || c == '\'')
            result = result.substring(0, result.length() - 1);
        return result;
    }


    private String getAtts() {
        StringBuffer sb = new StringBuffer();
        char c, c1;
        boolean inQ = true;
        while ((c = getChar()) != 0) {
            inQ = false;
            if (c == '"' || c == '\'') {
                inQ = true;
                sb.append(c);
                while ((c1 = getChar()) != 0) {
                    if (c1=='/' || c=='>') {
                        showError("No End ' or \":"+sb+"");
                        throw new Error();
                    }
                    sb.append(c1);
                    if (c1 == c)
                        break;
                }
            }
            if (inQ)   continue;
            if (c == '/' || c == '>') {
                currentPoint--;
                break;
            }
            sb.append(c);
        }
        return sb.toString();
    }
    
    private void showError(String text) {
        log(this.longStr);
        log(text);
        haveError=true;
    }

    /*
   * @param String cmd
   *             prefix:name if no prefix then it is normal UILayout component
   *             map:name   component on the map table
   *             this:name  UILayoutParase type class method
   *             class:name UILayout       type class
   * @param Hashtable ha   Hashtable for attribute
   */
    private UILayout createObject(String cmd) {
        String prefix = getPrefix(cmd);
        if (prefix == null)
            return uiParse.create(cmd);
        String name = getName(cmd);
        String cmdTable = "m map t this c class";
        String param = currentAtts.get("param");
        char c = findCommand(cmdTable, prefix);
        switch (c) {
        case 'm': return AppMgr.service().getLayout(name, param);
        case 't': return getMainUILayout(name, param);
        case 'c': return uiParse.createClass(name, param);
        default:   break;
        }
        return null;
    }

    private String getPrefix(String s) {
        String ss = s.trim();
        if (ss == null || ss.length() == 0)
            return null;
        String[] sa = split(ss, ":");
        if (sa.length == 1)
            return null;
        else
            return sa[0];
    }

    private String getName(String s) {
        String ss = s.trim();
        if (ss == null || ss.length() == 0)
            return null;
        String[] sa = split(ss, ":");
        if (sa.length == 1)
            return sa[0];
        else
            return sa[1];
    }

    


    private UILayout getMainUILayout(String name, String param) {
        Method m;
        Object obj = null;
        try {
            m = this.getClass().getMethod(name, new Class[] { });
            obj = m.invoke(this, new Object[] { });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (obj == null)
            return (UILayout) obj;
        if (obj instanceof UILayout)
            return (UILayout) obj;
        String s = (String) obj;
        s = replaceMacro(param, s);
        UIParser uiParser = UIParser.instance();
        return uiParser.compile(s);
    }


    public String replaceMacro(String values, String s) {
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
    
    private String swapMacro(String value){
       while(true) 
       {  int n=value.indexOf(MACROTEXT);
          if (n<0) return value;
          String key=value.substring(n,n+2);
          String value1= textMacro.get(key);
          if (value1==null) throw new RuntimeException("Can not find macro key:"+key);
           value1=value1==null? key:value1;
          value=value.replace(key,value1);
       }
        
    }


    private void setAtt(UILayout cw, String key, String value) {
        
        String cmd =
            "g group T td v value n name r require t title" +
            "p param C comment V validate a add f flag o onload o onload.0 o onload.1 ";
        char c = findCommand(cmd, key);
        switch (c) {
        case 'n':
            cw.setName(value);
            break;
        case 'v':
            cw.setValue(value);
            break;
        case 'r':
            cw.setRequire(value);
            break;
        case 't':
            cw.setTitle(value);
            break;
        case 'T':
            cw.addTdAtts(value);
            break;
        case 'g':
            cw.setGroup(value);
            break;
        case 'C':
            cw.setComment(value);
            break;
        case 'V':
            cw.setValidateStr(value);
            break;
        case 'a':
            cw.setCount(0 - Integer.parseInt(value));
            break;
        case 'f':
            cw.setFlagStr(value);
            break;
        case 'o':
            cw.addToOnLoad(value);
            break;
        case 'p':
            break;
        default:
            cw.addAtt(key, value);
            break;
        }
    }

    private char findCommand(String cmd, String pattern) {
        if (pattern.length() < 2)
            return 0;
        int index = cmd.indexOf(pattern.toLowerCase());
        if (index == -1)
            return 0;
        index -= 2;
        return cmd.charAt(index);
    }

    private char getChar() {
        if (currentPoint >= size)
            return 0;
        else
            return longStr.charAt(currentPoint++);
    }

    private String getWord() {
        String result = "";
        char c;
        boolean notOver = true;
        while (notOver) {
            c = getChar();
            switch (c) {
            case ' ':
                notOver = false;
                break;
            case '/':
            case '>':
                currentPoint--;
            case 0:
                notOver = false;
                break;
            default:
                result += c;
                break;
            }
        }
        if (result.length() == 0)
            return null;
        else
            return result;
    }


    private String[] split(String s) {
        if (s == null)
            return new String[0];
        return split(s, "|");
    }

    private Stack stack = new Stack();

    private UILayout getParent() {
        return (UILayout) stack.peek();
    }

    private UILayout popParent() {
        return (UILayout) stack.pop();
    }

    private void saveParent(UILayout c) {
        stack.push(c);
    }


    private String[] split(String text, String pattern) {
        return Util.split(text, pattern);
    }

    private Hashtable readHash(String text) {
        return WUtil.readHash(text);
    }

    private void log(String text) {
        System.out.println(text);
    }


    public boolean isError() {
        return haveError;
    }

    public UIParser getUiParse() {
        return uiParse;
    }
}

