package com.jm.xml;


import com.jm.util.Util;

import java.util.Hashtable;
import java.util.Map;

public class XmlBlockForUI implements XmlHandler
{
   private String       xmlData;
   private XmlParser    parse;
   private Map<String,String>   table=new Hashtable();
   private Map<String,String[]> stable=new Hashtable();
   public  XmlBlockForUI(String xmlData) 
   {   
       this.xmlData=xmlData;
       process();
   }
  
   private void process() 
   {
    parse=new XmlParser(xmlData,this);
    parse.parse();
   }

   public boolean startTag(String cmd,String att) 
       {   
         processBlock(cmd,att);return true;  
       }
       
   public boolean endTag(String cmd,String value) {return true;}    
   private void processBlock(String key,String att) 
   {   
       String   block=parse.getContenda(key);
       String[] sa=Util.split(block,"\n");
       stable.put(key,sa);
       table.put(key,block);
   }
   
   public XmlBlockForUI getBlock(String key){
       String text=table.get(key);
       XmlBlockForUI block=new XmlBlockForUI(text);
       return block;
   }
   
      
   public  String getValue(String key)
   {return  table.get(key); }

   public  String[] getArray(String key)
   {return  stable.get(key); }

    public Map<String,String> getMap()
    {      return table;   }

  

}
  

