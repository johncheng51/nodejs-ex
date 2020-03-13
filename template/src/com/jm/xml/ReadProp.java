package com.jm.xml;


import java.util.Hashtable;
import java.util.Map;


public class ReadProp extends AbstractParse
{
   private boolean  lineC=false;
   public  ReadProp(String data)  
   {  
      super(XmlUtil.trimLF(data)); 
      parse();   
   }
   
   @Override
   public   void parse() 
   {
      while(true)  
      {    char c=getChar0();
           switch(c)
           {
            case '\0':putValue(false);
                      return;
            case '\r':
            case '\n': 
            if (lineC) addValue(' ');
            else   putValue(false);
            break;
            case '\\': 
                      lineC=isSkip();
                      if (!lineC) addValue(c);
                      break;
            case '=':if (haveKey) addValue(c);
                     else   haveKey = true;
                     break;
            default:if (haveKey) addValue(c);
                    else addKey(c);
                    lineC = false;
                    break;}
                    }
 } 
   
  private boolean isSkip()
  {
  	boolean flag=true;
  	for (int i=currentPos;i<size && i<(currentPos+5);i++)
  	{
  	  char c=longStr.charAt(i);
  	  flag =flag && XmlUtil.isSpace(c);
  	}
  	return flag;
  }

    

    public Hashtable getMap()
    {
       
            Hashtable result = new Hashtable();
            Hashtable<String,String> temp = getTable();
            for (String key:temp.keySet())
            {
                String val = temp.get(key);
                Hashtable ht = XmlUtil.readHash(val);
                result.put(key.toLowerCase(), ht);
            }
            return result;
        }
   

    public static Map<String,String> getT(String text) {
        ReadProp p=new ReadProp(text);
        return p.getTable();
    }
  
    
}

