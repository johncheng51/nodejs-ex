package com.jm.util;


import java.util.Hashtable;
import org.mozilla.javascript.*;

public class ScriptFunc extends ScriptableObject 
{
    private static final String SELF = "self";
    private   Context cx;
    protected Scriptable scope;
   

    public ScriptFunc() 
    {
        enter();
        scope = this;
    }

    public ScriptFunc(Context cx, Scriptable scope) {
        this.cx = cx;
        this.scope = scope;
    }

    public Scriptable getCurrentScope() {
        return scope;
    }

    public String getClassName() {
        return "global";
    }

    private void enter() 
    {
        cx = Context.enter();
        cx.initStandardObjects(this);
        String[] names = { "print" };
        defineFunctionProperties(names, ScriptFunc.class, 
                                 ScriptableObject.DONTENUM);
    }

    public String  eval(String s) 
   {    if (s==null) return null;
        try 
        { 
            cx.evaluateString(scope, s, "<cmd>", 1, null);
            return null;
        } 
        catch (Throwable e) 
        {
            String message=e.getMessage().replaceAll("\r\n"," ");
            //message=message.replaceAll(":","\r\n");
            if (message.startsWith("ERROR::") ||
                message.startsWith("Can't find method") ||
                message.indexOf("java.io.FileNotFoundException")>=0 ||
                message.indexOf("There is Error")>=0)                                                         
                throw new RuntimeException(message);
            else e.printStackTrace();
        }
        return null;
    }


   public String evalScript(String s) 
   {    if (s==null) return null;
        try 
        { Object obj = cx.evaluateString(scope, s, "<cmd>", 1, null);
            return null; } 
        catch (Throwable e) 
        { return Util.getException(e);   }
    }

   

    public Object getObject(String s) {
        String str = "\nString been evaluted is  [ " + s + " ]";
        try {
            cx.evaluateString(scope, s, "<cmd>", 1, null);
            Object obj = getProperty(scope, SELF);
            obj = cx.jsToJava(obj, (new Object()).getClass());
            if (obj instanceof UniqueTag)
                throw new RuntimeException("'self' key not found in expression "+str);
            return obj;
        } catch (Throwable e) 
       {
            throw new RuntimeException(str+Util.getException(e));
        }
    }

    public Object createObject(String s) 
    {
        String str = "\nString been evaluted is  [ " + s + " ]\n";
        try {
            cx.evaluateString(scope, s, "<cmd>", 1, null);
            Object obj = getProperty(scope, SELF);
            obj = cx.jsToJava(obj, (new Object()).getClass());
            if (obj instanceof UniqueTag)   return str+"'self' key not found in expression";
            return obj;
        } catch (Throwable e) 
       {    return str+e.getMessage();    }
    }

    public Object[] getObjectNoEx(String s)  
   {   try {
            cx.evaluateString(scope, s, "<cmd>", 1, null);
            Object obj = getProperty(scope, SELF);
            obj = cx.jsToJava(obj, (new Object()).getClass());
            return new Object[] {obj}; 
           } catch (Throwable e) 
             {
               String message=("ScriptFunc.getObjectNoEx|Can not eval String ["+s+"]");
               return new Object[] {null,message};
             } 
           }

    public void addVar(String var, Object obj) 
    {
        Object sobj = Context.javaToJS(obj, scope);
        ScriptableObject.putProperty(scope, var, sobj);
    }

    public void removeVar(String temp) 
    {  Object[] obja= scope.getIds();
        for (int i=0;i<obja.length;i++)
         {
           String name=obja[i]+"";
           if (!name.startsWith(temp)) continue;
           ScriptableObject.deleteProperty(scope,name);
         }
     }

    public ScriptFunc getScope() {
        Context cx = Context.enter();
        Scriptable threadScope = cx.newObject(this);
        threadScope.setPrototype(this);
        threadScope.setParentScope(null);
        return new ScriptFunc(cx, threadScope);
      
    }

    public void showAllElement() 
    {
        StringBuffer sb=new StringBuffer();
        Object[] obja= scope.getIds();
        for (Object o:obja)  sb.append(o+" ");
        String cmd=sb+"";
        
    }

    public boolean isExist(String name) 
    {
       Object[] obja= scope.getIds();
        for (Object o:obja)  if (o.toString().equals(name)) return true;
       return false;
    }

    /**
     * Print the string values of its arguments.
     *
     * This method is defined as a JavaScript function.
     * Note that its arguments are of the "varargs" form, which
     * allows it to handle an arbitrary number of arguments
     * supplied to the JavaScript function.
     *
     */
    public static void print(Context cx, Scriptable thisObj, Object[] args, 
                             Function funObj) 
   {
        for (int i = 0; i < args.length; i++) {
            if (i > 0)  System.out.print(" ");
            String s = Context.toString(args[i]);
            System.out.print(s);  }
        System.out.println();
    }


    /**
     *  the scope is base on the thread object, and it will be
     *  reused if thread have already have
     *  
     */
    private static Hashtable<String,ScriptFunc> scriptTable=new Hashtable(); 
   

   public static ScriptFunc getSF() 
   {
       return getSF("dummy");
   }

   public static ScriptFunc getSF(String className) 
   {
       ScriptFunc staticScope=scriptTable.get(className);
        if (staticScope == null)
         {
            staticScope = new ScriptFunc();
            scriptTable.put(className,staticScope);
         }
        return staticScope.getScope();
    }

  
}

