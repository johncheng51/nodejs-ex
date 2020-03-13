package com.jm.xml;
import com.jm.model.TextMap;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class XmlList 
{
   private String xmlData;
   private XmlParser parse;
   private Map<String,String>     strTable=new Hashtable();
   private Map<String,TextMap>  textmTable=new Hashtable();
   private ArrayList list=new ArrayList();
   private int count=0;
   private String key;
  
  
   public XmlList(String xmlData,String key) 
   {   this.xmlData=xmlData;
       this.key=key.trim();
       process();   
   }

   private void process() 
   {
      XmlHandler xh=new XmlHandler() 
      {
       public boolean startTag(String cmd, String att) 
       {   if (!cmd.startsWith(key)) return true;
           processBlock(cmd,att);
           return true;  }

       public boolean endTag(String cmd, String value) {return true;}
     };
    
    parse=new XmlParser(xmlData,xh);
    parse.parse();
   
}
 
  
   private void processBlock(String key,String att) 
   {   
       ReadHash rh=new ReadHash(att);
       String block=parse.getContenda(key);
       String name =(String) rh.getTable().get(NC.NAME);
       name=(name==null)? name="name"+(count++):name;
       TextMap textMap=new TextMap(block,rh.getTable());
       textmTable.put(name,textMap);
       strTable.put(name,block);
       list.add(name);
   }

    public Map<String, String> getTable() {return strTable;}

    public ArrayList<String> getList() {
        return list;
    }

    public Map<String, TextMap> getTextmTable() {
        return textmTable;
    }
}
