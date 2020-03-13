package com.jm.process;


import com.jm.mgr.SwapMgr;
import com.jm.model.*;
import java.util.*;



public class ProcessPath extends ProcessWeb
{
  public static final String PATH="path_";   
  public static final String SPRING="spring"; 
  public static final String BODY="body"; 
  public static final String CONTROLLER="controller"; 
  public static final String DOT="."; 
  public static final String MODEL="model";
  public static final String JAVA="java";
  public static final String SPRINGCONTROL="springcontroller";

    public ProcessPath(String text) {super(text);}
  public void path(XmlResult result) 
    {  processMethod(result,PATH); } 
 
  public void path_spring(String value)
  {
      PathModel pathModel=modelMgr.getPathModel(value);
      List<PathField> list=pathModel.list();
      for (PathField f:list) 
      {
          Map map=f.getMap();
          String template=SwapMgr.instance().
                getTemplate(f.getType(), SPRING);
          append(freeMarker.tranWithText(template,map));
      }
      Map map=new Hashtable();
      map.put(BODY,getCurReport());
      map.put(CONTROLLER,pack+DOT+CONTROLLER);
      map.put(MODEL,pack+DOT+MODEL);
      map.put("className",cap(value));
      String template=freeMarker.readTemplate(SPRINGCONTROL);
      append(freeMarker.tranWithText(template,map));
      writeSource(pack+DOT+CONTROLLER,cap(value)+DOT+JAVA);
  }

  public static void main(String[] args)
  {
     new ProcessPath("springPath.xml");
  }
}
