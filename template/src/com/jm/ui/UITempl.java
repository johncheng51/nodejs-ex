package com.jm.ui;

public class UITempl extends UILayout
{
  private int count=0;
  public UITempl(String s)
  {
    
    setType(UILayout.UI_ARRAY);
    super.add(uiParser.compile(s));
  }

  @Override
  public UILayout add(UILayout c)
   {
      String key="###"+count++;
      UILayout layout=findObject(key,false);
      if (layout!=null)  layout.add(c);
      return this;
   }

   
}
