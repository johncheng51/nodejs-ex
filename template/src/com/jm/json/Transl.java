package com.jm.json;

import com.jm.xml.AbstractParse;
import java.util.*;


public class Transl extends AbstractParse {
    private String result="";
    private String head="";
    private Map<String,String> map;
    public Transl(String text,Map map) 
    { 
       super(text);
       this.map=map; 
       parse();
    }

    public Transl(String text,String[] list) 
    { 
       super(text);
       map=new Hashtable(); 
       for (String s:list) map.put(s,s);
       parse();
    }

    private String find(String text)
    {
        for (Object key:map.keySet().toArray()) 
            {
               if (!(key+"").startsWith(text))  continue;
               return map.get(key);
            }
        return "NOT_FOUND_FOR_"+text;
    }
     
     
     public void parse() 
     {
       while(true)
       {
           char c=getChar0();
           switch(c) 
           {
             case '$':head+=value;
                      key=this.skipToChar('.');
                      key= find(key);
                      haveKey=true;
                      value="";
                      break;
             case '\0': result=head+value;
                        return;
             case ' ':
             case 0xD:
             case '\t':if (haveKey) 
                       {
                         head+="'"+key+value+"' ";
                         haveKey=false;
                         value=""; 
                        }
                        break;
             default: value+=c;
           }
       }
         
     }

public static String conv(String text,String[] sa) 
{
    Map map=new Hashtable(); 
    for (String s:sa) 
      {
        map.put(s.substring(1),s+".");
      }
    String result=conv(text,map);
    result=result.substring(1);
    result=result.trim();
    result=result.substring(1,result.length()-3);
    return result;
}
public static String conv(String text,Map map) 
{
    text=text.trim();
    boolean have=text.charAt(0)=='{';
    text=have? text.substring(1,text.length()-1):text;   
    Transl tran=new Transl(text,map);
    String result="{"+tran.result+"}";
    return result;
}
}
 

