package com.jm.ui;

import com.jm.model.LookupList;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Vector;
import javax.servlet.http.*;
import com.jm.util.*;


public class WebSession 
{
  

   public static void printHash(Hashtable hashtable)
 {
   Enumeration en=hashtable.keys();
   while(en.hasMoreElements())
   { String s=(String) en.nextElement();
     log(s+"="+hashtable.get(s));
   }      
 }

  public static String printObj(Object obj)
 {  StringBuffer sb=new StringBuffer();
    Class cl=obj.getClass();
    Field[] fa=cl.getDeclaredFields(); 
    for (int i=0;i<fa.length;i++)
         { String name=fa[i].getName();
           try {sb.append(name+"="+fa[i].get(obj)+"\n");}
           catch(Exception e) {}
         }
    return sb.toString();          
 }

   /**
   * 
   * @param obj
   * @return Field only contain with String
   */
 public static Field[] getFields(Object obj)
 {      
        HashSet hs=new HashSet();
        Class cla=obj.getClass();
        Field[] fa=cla.getDeclaredFields(); 
        for (int i=0;i<fa.length;i++)
         if (fa[i].getType().equals(new String().getClass())) hs.add(fa[i]);
         fa=new Field[hs.size()];
         hs.toArray(fa);
         return fa;
 }   

  public static Object readFromReq(HttpServletRequest req,String className)
 {      String name=null,value=null;
        Object ob=makeObject(className);
        if (ob==null) return null;
        Class cla=ob.getClass();
        Field[] fa=cla.getDeclaredFields(); 
        for (int i=0;i<fa.length;i++)
         { name=fa[i].getName();
           if (name.startsWith("x_")) continue;
		   if (name.endsWith("_DATA")) continue;
		   value=null;
           if (name!=null)  value=req.getParameter(name);
           if (value==null) value="";
           WUtil.setProperty(ob,name,value);  }
         return ob;
}

    public static Object makeObject(String className)        
    {   
       Class cla=null;
       try{  cla=Class.forName(className);
            return cla.newInstance();   }
            catch(Exception e) 
              {
                return null;
              }     
    }

   public static Object readFromHash(HttpServletRequest req,String sname,String className,Hashtable table)
	 {      String name=null,value=null;
		    Object ob=createData(req,sname,className);  
		    Class cla=ob.getClass();
			Field[] fa=cla.getDeclaredFields(); 
			for (int i=0;i<fa.length;i++)
			 {
			   name=fa[i].getName();
			   if (name.startsWith("x_")) continue;
			   if (name.endsWith("_DATA")) continue;
			   value=null;
			   if (name!=null)  value=(String) table.get(name);
			   if (value==null) value="";
			   try{fa[i].set(ob,value);}
			   catch(Exception e){log("Can not set member function="+fa[i].getName());}
			 } 
			return ob;   
	  }  

   private static String findMatch(HttpServletRequest req,String s)
 { String key=null;
   Enumeration en=req.getParameterNames();
   while(en.hasMoreElements())
   { key=(String) en.nextElement();
     if (key.toLowerCase().endsWith(s.toLowerCase())) return key;   }
   return null;  
 }

  public static Object createData(HttpServletRequest req, String sname,String className)        
 {
  Class cla=null;
   HttpSession se=req.getSession();
   Object ob=se.getValue(sname);
   if (ob!=null) return ob;
   try{  cla=Class.forName(className);
         ob=cla.newInstance();   }
             catch(Exception e) {e.printStackTrace();}  
   se.putValue(sname,ob);;             
   return ob;          
 }

  public static String printReq(HttpServletRequest req)
 {
  String key=null;
  String value=null;
  StringBuffer sb=new StringBuffer();
  sb.append("<!-- Request Parameter Name");
  Enumeration en=req.getParameterNames();
  int count=0;
  while(en.hasMoreElements())
  {  key=(String) en.nextElement();
     value=req.getParameter(key); 
     sb.append(count++ +" "+key+"="+value+"\n");
  }
  sb.append("Request Parameter Name END-->");
  return sb.toString(); }

   public static String printSession(HttpServletRequest req)
 {
  String key=null;
  Object value=null;
  HttpSession se=req.getSession();
  StringBuffer sb=new StringBuffer();
  sb.append("<!-- Session List");
  String[] sa= se.getValueNames();
  for (int i=0;i<sa.length;i++)
  {  key=sa[i];
     value= se.getValue(key); 
     sb.append(i+" "+key+"="+value+"\n");
  }
  sb.append("Session List End -->");
  return sb.toString(); }

