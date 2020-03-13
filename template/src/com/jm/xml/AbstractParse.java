package com.jm.xml;


import java.util.Hashtable;


abstract public class AbstractParse 
{
	 protected Hashtable  currentMap=new Hashtable();
	 protected String  longStr;
	 protected int  currentPos;
	 protected String  key="";
	 protected String  value="";
	 protected boolean  haveKey=false;
	 protected int  size = 0;
	 protected boolean  _isSpace=false;
	 protected boolean  _isSpaceBefore=false;
         protected char lastChar;
	 protected char  currentChar;
    public AbstractParse() { }
    public  AbstractParse(String data)
	 {	 
		 data  = data==null? "":data;
	         data += " ";
		 this.longStr=data+"  ";
		 size = longStr.length();
                
          }
		 
	 public Hashtable getTable()
	 {
	 	Hashtable newTable=new Hashtable();
	 	for(Object key:currentMap.keySet())
	 	{
	 		String val=(String) currentMap.get(key);
	 		newTable.put(key,XmlUtil.removeQ(val));
	 	}		
	    return  newTable;	 
	 }
	 
	 abstract public  void parse();
	 
	 public void addKey(char c)
	 {		key+=c;	}
	 
	 public void addValue(char c)
	 { value+=c; }
	 
	 public int getTextPos()
	 { return currentPos; }
	 
	 public void moveTextPos(int n)
	 { currentPos=n; }
	 
	 public boolean noKey()
	 {     return NC.isBlank(key); }
	 
	 	 
	 public boolean isSpaceBefore()
	 { return _isSpaceBefore; }
	 
	 public void setLast()
	 { 	lastChar=currentChar;
	 	_isSpaceBefore=_isSpace;	 }
         
         public boolean isBlank(String text) {
             return text.length()==0;
         }
	 
	 public String getValue()
	 {  String result=value;
	    value="";
	    return result;	 }
	 
	 public String getKey()
	 {
	 	String result=key;
	    key="";
	    return result;
	 }   
	 
	 
	 protected  char getChar() 
	 {   
	      currentChar=getChar0();
	     _isSpace=XmlUtil.isSpace(currentChar);
             currentChar = _isSpace ? ((currentChar == '\n' || currentChar=='\r')? 
                           NC.CRTN : NC.CSP) :currentChar;
	     if (currentChar=='"' || currentChar=='\'') 
	     {
	     	value=currentChar+skipToChar(currentChar)+currentChar;
	     	currentChar=NC.CQQ;
	     }
	     return currentChar;
	 }  
	 
	 protected char getChar0()
	 {
	 	char c=currentPos>=longStr.length()? NC.CEND:
	                 longStr.charAt(currentPos++);
	    return c;          
	 }
	 
	 protected  String skipToChar(char patt) 
	  {     
	        char c;
	        String result = "";
	        while(true)
	        { 
	          c=getChar0();   
	          if (c==NC.CEND) break;
	          if (c == patt) return result;
	          result += c;
	        }
	        return null;
	  }
	  
	 protected void skipToNonSp()
	 {	 while(true)
	        { 
	          char c=getChar0();  
	          if (c==NC.CEND) {return;}
	          if (!XmlUtil.isSpace(c)) {currentPos--;return;}
	          
	        }
	    
	 } 
	 
	 protected void putValue(boolean isQ) 
	 { 
	   if (key.length()==0 )  return;
	   if (!isQ && value.length()==0) return;
	   currentMap.put(XmlUtil.trim(key), XmlUtil.trim(value));
	   key="";value="";haveKey= false;
	 }  
	 
	 public String  getContend(String pattern) 
	   {
	        int n = currentPos;
	        skipTo(pattern);
	        String result=longStr.substring(n + 1, currentPos-1);
                return result;
	    }
	 
	 public String  getContenda(String pattern) 
	    {
		    pattern="</"+pattern+">";
	        String result=getContend(pattern);
	        return result;
	    }
	   
	 protected void skipTo(String pattern) 
	    {
	        String s = longStr.substring(currentPos);
	        int n = s.indexOf(pattern);
	        currentPos += (n + 1);
	    }

	 protected String getWord() 
	 {
	        String result = "";
	        char c;
	        boolean notOver = true;
	        while (notOver) 
            {
                notOver = false;
	            c = getChar0();
                switch (c) 
                {
                
                case ' ': break;
	            case '/':
	            case '>':currentPos--;
                         break;
	           
	            default:result += c;
                    notOver = true;
	                    break;
	            }
	        }
	        if (result.length() == 0)   return null;
	        else  return result;
	    }
	 
	 protected String getTempalte(String pat)
	 {
		 String sb="";
		 boolean  isSpace=XmlUtil.trim(pat).length()==0;
		 for (int i=0;i<size;i++)
		 {	 char c=longStr.charAt(i);
			 if (isSpace && c==' ')  {sb+=" ";continue;}
			 String rest=longStr.substring(i);
			 if (rest.indexOf(pat)!=0) sb+=".";
			 else sb+=nchar(pat.length()); }
		 return sb+"";
	 }
	 
	 protected char next()
	 {
	 	if (currentPos==size) return NC.CEND;
	 	char c=longStr.charAt(currentPos);
                return c;
	 }
	 
	 protected String nchar(int n)
	 {
		 String result="";
		 for (int i=0;i<n;i++) result+=" ";
		 return result;
	 }
	 
	 protected String rest()
	 {	 	return longStr.substring(currentPos);	 }
	 
	 protected String getAtts() 
	 {      String sb="";
            char c='0';
	        while ((c = getChar()) != NC.CEND) 
	        {
	           if (c == NC.CQQ) sb+=getValue();
	           if (c == '/' || c == '>') 
	            {
	            	currentPos--;
	                break;
	            }
	            sb+=c;
	        }
	        return sb;
	    }

     public String doQ() 
    {
      char q='\'';
      StringBuffer re=new StringBuffer();
      re.append(q);
      boolean over=false;
      while(!over) 
      {   char c=getChar0();
          switch(c) 
          {
            case '\0':return re+"";
           
            case '\'':re.append(q);
                      char ne=next();
                      if (ne==q) re.append(getChar0());
                      else over=true;
                      break; 
            default:re.append(c);
                    break;          
                      }
          
      }
      return re+"";
    }


}
