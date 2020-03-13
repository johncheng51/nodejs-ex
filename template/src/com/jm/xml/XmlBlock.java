package com.jm.xml;


import com.jm.model.XmlBody;
import java.util.Hashtable;
import java.util.Map;


public class XmlBlock implements XmlHandler
{
   private String       xmlData;
   private XmlParser    parse;
   private static final String NAME="name";
   private Map<String,Map<String,XmlBody>> curTable=new Hashtable();
   private Map<String,String>              simTable=new Hashtable();
   private Map<String,XmlBody>             xmlTable=new Hashtable();
   private boolean forList=false;
   private boolean forXmlBody=false;
   public  XmlBlock(String xmlData) {    this.xmlData=xmlData;}

  
  
   private void process() 
   {
    parse=new XmlParser(xmlData,this);
    parse.parse();
   }

   public boolean startTag(String cmd,String att) 
       {   
         processBlock(cmd,att);
         return true;  
       }
       
   public boolean endTag(String cmd,String value) {return true;}    
   private void processBlock(String key,String att) 
   {   
       String block=parse.getContenda(key);
       if (forList) simTable.put(key,block);
       else if (forXmlBody) 
       {
           XmlBody xmlBody=new XmlBody(key,att,block);
           String name=xmlBody.getMap().get(NAME);
           name=name==null? NAME:name;
           xmlTable.put(name,xmlBody);
       }
       else 
       {
             XmlBody xmlBody=new XmlBody(key,att,block);
             add(key,xmlBody);
       }
   }

   private void add(String key,XmlBody xmlBody)
   {
      Map<String,XmlBody> map=curTable.get(key);
      if (map==null) map=new Hashtable();
      String name=xmlBody.getMap().get(NAME);
      if (name==null) name=key+map.size();
      map.put(name,xmlBody);
      curTable.put(key,map);
   }
  
    public static Map<String,Map<String,XmlBody>> getMap(String text)
     {
        try {
              XmlBlock xmlBlock=new XmlBlock(text);
              xmlBlock.process();
              return xmlBlock.curTable;
            }
        catch(Exception e){}
        return null;   
     }

    public static Map<String,String> getSimple(String text)
     {
        XmlBlock xmlBlock=new XmlBlock(text);
        xmlBlock.forList=true;
        xmlBlock.process();
        return xmlBlock.simTable;
     }

    public static Map<String,XmlBody> getXmlBody(String text)
     {
        XmlBlock xmlBlock=new XmlBlock(text);
        xmlBlock.forXmlBody=true;
        xmlBlock.process();
        return xmlBlock.xmlTable;
     }
    

}
  

