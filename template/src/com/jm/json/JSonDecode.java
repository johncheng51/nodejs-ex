package com.jm.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Hashtable;

public class JSonDecode
	{
		private final static char TOKEN_END = 'e'; 
		private final static char TOKEN_CURLY_OPEN = '{';
		private final static char TOKEN_CURLY_CLOSE = '}';
		private final static char TOKEN_SQUARED_OPEN = '[';
		private final static char TOKEN_SQUARED_CLOSE =']';
		private final static char TOKEN_COLON = ':';
		private final static char TOKEN_COMMA = ',';
		private final static char TOKEN_STRING = 's';
		private final static char TOKEN_VALUE = 'v';
		private int index=0;
                private char[] charArray;
                private String  word;
                private Object value=null;
	        private Object currentObj;
              
                public JSonDecode(String text) 
                {
                    charArray=text.toCharArray();
                    currentObj=parseValue();
                }

		
		

private Object parseHash()
{
  Hashtable table = new Hashtable();
  char c=nextToken();
  if (c!=TOKEN_CURLY_OPEN) return null;
   while (true)
  {
   char token = lookAhead();
   switch(token)
     {
      case TOKEN_END: return null;
      case TOKEN_COMMA:nextToken();break;
      case TOKEN_CURLY_CLOSE:nextToken();
                             return hashToObj(table);
      default:String name = parseKey();
      token = nextToken();
      if (token != TOKEN_COLON) return null;
      Object value = parseValue();
      if (value==null) return null;
      table.put(name,value);
      
     }
    }
  
  }

private Object hashToObj(Hashtable<String,Object> hash) 
{
  String className=(String) hash.get("class");
  hash.remove("class");
  if (className==null) return hash;
  Object result=createObj(className);
  for (String key:hash.keySet()) 
    {
     Object val=hash.get(key);
     setProp(result,key,val);
    }
  return result;
}



 private boolean setProp(Object bean, String field,Object value) 
 {
                if (value instanceof String ) 
                {
                    String val=(String) value;
                    value=val.equals("null")? null:value;
                }
	        String cname = cap(false, field);
	        try 
                {
	            Field f = bean.getClass().getDeclaredField(field);
                    Class sClass=f.getType();
	            Method m =  bean.getClass().getMethod(cname, new Class[] { sClass });
	            m.invoke(bean, new Object[] { value });
	            return true;
	        } 
                catch (Exception e) 
                {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public static String cap(boolean isGet, String s) {
	        String verb = isGet ? "get" : "set";
	        String ret = verb + s.substring(0, 1).toUpperCase() + s.substring(1);
	        return ret;
	    }

private Object createObj(String className) 
{
    Class myClass;
        try {
            myClass = Class.forName(className);
            return myClass.newInstance();
        } catch (Exception e) 
        {
            return null;
        }
    }

private ArrayList parseArray()
{
 ArrayList array = new ArrayList();
 char c=nextToken();
 if (c!=TOKEN_SQUARED_OPEN) return null;
 while (true) 
 {
 char token = lookAhead();
 switch(token)
 {
  case TOKEN_END: return null;
  case TOKEN_COMMA:nextToken();break;
  case TOKEN_SQUARED_CLOSE:nextToken();
                           return array;
  case TOKEN_VALUE:array.add(value);break;
 default:Object value = parseValue();
	array.add(value);
}
}
  
}

private Object parseValue()
{
 char mode=lookAhead();
 switch (mode) 
 {
  case TOKEN_STRING:return parseString();
  case TOKEN_CURLY_OPEN:return parseHash();
  case TOKEN_SQUARED_OPEN:return parseArray();
  case TOKEN_VALUE:return value;
  case TOKEN_END:break;
}
return null;
}

private String parseString()
{
 StringBuilder s = new StringBuilder();
 EatWhitespace();
 char c = charArray[index++];
 boolean complete = false;
 while (!complete) 
 {
 if (index == charArray.length) 	break;
 c = charArray[index++];
 if (c == '"')  {complete = true;break;}
 if (c != '\\') {s.append(c);continue; }
 if (index == charArray.length) break;
 c = charArray[index++];
 switch(c){
 case  '"' :s.append('"');break;
 case  '\\':s.append('\\');break;
 case  '/':s.append('/');break;
 case  'b':s.append('\b');break;
 case  'f':s.append('\f');break;
 case  'n':s.append('\n');break;
 case  'r':s.append('\r');break;
 case  't':s.append('\t');break;
 case  'u':int remainingLength = charArray.length - index;
               if (remainingLength<4) {complete=true;break;} 
               char[] unicodeCharArray = new char[4];
               System.arraycopy(charArray, index,unicodeCharArray, 0, 4);
               int codePoint = Integer.parseInt(new String(unicodeCharArray), 16);
               s.append(codePoint);
               index += 4;
               break;					
               }
	}
 if (!complete) 	return null;
return s.toString();
}


private String parseKey()
   {
     if (value!=null) return value+"";
     else return parseString();
   }
   
private void EatWhitespace()
{	
  for (; index < charArray.length; index++) 
  if (" \t\n\r".indexOf(charArray[index]) == -1) return;			
}

                
private char lookAhead()
{   
  int oldIndex=index;	
  char n=nextToken();
  if (n!=TOKEN_VALUE) index=oldIndex;
  return n; 	
}
                
              

private String getWord() 
{
  word="";
  for (; index < charArray.length; index++) 
  {
   char c=charArray[index];
   if (":]},".indexOf(c) >=0) break;
                       word+=c;
  }
  word=word.replaceAll("\r","");
  word=word.replaceAll("\t","");
  word=word.trim();
  return word;
}

private char nextToken()
{
  value=null;
  EatWhitespace();
  if (index >= charArray.length) return TOKEN_END;
  char c = charArray[index];
  index++;
  switch (c) 
  {
	case '{':return TOKEN_CURLY_OPEN;
	case '}':return TOKEN_CURLY_CLOSE;
	case '[':return TOKEN_SQUARED_OPEN;
	case ']':return TOKEN_SQUARED_CLOSE;
	case ',':return TOKEN_COMMA;
        case '\'':
	case '"':return TOKEN_STRING;
        case ':':return TOKEN_COLON;
        default:
  }
  index--;
  String word=getWord().toLowerCase();
  if (!checkObj(word)) value=word;
  return TOKEN_VALUE;
}

    private boolean checkObj(String str) 
    {      
        try { value=Integer.parseInt(str); return true; }
        catch(Exception e) {}
        try { value=Double.parseDouble(str); return true; }
        catch(Exception e) {}
        try {
        SimpleDateFormat sdf=new  SimpleDateFormat("yyyy-MM-dd hh-mm-ss.SSS");
        value=new Timestamp(sdf.parse(str).getTime()); return true; } 
        catch(Exception e) {}
        return false;
    }

    public static Object process(String text) 
        {
            JSonDecode jd=new JSonDecode(text);
            return jd.currentObj;
        }

		

		

		
		
		
	}

