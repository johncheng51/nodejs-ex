package com.jm.model;

import com.jm.json.*;
import com.jm.util.Util;
import java.util.*;
public abstract class AbstractModel 
{   
    protected String type;
    protected String name;
    protected String table;
    protected String mapby ="ERROR";
    protected List list=new ArrayList();
    public AbstractModel(XmlBody xmlBody) 
    {
       type=xmlBody.get("type");
       name=xmlBody.get("name");
       table=xmlBody.get("table");
       if (table==null) table=name; 
       JasonData jasonData=JasonParse.jasonData(xmlBody.body());
       process(jasonData); 
    }

    private void process(JasonData jasonData) 
    {
        char mode=Util.findCommand("c class p path",type);
       for (String key:jasonData.getList()) 
        {
           AbstractField field=null;
           String value=jasonData.getMap().get(key);
           switch(mode) 
           {
             case 'c': field=new ClassField(this,key,value);break;
             case 'p': field=new PathField(this,key,value);break;
           }
           list.add(field);
        }
    }
    
    public String type() {return type;}
    public String name() {return name;}
    public String uname() {return name.toUpperCase();}
    public String cname() {return Util.cap(name);}
    public void setMapby(String mapby) {
        this.mapby = mapby;
    }

    public String getMapby() {
        return mapby;
    }
   
    
}