  public static void copyBean(Object source,Object target,String name)
{   
	String result="";
	Class sClass=source.getClass();
	Field[] tfa=getFields(target);
	for (int i=0;i<tfa.length;i++)
	 { 
	  if (tfa[i].getType()!=result.getClass()) continue;
	  try { Method m=sClass.getMethod("get"+name+tfa[i].getName(),new Class[] {});
			 result = (String) m.invoke(source,new Object[] {});            
			tfa[i].set(target,result);}
	  catch(Exception e) {}            
	 }  
} 

 

 public static String[][] getData(String fields,Vector v,Vector lookup)
{	
	Object object=null;
	String[] sa=WUtil.split(fields,"|");
	String[][] data=new String[v.size()][sa.length]; 
	for (int j=0;j<v.size();j++)
	{    object=v.get(j);
		 for (int i=0;i<sa.length;i++)
			data[j][i]=getProperty(sa[i],object,(Vector) lookup.get(i));
	 	
	}
	return data; 
}

public static String[][] getData(String fields,Vector v)
{	
	Object object=null;
	String[] sa=WUtil.split(fields,"|");
	String[][] data=new String[v.size()][sa.length]; 
	for (int j=0;j<v.size();j++)
	{    object=v.get(j);
		 for (int i=0;i<sa.length;i++)
			data[j][i]=getProp(sa[i],object);
	 	
	}
	return data; 
}		

private static String getProperty(String name,Object obj,Vector lookup)
 {   String result=getProp(name,obj);
	 if (lookup.size()==0) return result;
	 for (int i=0;i<lookup.size();i++)
	  {
		LookupList ll=(LookupList) lookup.get(i);
		if (result.equals(ll.value)) return ll.desc; 	   
	  }
	 return "";
}	

public static String getProp(String name,Object obj)
{  
	String result="";
	try 
	{ Method m=obj.getClass().getMethod("get"+name,new Class[] {});
	  result = (String) m.invoke(obj,new Object[] {});}
	catch(Exception e){}  
	return result;
}	

private static Hashtable search(String key,Hashtable ht)
{ String match=null;
  String nmatch=null;
  Hashtable newTable=new Hashtable();
  Enumeration en=ht.keys();
  while(en.hasMoreElements())
  {  match=(String) en.nextElement();
     if (key.startsWith(match)) continue;
     nmatch=match.substring(key.length());
     newTable.put(nmatch,ht.get(match));
  }
  return newTable;
}

public static void log(String s)
{ WUtil.log(s+" IN WebSession");}

private static Hashtable threadTable = new Hashtable();


private static Hashtable getThreadTable() 
    {
        Thread thread = Thread.currentThread();
        Hashtable hashtable = (Hashtable)threadTable.get(thread);
        if (hashtable != null)   return hashtable;
        hashtable = new Hashtable();
        threadTable.put(thread, hashtable);
        return hashtable;
    }



public static String printProp()
{ 
    String result="";
    Properties p = System.getProperties();
    Enumeration keys = p.keys();
    while (keys.hasMoreElements()) 
   {
    String key = (String)keys.nextElement();
    String value = (String)p.get(key);
    result+=(key + ": " + value+"<BR>");
   }
    return result;
}

public static String printEnv()
{ 
    String result="";
    Map map=System.getenv();
    Object[] keys = map.keySet().toArray();
    for (Object key:keys)
   {
      String value = (String)map.get(key);
      result+=(key + ": " + value+"<BR>");
   }
    return result;
}

public static String printFiles(String search,String type) 
{ 
    if (search==null) return "";
    boolean isEnv=type.equals("ENV");

    String folder=isEnv? System.getenv(search):
                     System.getProperty(search);
    if (folder==null) return "<H4>"+ search+" not found </H4>";
    File file=new File(folder);
    TreeSet<String> set=new TreeSet();
    String result="";
    putFile(set,file);
    for (String line:set) result+=line;;
    return result;
}

public static void putFile(TreeSet<String> set,File file)
{
    if (file.isDirectory()) set.add("<H2>"+file+"</H2>");
    else  { set.add(file+"</BR>");return;}
    File[] ff=file.listFiles();
    for (File f:ff)  
      {
         try {putFile(set,f);}
         catch(Exception e) {}
      }
}

}

 
