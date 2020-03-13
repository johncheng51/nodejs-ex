package com.jm.util;

import java.util.Vector;

public class HtmSplit 
{
    private String text;
    private Vector list=new Vector();
    public HtmSplit(String text) 
    {
        this.text=text;
        process();
    }

    private void process() 
    {
         while(true) 
          {
             int n=  text.indexOf("<TH>");
             if (n<0) break;
             int n1= text.indexOf("</TH>");   
             if (n1<0) break;
             String data=text.substring(n+4,n1);
             list.add(data.trim());
             text=text.substring(n1+5);
          }  
    }

    public Vector getResult()   {     return list;    }
    public static Vector split(String text) 
    {
        HtmSplit htmSplit=new HtmSplit(text);
        return htmSplit.getResult();
    }
}
