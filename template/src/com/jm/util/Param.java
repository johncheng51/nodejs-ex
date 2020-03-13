package com.jm.util;

public class Param 
{
    private String name;
    private Object obj;
    public Param() {}
    
    public void setValue(Object obj) 
    {   this.obj=obj;    }

    public Object getValue() 
    {   return obj;    }

   
    public void setName(String name) 
    {    this.name = name;   }
    public String getName() 
    {    return name;   }

    public String getText()
    { return obj+"";}
    
    
}
