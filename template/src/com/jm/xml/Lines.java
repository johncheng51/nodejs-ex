package com.jm.xml;


import java.util.ArrayList;



public class Lines extends AbstractParse
{
    private ArrayList<String> table = new ArrayList();
    public  Lines(String str)
	 { 
           super(str);    
           parse();
        }
	 
	 @Override
	  public  void parse() 
	 {   String sb="";
		 while(true)  
	      {    char c=getChar0();
	           switch(c)
	           {
	            case '\0':add(sb);
                          return;
	            case '\r':
	            case '\n':add(sb);
	                      sb="";break;
	            default:sb+=c;break;}}  
	 }
	 
	 private void add(String str)
	 {
		 //if (XmlUtil.isBlank(str)) return;
		 table.add(str);
	 }

    public String[] getLines()
    {
      
        ArrayList<String> result=new ArrayList();
        for (String line:table)
        {
            if (line.length() == 0) continue; 
            result.add(line);
        }
        String[] re=new String[result.size()];
        result.toArray(re);
        return re;
    }

}

