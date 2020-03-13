package com.jm.xml;



import java.util.*;

public class CsvParse extends AbstractParse
{
    private List<String> list=new ArrayList();
    private boolean putq;
    public CsvParse(String data,boolean putq) 
    { 
      super(data);
      this.putq=putq;
      parse();  
    }

    private String qq(String text)
    {
         if (!putq) return text;
         String Q="'"; 
         return Q+qqq(text)+Q;
    }

    @Override    
    public void parse() 
    { 
       String key="";
        while(true)  
        {    char c=getChar0();
          switch(c)
          {
           case '$':
           case ' ': break;
           case '`' :
           case '\"':key=qq(skipToChar(c));break;
           case '\'':key=qq(doQ());break;
           case ',' :key=NC.isBlank(key)? "null":key;
                     list.add(key);
                     key="";break;
           case '\0':return;          
           default:key+=c;break;
           }
         }
    }

    private String qqq(String text) 
    {   
        String re="";
        for (char c:text.toCharArray())  
         re+=c=='\''?
         new String(new char[]{c,c}):
         new String(new char[]{c});
        return re;
    }
   
    public static List<String> csv(String text,boolean putq) 
    {
         CsvParse cp=new  CsvParse(text,putq);
         return cp.list; 
    }

}
