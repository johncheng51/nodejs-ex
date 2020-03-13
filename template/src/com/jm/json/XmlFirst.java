package com.jm.json;

import com.jm.model.XmlResult;
import com.jm.xml.XmlHandler;
import com.jm.xml.XmlParser;

public class XmlFirst implements XmlHandler {
    private String       xmlData;
    private XmlParser parse;
    private String cmd;
    private String attData;
    private String restData;
    public XmlFirst( String xmlData)
      {
       this.xmlData=xmlData;
       process();
    }

    private void process() 
   {
    parse=new XmlParser(xmlData,this);
    parse.parse();
   }

   

    public boolean startTag(String cmd, String att) {
       this.cmd=cmd;
       this.attData=att;
       restData=parse.getContenda(cmd);
       return false;  
    }

    
    public boolean endTag(String cmd, String value) {
        return false;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public XmlResult getResult()
    {  return new XmlResult(cmd,attData,restData);  }

    public static XmlResult get(String text) 
   {   return (new XmlFirst(text)).getResult(); }

    
}
