package com.jm.xml;

import java.util.Map;

public class HashReplace extends AbstractParse
{
    private Map<String,String> curMap;
    private String result="";
    public HashReplace(String text,Map<String,String> map) 
    {
        super(text);
        curMap=map;
        parse();
    }
    public void parse() 
    {
        while(true)  
        {    
           char c=getChar0();
           switch(c){
            case '{':doAppend();
                      break;
            case '\0':return; 
            default:result+=c;
                    break;
        }
    }
    }

    private void doAppend()
     {
       String text=this.skipToChar('}');
       if (!text.startsWith("$")) { result+="{"+text+"}";return;}
       String key=text.substring(1);
       String value=curMap.get(key);
       if (value==null) { result+="{Key not found for "+key+"}";return;}
       value=value.equals("none")? "":value;
       result+=value;
   
     }
    

    public static String conv(String text,Map<String,String> map) 
    {
        HashReplace hr=new HashReplace(text,map);
        return hr.result;
    }
}
