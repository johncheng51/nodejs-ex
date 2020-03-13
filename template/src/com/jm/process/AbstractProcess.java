package com.jm.process;

import com.jm.mgr.*;
import com.jm.model.*;
import com.jm.util.Util;
import com.jm.xml.*;
import java.lang.reflect.Method;
import com.jm.json.*;
import java.util.*;

public class AbstractProcess implements DoUpdate {
    public static final String SERVICE = "service";
    public static final String START = "start";
    public static final String NAME = "name";
    public static final String NONE = "none";
    public static final String ARGS = "args";
    public static final String SCRIPT = "script";
    public static final String PACKAGE = "package";
    public static final String END = "end";
    private String text;
    private String file;
    protected StringBuffer primaryData = new StringBuffer();
    protected StringBuffer secondData = new StringBuffer();
    protected SwapMgr swapMgr=SwapMgr.instance();
    public FileMgr fileMgr = FileMgr.instance();
    public ModelMgr modelMgr = ModelMgr.instance();
    public FreeMarker freeMarker = FreeMarker.instance();
    protected String pack;

    public AbstractProcess(String name) {
        file = name;
        if (file.indexOf(".")<=0) file+=".xml";
        text = fileMgr.readFile(this, "main", file);
        log("Reading text file:" + file);
        log("text data:\n===========\n" + text + "\n=============");
        start();
    }

    public Object[] packArgs(XmlResult result) {
        String args = result.get(ARGS);
        String[] sa = Util.split(args, " ");
        String argsText = "";
        for (String key : sa)
            argsText += key + ",";
        argsText = Util.removeLast(argsText, 1);
        return new Object[] { argsText, sa };
    }

    public String[] packArgss(XmlResult result) {
        String args = (String) result.getMap().get(ARGS);
        String[] sa = Util.split(args, " ");
        String argsText = "";
        String argsText1 = "";
        for (String key : sa) {
            argsText += "'" + key + "',";
            argsText1 += key + ",";
        }
        argsText = Util.removeLast(argsText, 1);
        argsText1 = Util.removeLast(argsText1, 1);
        return new String[] { argsText, argsText1 };
    }

    protected String getCurReport(boolean isPr) {
        String data = (isPr) ? primaryData + "" : secondData + "";
        clear(isPr);
        log(data);
        return data;
    }

    protected void clear(boolean isPr) {
        if (isPr) primaryData = new StringBuffer();
        else  secondData = new StringBuffer();
    }

    protected void clear() {
        clear(true);
    }

    protected String getCurReport() {
        return getCurReport(true);
    }

    protected boolean appendApp(XmlBody xmlBody) {
        String services = xmlBody.get(SERVICE);
        boolean noService = false;
        String serviceText = "";
        boolean noneService = services.equals(NONE);
        if (!noneService) {
            String[] sa = Util.split(services, " ");
            noService = sa.length == 0;
            for (String key : sa)
                serviceText += "'" + key + "'" + ",";
            serviceText = Util.removeLast(serviceText, 1);
        }
        String templ =null; //propsMgr.getX(noneService ? START + 2 : noService ? START + "1" : START);
        String name = xmlBody.get(NAME);
        String app = Util.printf(templ, new Object[] { name, serviceText });
        append(app);
        return noService;
    }

    private void start() {
        Map<String, XmlBody> map = XmlBlock.getXmlBody(text);
        String[] keys = new String[map.size()];
        map.keySet().toArray(keys);
        //append(propsMgr.getP(START, true));
        for (String key : keys) {
            XmlBody xmlBody = map.get(key);
            boolean noService = appendApp(xmlBody);
            if (noService)
                continue;
            XmlResult result = XmlFirst.get(xmlBody.body());
            String cmd = result.getCmd();

            try {
                Method method = this.getClass().getMethod(cmd, XmlResult.class);
                method.invoke(this, new Object[] { result });
            } catch (Exception e) {
               throw new Error(e.getMessage());
            }
        }
        writeFile();
    }

    protected String cap(String name) {
        return Util.cap(name.toLowerCase());
    }
   

    private void writeFile() {
        if (primaryData.length()==0) return;
        String newFile = Util.getFirst(file, ".") + ".js";
        fileMgr.writeFile(newFile, primaryData + "");
        clear();
    }

    public void writeSFile(String folder, String file) {
        Util.writeFile(folder, file, getCurReport(false));
    }

    public void writeFile(String folder, String file) {
        Util.writeFile(folder, file, getCurReport(true));
    }


    protected void append(String text, Map<String, String> map) {
        append(HashReplace.conv(text, map));
    }
   

    protected void processMethod(XmlResult result) {
        processMethod(result, "");
    }

    protected void processMethod(XmlResult result, String preFix) {
        pack = result.get(PACKAGE);
        JasonData jd = JasonParse.jasonData(result.value());
        for (String key : jd.getList()) {
            String value = jd.get(key);
            try {
                Method method = this.getClass().getMethod(preFix + key, new Class[] { String.class });
                method.invoke(this, new String[] { value });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
   

    public void append(String message) {
        append(message, true);
    }

    public void append(String message, boolean isPr) {
        if (message == null)   return;
        if (isPr)   primaryData.append(message + "\r\n");
        else    secondData.append(message + "\r\n");
    }

    protected String prop(String key){
        return "ERROR";
    }

    protected String path(String text) {
        char[] ca = text.toCharArray();
        String result = "";
        for (char c : ca) {
            if (c == '.')  result += "/";
            else result += c;
        }
        return result;
    }

    public void log(String msg) {System.out.println(msg);}


    public void update() {
    }


}

