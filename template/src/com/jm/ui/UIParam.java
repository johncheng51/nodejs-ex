package com.jm.ui;
import java.util.*;
import com.jm.util.*;

public class UIParam extends UILayout 
{
  private UILayout   parentParam;
  private String      style="";
  private int         colNumber=1;
  private UILayout   current;
  private  UIParser uiParse=UIParser.instance();
  
  public UIParam(String s)
  {
    setType(UILayout.UI_ARRAY);
    super.add(uiParse.compile(s));
  }
  
  @Override
  public UILayout add(UILayout c)
  {
    if (c.type.equals(UILayout.UI_INSERT)) 
      { 
        super.add(c);
        return this;
      }
    UILayout cc=c;  
    if (style.equals("column"))  
       { if (count==0) 
         {  
            current=uiParse.create(UILayout.UI_ROW);
            current.add(c);
            addAt(list,current);
         } 
         else if (count<colNumber) current.add(c);
         count++;
         count = (count==colNumber)? 0:count;
        }
    else addAt(list,cc);
    return this;
  }
  
  public void setParentParam(UILayout c)
  { 
    parentParam=c;
    Hashtable hs =Util.readHashh(parentParam.attributes);
    style=(String) hs.get("style");
    style=(style==null)? "":style;
    String number=(String) hs.get("number");
    if (number!=null) 
    {
      try { colNumber =Integer.parseInt(number);}
      catch(Exception e) {} 
    }
    else colNumber=1;
  }
  
  private boolean addAt(ArrayList list,UILayout c0) 
  {
    String name =(parentParam==null)? null:parentParam.gname();
    for (int i=0;i<list.size();i++)
    { 
      UILayout c=(UILayout) list.get(i);
      if (!c.getType().equals(UILayout.UI_PARAM)) continue;
      if (name !=null && b(c.gname())) continue;
      if (name !=null && !c.gname().equals(name))  continue;
      list.add(i,c0);
      return true; 
    }
    for (int i=0;i<list.size();i++)
    {
      UILayout c=(UILayout) list.get(i);
      ArrayList al=c.getList();
      if (al==null) continue;
      if (addAt(al,c0)) return true;
    }  
    return false;
  }
  
  public  void removeParam()
  {
    removeParam(list);
  }
  
  private void removeParam(ArrayList list)
  {
    int count=0;
    while(true)
    { 
      if (count>=list.size()) break;
      UILayout c=(UILayout) list.get(count);
      count++;
      if (!c.getType().equals(UILayout.UI_PARAM)) continue;
      list.remove(c);
      count =0;
    }
    for (int i=0;i<list.size();i++)
    {
      UILayout c=(UILayout) list.get(i);
      ArrayList al=c.getList();
      if (al==null) continue;
      removeParam(al);
    }
  }
}  
  