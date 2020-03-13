package com.jm.util;

import java.io.*;
import java.util.prefs.*;
import java.util.*;

public class PrefUtil 
{
   private static final String ALREADY_LOAD="already_load";
   private static final String YES="yes";
   /**
     * Throw Runtime Exception if there is no value for the key
     * @param node: descript by a.b.c
     * @param key : key under the node
     * @return the value key  
     */
public static String getAndCheck(String node, String key)
{
   String val=get(node,key);
   if (val.length()!=0) return val;
   throw new RuntimeException("\nPlease enter the value under folder ["+node+"] with key ["+
               key+"] for Java Preferences");
}


public static String get(String node,String key)
 {
    try{Preferences pnode=getNode(node);
    String[] keys=pnode.keys();
    if (keys.length==0) return "";
    return pnode.get(key,""); 
}  catch(Exception e) {return "";}}

public static String[] getNodes(String node) 
 {
    try
   {
      Preferences pnode=getNode(node);
      return pnode.childrenNames();
    }
    catch(Exception e) {return null;}
 }

public static String[][] getPairs(String node) 
 { 
   try
    {
      Preferences pnode=getNode(node);
      String[] keys=pnode.keys();
      String[][] result=new String[keys.length][2];
      for (int i=0;i<keys.length;i++)
          {
            result[i][0]=keys[i];
            result[i][1]=get(node,keys[i]);
          }
      return result; 
    }
    catch(Exception e) {return new String[0][];}
 }


public static void putPairs(String node,String[][] pairs) 
 { 
   try
    {
      removeNode(node);
      for (int i=0;i<pairs.length;i++) put(node,pairs[i][0],pairs[i][1]);
    }
    catch(Exception e) {}
 }


public static void putObject(String node,String key,Object o) 
{ 
 try{
  ByteArrayOutputStream baos=new ByteArrayOutputStream();
  ObjectOutputStream oos=new ObjectOutputStream(baos);
  oos.writeObject(o);
  Preferences pnode=getNode(node);
  pnode.putByteArray(key,baos.toByteArray()); 
 }
catch(Exception e) {e.printStackTrace();}
}


public static Object getObject(String node,String key)
{ 
 try{
  Preferences pnode=getNode(node);
  byte[] ba=pnode.getByteArray(key,null);
  ByteArrayInputStream bais=new ByteArrayInputStream(ba);
  ObjectInputStream ois=new ObjectInputStream(bais);
  Object result=ois.readObject();
  return result;
 } catch(Exception e){return null;}
 }
 
   /**
     * put value for the key under the node tree 
     * @param node: descript by a.b.c
     * @param key : key string under the node
     * @param value
     */

public static void put(String node,String key,String value)
 { try{Preferences pnode=getNode(node);
    pnode.put(key,value); }
   catch(Exception e) {} }

   /**
     * compare the val from tree with value
     * @param node: descript by a.b.c
     * @param key : key string under the node
     * @param value
     * @return
     */

public static boolean match(String node,String key,String value) 
  {  String result=get(node,key);
     return result.equalsIgnoreCase(value);   }
      
   /**
     * 
     * @param node: descript by a.b.c
     * @return
     * @throws Exception
     */
      
public static String printChild(String node) throws Exception
   {  StringBuffer sb=new StringBuffer();
      Preferences p=getNode(node);
      String child[] =p.childrenNames();
      for (int i=0;i<child.length;i++) 
      sb.append(child[i]+"\n");
      return sb+"";}     

 
 
 
  public static void  removeNode(String node) 
   {  try
     { 
      Preferences pnode=getNode(node);
      pnode.removeNode();
       
     }
     catch(Exception e)  {e.printStackTrace();}
   } 
  
 
  private static Preferences getRoot() 
   {  //return (!isUser)? Preferences.systemRoot().node(""): 
      return  Preferences.userRoot().node("");}

 
 

  private static Preferences getNode(String node) 
   {  try
     { Preferences pnode=getRoot();
      String[] sa=Util.split(node,".");
      for (int i=0;i<sa.length;i++) 
         { pnode=pnode.node(sa[i]); }
       return pnode; }
     catch(Exception e){e.printStackTrace();return null;}
   }

  
   /**
     * load preference value into the system under the node
     * @param node
     * @param is
     */
  public static void loadPref(String fileName,String node,Class c) 
  {
     InputStream is=c.getResourceAsStream(fileName);
     loadPref(node,is); 
  }

  public static void loadPref(String node,InputStream is) 
  {
      
    boolean isOn=match(node,ALREADY_LOAD,YES); 
    if (isOn) 
     {
      log("Preference is already exist!!!");  
      return;
     }
    log("Loading the preference!!!");
    removeNode(node);
    try
     {
      Properties p=new Properties();
      p.load(is);
      Enumeration en=p.keys();
      while(en.hasMoreElements()) 
      {
         String key=(String) en.nextElement();
         String val=(String) p.get(key);
         put(node,key,val);
      }
      put(node,ALREADY_LOAD,YES);    
     }
   catch(Exception e) {e.printStackTrace();}
  }
 
 
private static void log(String s) {
    System.out.println("PreUtil:"+s);
}
  
  
  
}
