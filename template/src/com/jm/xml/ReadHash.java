package com.jm.xml;

import java.util.Map;


public class ReadHash extends AbstractParse
{
    
public  ReadHash(String data)
    {  
      super(data);
      parse();    
    }

@Override    
public   void parse() 
   {
      while(true)  
      {    char c=getChar();
           switch(c){
            case '\0':return;
            case ' ':
            case '\n':putValue(false);
                      break;
            case '=':haveKey = true;
                     break;
            case '\'':putValue(true); 
                      break;
            default:if (!haveKey) addKey(c);
                    else addValue(c);
                    break;}}} 

public static Map<String,String> get(String text) 
{   return (new ReadHash(text)).getTable(); }
                    
                     
 }
 


