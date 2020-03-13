package com.jm.xml;

import java.util.ArrayList;

public class ScriptParse extends AbstractParse
{
    private ArrayList list=new ArrayList();
    public  ScriptParse(String data) 
    {   
        super(data);
        parse();
    }
    
    @Override    
    public void parse() 
    {   StringBuffer sb=new StringBuffer();
        while(true)  
        {    char c=getChar0();
          switch(c)
          {
           case ';' :list.add(sb+"");
                     sb=new StringBuffer();
                     break;
           case '\0':return;
           case '\'':String text=doQ();
                     sb.append(text);
                     break;         
           default:sb.append(c);
                   break;
           }
         }
    }
    
    public ArrayList getResult() {return list; }
    
    
    
    
}
