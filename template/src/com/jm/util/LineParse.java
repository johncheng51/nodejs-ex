package com.jm.util;

public class LineParse 
{
    private String longStr;
    private int cp = 0;
    private int size = 0;

    public LineParse(String s) {
        longStr = s;
        size = longStr.length();
    }

    private char getChar() {
        if (cp >= size)  return 0;
        else  return longStr.charAt(cp++);
    }

    public String getLine(String pattern) 
   {
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = getLine()) != null) 
       {
            if (line.startsWith(pattern))   break;
            sb.append(line + ">");
        }
        return sb.toString();
    }

    public String getMatch(String pattern) {
        String line = null;
        while ((line = getLine()) != null) 
       {
            if (!line.startsWith(pattern))    continue;
            return line.substring(pattern.length());
        }
        return null;
    }

    public String getLine() {
        char c;
        StringBuffer sb = new StringBuffer();
        while ((c = getChar()) != 0) 
        {     if (c == '>')  break;
              sb.append(c);    }
        String result = sb.toString();
        if (result.length() == 0)  return null;
        else  return result.trim();
    }

   public static String addReturn(String s) 
   {
       String result="";
       LineParse lp=new LineParse(s);
       while(true) {
           String ss=lp.getLine();
           if (ss==null) return result;
           result+=ss+">\n";
       }
   }
}

