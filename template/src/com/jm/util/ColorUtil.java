package com.jm.util;


import java.awt.Color;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;


public class ColorUtil 
{
   private static Hashtable<String,Color>  table;
   static 
   {
      InputStream is=ColorUtil.class.getResourceAsStream("ColorName.properties");
      Properties p=new Properties();
      try {p.load(is); } catch(Exception e) {}
      table=new Hashtable();
      for(Object keys:p.keySet()) 
      {
          String key=(String) keys;
          String value=((String) p.get(key)).trim();
          table.put(key.trim().toLowerCase(),getColor0(value));
      }
    }

   private static Color getColor0(String ss) 
   {
     String s=ss.substring(1);
     int r=255,g=255,b=255;
     try{
       boolean isInt=s.indexOf("_")>=0;
       if (isInt) 
        {
            String[] sa=Util.split(s,"_");
            r=Integer.parseInt(sa[0]);
            g=Integer.parseInt(sa[1]);
            b=Integer.parseInt(sa[2]);
        }
     else
       {
         String rs=s.substring(0,2);
         String gs=s.substring(2,4);
         String bs=s.substring(4,6);
         r=Integer.parseInt(rs,16);
         g=Integer.parseInt(gs,16);
         b=Integer.parseInt(bs,16);
       }
     }
     catch(Exception e) {System.out.println("ColorUtil:Error code="+ss);}
     return new Color(r,g,b);
  }

   public static Color getColor(String s) 
   {   Color result=null;
       result=table.get(s.trim().toLowerCase());
       if (result!=null) return result;
       result=getColor0("#"+s);
       return result;}

   public static Color getColor(Color r,String s) 
   {
       boolean haveA=s.startsWith("A_");
       s=haveA? s.substring(2):s;
       Color a=getColor(s);
       if (!haveA) return a;
       return new Color(add(r.getRed(),a.getRed()),
                        add(r.getGreen(),a.getGreen()),
                        add(r.getBlue(),a.getBlue()));
   }

   
   private static int add(int a,int b) 
   {
      int c=a+b;
      if (c>255) return a-b;
      else return c;
   }
   
}
