package com.jm.model;
import java.util.*;

/*
 *     type.method:
 *       r.test:show  
 *       r:redirect 
 *       test: method name
 *       args: show
 *
 */
public class PathField extends AbstractField
{
   private String path;   
   private String method;   
   private String view;     
   private String className;
   private String arg;
   private String argType;
  
   public PathField(AbstractModel mode,String name,String text){
       super(mode,name,text);}

    protected void processField(String rest, String value) 
    {
       path=name;
       method=getIndex(path);
       processType(rest);
       processArg(value);
    }

    private void processType(String rest) 
    {
       rest=rest.length()==0? "s":rest;
       char c=rest.charAt(0);
       switch(c)
       {
           case 's':type="simple"   ;break;
           case 'm':type="modelview";break;
           case 'p':type="path"     ;break;
           case 'a':type="arg"      ;break;
           case 'r':type="redirect" ;break;
           case '0':type="action0"  ;break;
           case '1':type="action1"  ;break;
           default: type="simple";break;
       } 
    }


   private void processArg(String value)
   {
        String[] var_varType=splitCommon(value);
        arg=objName=var_varType[0];
        className=cap(objName);
        String varType=var_varType.length>=2? var_varType[1]:arg;
        argType=view=varType;
   }

    protected void processObject(String objName, String type) 
    {
      
    }

    private String path(String text)
   {
    char[] ca=text.toCharArray();
    String result="";
    for(char c:ca) {
        if (c=='.') result+="/";
        else result+=c;
    }
    return result;
   }

     public String getView() {
         return path(view)+".jsp";
     }

     public Map<String,String> getMap() 
    {
        Map map=new Hashtable();
        map.put("path",path);
        map.put("method",method);
        map.put("type",type);
        map.put("obj",objName);
        map.put("class",className);
        map.put("arg",arg);
        map.put("argtype",argType);
        map.put("view",getView());
        map.put("function",objName);
        return map;
    }
}
