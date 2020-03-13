package com.jm.json;

import java.lang.reflect.Field;

import java.lang.reflect.Method;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Hashtable;

public class JSonEncode 
{
    private Object object;
    private StringBuilder builder=new StringBuilder();
    public JSonEncode(Object obj) 
    {     this.object=obj; 
          serializeValue(object);   }

    private void serializeString(String aString)
   {
     if (aString.equals("null")) {builder.append(aString);return;}
     builder.append("\"");
     for (int i = 0; i < aString.length(); i++) 
     {
	char c = aString.charAt(i);
                                switch(c){
				case '"':builder.append("\\\"");break;
				case '\\':builder.append("\\\\");break;
				case '\b':builder.append("\\b");break;
				case  '\f':builder.append("\\f");
				case '\n':builder.append("\\n");
				case '\r':builder.append("\\r");
				case '\t':builder.append("\\t");
				default:builder.append(c); }
			}
			builder.append("\"");
		}

   private void serializeValue(Object value)
{
  if (value==null) { builder.append("null");return;}
   char mode=(value instanceof String)?    's':
             (value instanceof Hashtable)? 'h': 
             (value instanceof ArrayList)? 'a':
             (value instanceof JsonStyle)?  'j':
             (value instanceof Timestamp)? 't':'d';
                                
                      switch(mode) 
                      {
                           case 's':serializeString((String)value);break;
                           case 'h':serializeHash((Hashtable)value);break;
                           case 'a':serializeArray((ArrayList)value);break;
                           case 'j':serializeObject(value);break;
                           case 't':serializeTime((Timestamp) value);break;
                           default:builder.append(value+"");break;
                           
   		      }
		  
}

 private void serializeTime(Timestamp time) 
 {
    SimpleDateFormat sdf=new  SimpleDateFormat("yyyy-MM-dd hh-mm-ss.SSS");
    builder.append(sdf.format(time));
 }
 
 private void serializeArray(ArrayList anArray)
		{
			builder.append("[");

			boolean first = true;
			for (int i = 0; i < anArray.size(); i++) 
                        {
				Object value = anArray.get(i);
				if (!first)	builder.append(", ");
				serializeValue(value);
				first = false;
			}
			builder.append("]");
			
		}
                
private void serializeObject(Object obj) 
{
   Hashtable table=new Hashtable();
   Field[] fa =obj.getClass().getDeclaredFields();
   for (Field f:fa) 
   {
     String name=f.getName();
     Object val=getProp(obj,name);
     if (val==null) val="null";
     table.put(name,val);  
   }
   table.put("class",obj.getClass().getName());
    serializeHash(table);
}

    private  Object getProp(Object bean, String field) 
    {
        Object result = null;
        Class sClass = bean.getClass();
        String cname = cap(true, field);
        try {
            Method m = sClass.getMethod(cname, new Class[] { });
            result = m.invoke(bean, new Object[] { });
            return result;
        } catch (Exception e) 
        {
            
            return null;
        }
    }

private String cap(boolean isGet, String s) 
   {
        String verb = isGet ? "get" : "set";
        String ret = verb + s.substring(0, 1).toUpperCase() + s.substring(1);
        return ret;
    }

private void serializeHash(Hashtable anObject)
{
  builder.append("{");
  boolean first=true;
  for (Object obj:anObject.keySet())
  {
    String key = (String) obj;
    Object value = anObject.get(obj);
    if (!first) builder.append(", ");
    builder.append(key);
    builder.append(":");
    serializeValue(value);
    first = false;
   }
  builder.append("}");
  }

  @Override
  public String toString() 
  {
     return builder+""; 
  } 

  public static String process(Object json)
		{
			 JSonEncode js=new JSonEncode(json);
			return js+"";
		}
}
