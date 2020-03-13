package com.jm.mgr;


import com.jm.model.XmlBody;
import com.jm.util.Util;
import com.jm.xml.XmlBlock;
import java.lang.reflect.Method;
import java.util.Map;

public class FuncMgr implements DoUpdate{
   
    private static final String PARAM = "param";
    private static final String FUNCTIONS = "functions";
    private FileMgr fileMgr=FileMgr.instance();
    private Map<String,XmlBody> funcsMap=null;
   

    public FuncMgr() {
        String text=fileMgr.readFile(this,FUNCTIONS);
        funcsMap=XmlBlock.getMap(text).get("func");
    }

    public String read(String key, String value) {
       
        String[] comd_key = Util.split(key, ".");
        String comd = comd_key[0];
        String key1 = comd_key[1];
        try {
            Method m = this.getClass().getMethod(comd, new Class[] { String.class, String.class, String.class });
            return (String) m.invoke(this, new Object[] { comd, key1, value });
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }

    }

    private String addDot(String text) {
        String result = "";
        String[] sa = Util.split(text, " ");
        for (String s : sa)
            result += s + ",";
        result = Util.removeLast(result);
        return result;
    }

    public String watch(String comd, String key, String value) {
        String template =null; //propsMgr.getP(comd);
        XmlBody body = funcsMap.get(value);
        String param = addDot(body.get(PARAM));
        String data = Util.printf(template, new Object[] { key, param, body.body() });
        return data;
    }

    

    private static FuncMgr mgr;
    private static Object lock = new Object();

    public synchronized static FuncMgr instance()

    {
        if (mgr != null)
            return mgr;
        synchronized (lock) {
            mgr = new FuncMgr();
            return mgr;
        }
    }

    @Override
    public void update() {
        // TODO Implement this method
    }
}
