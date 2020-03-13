package com.jm.util;

import com.jm.model.LookupList;
import com.jm.ui.UILayout;
import  java.util.*;
import java.io.*;
import java.lang.reflect.*;
public class WUtil 
{
  
 public static Vector createDummy(int n)
 {  
    Vector v = new Vector();
    String str="";
    for (int i=0;i<n;i++) str+="  ";
    LookupList lk=new LookupList();
    lk.value=str;
    lk.desc=str;
    v.add(lk);
    return v;
 }
 public static String getValue(Vector v)
 {  LookupList lk=(LookupList) v.get(0);
    return lk.value; }
 
  public static Hashtable<String,String> readHash(String ss)
{
  Hashtable<String,String> h=new Hashtable();
  String s=ss+"  ";
  String key="";
  String value="";
  boolean isKey=true;
  char c;
  for (int i=0;i<s.length();i++)
  {
    c=s.charAt(i);
    switch(c) 
    {
    case '\n':
    case '\t':
    case  ' ':  if (key.length()!=0 && value.length()!=0)
                   { h.put(key.toLowerCase(),value);
                     key="";value="";isKey=true;}
               break;
     case '=' : isKey=false;break;
     case '"' :
     case '\'': Object[] o=getString(s,i+1,c);
                if (o==null) return h;
                String result=(String) o[1];
                value+=result;
                if (value.length()==0) value=" ";
                i=((Integer) o[0]).intValue();
                break;
     default: if (isKey) {key+=c;break;}        
               value+=c;break;
    }  
  }  
   return h; 
}

public static Object[] getString(String s,int n,char patt)
{
   char c;
   String result="";
   int size=s.length();
  for (int i=n;i<size;i++)
  {  c=s.charAt(i); 
     if ( (i!=(size-1)) && (c=='\\') && (s.charAt(i+1)== patt) ) 
       { i++;result+=patt;continue;}
     if (c==patt) 
          {
            Object[] o=new Object[] {new Integer(i),result};
            return o;
          }
     result+=c;
  }
  return null;   
}
 
 public static String[] split(String s,String patt)
  {
   
    StringTokenizer st=new StringTokenizer(s,patt);
    String sa[]=new String[st.countTokens()];
    int i=0;
    while(st.hasMoreElements())  sa[i++]=((String) st.nextElement()).trim();
    return sa;
  }
  
public static String strToHex(String s)
{ char c;
  if (s==null) return "";
  StringBuffer sb=new StringBuffer();
  for (int i=0;i<s.length();i++)
  {  c=s.charAt(i);
    sb.append(Integer.toHexString(c));
  }
  return sb.toString();
}

public static String hexToStr(String s)
{   
   StringBuffer sb=new StringBuffer();
    int n=0;
    String s0;
    for (int i=0;i<s.length()/2;i++)                 
    { s0=s.substring(i*2,i*2+2);
      n = Integer.parseInt(s0,16);
      sb.append(new Character((char) n));
    }  
    return sb.toString();}
    
public static String merge(Vector v,String fields)
{ Field field=null;
  String s0=null;
  StringBuffer sb=new StringBuffer();
  String[] fa=split(fields,"|");
  for (int j=0;j<v.size();j++)
  {  Object o=v.get(j);
     Class  oc=o.getClass();
     for (int i=0;i<fa.length;i++)
     { s0="";
       try {field=oc.getField(fa[i]);    
            s0=(String) field.get(o);}
       catch(Exception e) {e.printStackTrace();}     
       sb.append(strToHex(s0));
       if (i!=fa.length-1) sb.append("|");}
     if (j!=v.size()-1) sb.append("^^^"); }
  return sb.toString();
}
   
 public final static String ACTBEAN="actbean";

   
   public static void display(Object source,String fields)
   {   log("source="+source);
   	   String result="";
	   String[] sa=split(fields,"|");         
	   Class sClass=source.getClass();
	   for (int i=0;i<sa.length;i++)
		{ 
		 if (sa[i].trim().length()==0) {log("");continue;}
		 String method="get"+sa[i];
		try { Method m=sClass.getMethod(method,new Class[] {});
			  result = (String) m.invoke(source,new Object[] {}); 
			  log("key="+sa[i]+" result="+result);
			}
		catch(Exception e ) 
		  {	//e.printStackTrace();
		  	log(source+": Can not find method="+method);  } 
		}             
	    log("");	
   }
 
  
  public static String  catchException(String msg)
   { String error="";
	 try { throw new Exception();}
	 catch(Exception e)
	 { StringWriter sw=new StringWriter(); 
	   error="\n"+msg;	
	   e.printStackTrace(new PrintWriter(sw));
	   error+=sw.toString();
	   if (error.length()>1000) error=error.substring(0,1000);
	   return error;
	  }  
   }
 
public static Vector makeVector(String[] sa)
	 { Vector v=new Vector();
	  for (int i=0;i<sa.length;i++)
	  {	LookupList ll=new LookupList();
		ll.value=sa[i];
		ll.desc  =sa[i];
		v.add(ll);}
	   return v; 
	 }



public static Vector makeVector(String s)
       { Vector v=new Vector();
       	 String ss="|"+s;
       	 String[] sa=split(ss,"|"); 
		 for (int i=0;i<sa.length;i++)
		  {
			LookupList ll=new LookupList();
			ll.value=sa[i];
			ll.desc  =sa[i];
			v.add(ll);}
		   return v; 
		 }	     
  
public static String getPrefix(String s,int n,String patt)
	{
	   String result="";
	   String data=s;
	   for (int i=0;i<n;i++)
	   {  int nn=data.indexOf(patt);
		  if (nn<0) return result;
		  result+=data.substring(0,nn+patt.length());
		  data=data.substring(nn+patt.length());}  
	   result=removeLast(result,1);	   
	   return result;   
	}   

	public static boolean isTrue(String s)
	{
	  if (s==null) return false;
	  return s.equalsIgnoreCase("Y") ||
			 s.equalsIgnoreCase("YES") ||
			 s.equalsIgnoreCase("TRUE");}

  
   
   public static void changeBlank(Vector v)
	{  for (int i=0;i<v.size();i++)
			 {  LookupList l=(LookupList) v.get(i);
				if (!l.value.equals("")) continue;
				l.desc="";		   }		 
	}
   
   public static Vector removeIKF(Vector v)
	  {
		   boolean isIn=false;
		   Vector vr=new Vector();
		   for (int i=0;i<v.size();i++)
		   { LookupList ll=(LookupList) v.get(i);
		   	 String ss=ll.value;
		   	 if (ss!=null && ss.length()==2 && ss.charAt(1)=='A') continue;
			 vr.add(ll);
		   }
		   return vr;
	  }
   
   
   public static void copyBean(Object source,Object target,String fields,String sprefix,String tprefix)  {   
		String spfx=sprefix.length()==0? "":sprefix+"_";
		String tpfx=tprefix.length()==0? "":tprefix+"_";
		String result="";
		String[] sa=split(fields,"|");         
		Class sClass=source.getClass();
		Class tClass=target.getClass();
		for (int i=0;i<sa.length;i++) { 
			try { 
				Method m = sClass.getMethod("get"+spfx+sa[i],new Class[] {});
				result = (String) m.invoke(source,new Object[] {});
				
				m=tClass.getMethod("set"+tpfx+sa[i],new Class[] {result.getClass()});              
				m.invoke(target,new Object[] {result});      
			} catch(Exception e) {
					log(catchException("Can not find get Method="+spfx+sa[i]+"set Method="+tpfx+sa[i]));  
			}           
		}  
	} 
	   
  public static void copyBean(Object source,Object target,String fields)
	{  copyBean(source,target,fields,"");}

  public static void copyBean(Object source,Object target,String fields,String sprefix)
	{ copyBean(source,target,fields,sprefix,"");	}

  public static String getSProperty(Object bean,String field) 
   {    return getProperty(bean,field)+"";  }

  public static Object getProperty(Object bean,String field)
  {   String result="";
             Class sClass=bean.getClass();
             String methodName=Util.cap(true,field);    
		 try { Method m=sClass.getMethod(methodName,new Class[] {});
                       result = (String) m.invoke(bean,new Object[] {});
			   return result; }
		   catch(Exception e)	{ e.printStackTrace();return null;  } 
  }

 public static void setProperty(Object bean,String field,String value)
  {   Class sClass=bean.getClass();
      String r="";
      String methodName=Util.cap(false,field); 
      try { Method m=sClass.getMethod(methodName,new Class[] {r.getClass()});
            m.invoke(bean,new Object[] {value});}				   
      catch(Exception e){ } 
 } 
		
  public static String merge(String m, String s)
	{
	   boolean notSub=(s==null) || s.trim().equals("");	
	   String result=(notSub)? m.trim():s.trim();
	   
	   return result;		
	}			
 
  public static String merge(String[] aa)
   { String result="";
   	 for (int i=0;i<aa.length;i++)
   	 {  
   	 	String[] bb=split(aa[i],"|");
   	 	result+=bb[0]+"|";
   	 } 
   	 return removeLast(result,1);
   }
   
  public static String removeLast(String s,int n)
	{ 
	   if (s==null || s.length()==0) return s;
	   return s.substring(0,s.length()-n); 
	}

  public static boolean isOverYearEnd(int yearEnd,String enroll)
  {
  	try{
  	Date today=new Date();
  	int  ienroll =Integer.parseInt(enroll)-1900;
  	int  itoday  =today.getYear();
  	long lyearEnd=today.getTime()+getTimePeriod(yearEnd);
  	long lyenroll=today.getTime()+getTimePeriod(ienroll-itoday);
  	boolean flag=lyenroll>lyearEnd;
  	return flag;
	}catch (Exception e){
		   log("Exception occurred in isOverYearEnd in WebLayoutUtil ");
		   return false;
	   }
  }
 
  public static long getTimePeriod(long year)
   {
	 return 	((long) 365)*((long) 86400)*((long) 1000)*year;	
   }

  
  
  
  
  public static String[] readFromBean(Object bean,String fields)
  {
  	String[] ff=split(fields,"|");
  	String[] result=new String[ff.length];
  	for (int i=0;i<result.length;i++) result[i]=getSProperty(bean,ff[i]);
  	return result;
  }

private static boolean isIn(Vector v,LookupList ll)
{
	for (int i=0;i<v.size();i++)
	{  LookupList l0=(LookupList) v.get(i);
		if (ll.value.equals(l0.value)) return true;
	}
	return false;
}


public static String printHidden(Object bean,String fields)
	{
	  String qq="\"";
	  StringBuffer sb=new StringBuffer();
	  String sa[]=split(fields,"|");
	  for (int i=0;i<sa.length;i++)
		sb.append("<input type="+qq+"hidden"+qq+
				  "name="   +qq+sa[i]+qq+
				  " value=" +qq+getProperty(bean,sa[i])+qq
				  +" >\n");
	  return sb.toString();}

public static Vector addLookUp(Vector v,String keys,String values)
{
	Vector newV = new Vector();
	newV.addAll(v);
	String[] ka=split(keys,"|");
	String[] va=split(values,"|"); 
	for (int i=0;i<ka.length;i++)
	{
		LookupList ll=new LookupList();
		ll.value=ka[i];ll.desc=va[i];
		newV.add(ll);
	}
	return newV;
}

public static String printJSLookup(String name,Vector v)
{
	StringBuffer sb=new StringBuffer();
	sb.append("var "+name+"=new Array();\n");
	for (int i=0;i<v.size();i++)
	{   LookupList ll=(LookupList) v.get(i);
		if (ll.value.length()==0) continue;
		sb.append(name+"['"+ll.value+"']="+"'"+ll.desc+"';\n");}
	return sb+"";
}



public static char findCommand(String table,String pattern)    
 { 
   if (pattern.length()<2) return 0;
   int index=table.indexOf(pattern);
   if (index==-1) return 0;
   index-=2;
   return table.charAt(index);
 }

 public static String[] YesOrNo(Object bean,String s)
 {	
	 String[] aa=split(s,"|");
	 String[] res=new String[aa.length];
	 for (int i=0;i<aa.length;i++) res[i]=YesOrNo(getSProperty(bean,aa[i]));
	 return res;
 }
 
public static String YesOrNo(String s)
{
	
	String result=(s==null || s.equals("") || s.equalsIgnoreCase("n") ||
	               s.equalsIgnoreCase("false"))? "No":"Yes";
	return result;                 
}



public static String getDesc(Vector v,String key)
{ 
	for (int i=0;i<v.size();i++)
	{   LookupList ll=(LookupList) v.get(i);
		if (ll.value.equals(key)) return ll.desc;}
	return "";}


static PrintWriter pw=null;
public static void logx(String msg)
{ 	 if (pw==null) 
		 {	try {pw=new PrintWriter(new FileOutputStream("C:\\test.log",false));}
			 catch(Exception e) {}  	}
		 pw.println(msg); 	
		 pw.flush();	
	   }
public static void log(String msg)
{ 	//NALogger.log(msg);
}



public static String catchErr(String msg)
  { StringBuffer sb=new StringBuffer();
	sb.append("ERROR message="+msg+"\n");
	try { throw new Exception();}
	catch(Exception e)
	{ StringWriter sw=new StringWriter();
	  e.printStackTrace(new PrintWriter(sw));
	  sb.append(sw.toString());
	  return sb+"";
	 }  
  }



public static String generateOption(String name,Vector  v)
{  StringBuffer sb=new StringBuffer();
   sb.append("var "+name+"=[");
   for (int i=0;i<v.size();i++)
   {  LookupList ll=(LookupList) v.get(i);
      sb.append("['"+ll.value+"','"+ll.desc+"'],\n");   		 
   }
   return removeLast(sb+"",2)+"];";
}


public static String convertCRToS(String s)
{
	StringBuffer sb=new StringBuffer(); 
	for (int i=0;i<s.length();i++)
	{
		char c=s.charAt(i);
		if (c==13 || c=='\t' || c==10) c=' ';
		sb.append(""+c);  
	}
	return sb+"";
}

public static String convertCRToSS(String s)
{
	StringBuffer sb=new StringBuffer(); 
		for (int i=0;i<s.length();i++)
		{
			char c=s.charAt(i);
			switch(c){
			case '\t':sb.append(" ");break;
			case 10  :break;
			case 13  :sb.append("\\n");break;
			default  :sb.append(""+c);break;}  
		}
		return sb+"";
}

public static String convertHStoJSArray(Hashtable ht) 
{
	StringBuffer sb=new StringBuffer();
	Enumeration en=ht.keys();
	sb.append("{\n");
	while(en.hasMoreElements())
	  {  String table=(String) en.nextElement();
		 sb.append(table+":[");
		 String[] re=(String[]) ht.get(table);
		 for (int j=0;j<re.length;j++)
		   {
			String[] aa=split(re[j],"|");
		   	sb.append("'"+aa[0]+"',");
		   }	
		 sb.deleteCharAt(sb.length()-1); 	
		 sb.append("]\n,"); 
	  } 
	sb.deleteCharAt(sb.length()-1);   
	sb.append("}");
	return sb+"";
}






 
 public static String generateAJAX(String method,String fields)
 { return generateAJAX(method,fields,false,false);}
 
 public static String generateAJAX(String method,String fields,boolean shouldAlert,boolean skipchange)
 {   StringBuffer sb=new StringBuffer();
	 String[] ff=split(fields,"|"); 
	 sb.append("<span id='AJAX_ERRORTAG' class='signuperror'></span>\n"); 
	 sb.append("<input type=hidden name='AJAX_OLD_CODE' value='XXXXX'>\n");
	 sb.append("<input type=hidden name='AJAX_ERROR_MSG' value=''>\n");
	 sb.append("<SCRIPT LANGUAGE=Javascript1.2>\n");
	 sb.append("function "+method+"()\n");
	 sb.append("{\n");
	 sb.append("var ff='"+ff[0]+"|AJAX_OLD_CODE|"+ff[1]+"|AJAX_ERROR_MSG|AJAX_ERRORTAG|"+method+"';\n");
	 sb.append("checkAjax(ff,"+shouldAlert+","+skipchange+");\n");    
	 sb.append("}</SCRIPT>\n");
	 return sb+"";}
	
 private static String outString(Vector ov) {
	 Vector v=new Vector();
	 for (int i=0;i<ov.size();i++) {  
		 String s=(String) ov.get(i);
		 if (s.length()==0) continue;
		 v.add(s);
	 }
	 String result=""; 
	 if (v.size()==0) return "";
	 if (v.size()==1) return (String) v.get(0);
	 for (int i=0;i<v.size();i++) { 
		 result+=(String) v.get(i);
		 if (i!=v.size()-1) result+="<BR>";
	 }
	 return result;
 }
 	
	
 public static String printCapitalized( String str ) {
	 str=str.toLowerCase();
	 StringBuffer result = new StringBuffer();
	 char ch;
	 char prevCh;
	 int i;
	 prevCh = '.'; 
	 for ( i = 0;  i < str.length();  i++ ) {
		 ch = str.charAt(i);
		 if ( Character.isLetter(ch)  &&  ! Character.isLetter(prevCh) )
			 result.append(Character.toUpperCase(ch) );
		 else
			 result.append(ch );
		  prevCh = ch;
	 }
	 return result.toString();
}

	

	/* END CODE FOR CATEGORY-STATE BLOCK 10/20/2006*/ 
    

/*
public static boolean isAddAvpProc(HttpSession session,HttpServletRequest req,HttpServletResponse res) throws Exception
	{
		if (session.getValue("ADD_PROF")==null) return false;
		String curr_party = req.getParameter("CURRENT_PARTY");
		CustomerAccountBean actbean=getACTBEAN(session);
		ScreenFlowMgr mgr=getMGR(session);
		actbean.deleteAVP(Integer.parseInt(curr_party),mgr);
		session.removeValue("ADD_PROF");
		res.sendRedirect("MainPage.jsp?PAGENAME=AccountMaintenancePP.jsp");
		return true;
	}
*/
 public static Object deepGetProperty(Object bean,String field,int seqNBR)
 { 	 boolean isPar=false;
 	 int param=seqNBR;
	 String[] sa=split(field,".");
	 Object result=bean;
	 for (int i=0;i<sa.length;i++)
	 {	 isPar=false;
	 	 String key=sa[i];
		 int n =key.indexOf("(");
		 if (n>=0) isPar=true;
		 else   n=key.indexOf("[");
		 if (n>=0 && !isPar) 
		   {
		   	String s= key.substring(n+1,n+2);
		   	param=Integer.parseInt(s);
		   } 
		 key=n>=0? key.substring(0,n):key;
		 result=n<=0? getOProperty(result,key):getOProperty(result,key,param);}
	 return result;}
	
 public static void setProperty(Object bean,String field,Object value)
			  {   Class sClass=bean.getClass();
			  	  String name="set"+field;
				  try { Method m=sClass.getMethod(name,new Class[] {value.getClass()});
						m.invoke(bean,new Object[] {value});}				   
				 catch(Exception e)
				  { printBeanMsg(e,'s',bean,name);}
			  } 	
 		
 public static Object getOProperty(Object bean,String field,int i)
			  {   Object result=new Object();
				  Class sClass=bean.getClass();
				  String name= "get"+field;
				  try { Method m=sClass.getMethod(name,new Class[] {int.class});
							result = m.invoke(bean,new Object[] {new Integer(i)});
							return result; }
				  catch(Exception e) 
				  { throw error(printBeanMsg(e,'g',bean,name));} 
			  }	

public static Object getOProperty(Object bean,String field)
				  {   Object result=new Object();
					  Class sClass=bean.getClass();
					  String name= "get"+field;
					  try { Method m=sClass.getMethod(name,new Class[] {});
								result = m.invoke(bean,new Object[] {});
								return result; }
					  catch(Exception e) 
					  { throw error(printBeanMsg(e,'G',bean,name));} }
					  
				  

			   
public static void beanToWebLayout(String cprefix,
                              String oprefix,
                              String fields,
                              Object o,
                              UILayout   c)
{
	String[][] fieldss=getFields(fields); 
	String[]   cfields=fieldss[0];
	String[]   ofields=fieldss[1];
	for (int i=0;i<cfields.length;i++)
	{   String s=getSProperty(o,oprefix+"_"+ofields[i]);
		c.setValue(cprefix+"_"+cfields[i],s);	}
	                              			   
}
 
 private static String[][] getFields(String s) 
 {
 	String[] sa=split(s,"|");
 	String[] first=new String[sa.length];
 	String[] second=new String[sa.length];
 	for (int i=0;i<sa.length;i++)
 	{   String[] bb=split(sa[i],"^");
 		first[i]=bb[0];
 		second[i]=bb.length==2? bb[1]:bb[0];}
 	return new String[][] {first,second};
  }
 
 public static String printBeanMsg(Exception e,char type,Object bean,String name)
 {  StringWriter sw=new StringWriter(); 
	e.printStackTrace(new PrintWriter(sw));
	String et="";
	switch(type)
 	{case 'G':et="Get Method";break;
 	 case 'g':et="Get Method with arg of int";break;
 	 case 's':et="Set Method";break;}
	return "Can not Find Method name="+name+
           "<BR>bean_Class="+bean.getClass()+
           "<BR>type="+et+
           "<BR>"+sw.toString().substring(0,400);
 }
 
 public static PrintWriter getPrinter(String output)
 {
   try {
	   FileOutputStream fos=new FileOutputStream(output);
	   PrintWriter pw=new PrintWriter(fos);
	   return pw;}
   catch(Exception e) {e.printStackTrace();} 
   return null;  }
   
public static String  capFirst(String s)
{
	  String re=s.trim();
	  if (re.length()==0) return re;
	  if (re.length()==1) return re.toUpperCase();
	  return re.substring(0,1).toUpperCase()+re.substring(1);
}
	
public static String getFileName(String s)
	  { int n=s.lastIndexOf(".");
		if (n<0) return s;
		else return s.substring(0,n);  }
    
public static String readFile(String input) 
	  { try
	  { StringBuffer sb=new StringBuffer();
		DataInputStream dis=new DataInputStream(new FileInputStream(input));
		while(true)
		  { String line=dis.readLine();
			if (line==null) break;
			sb.append(line+"\n"); }
		return ""+sb;}
	  catch(Exception e) {e.printStackTrace();}
	  return "";}	
    
public static String replace(String s,String[] sa)
	 {  int on,nn;
		String result=s;
		String sub=null;
		while(true)
		{ on=result.indexOf("###");
		  if (on==-1) break;  
		  nn=result.charAt(on+3)-'0';
		  sub=sa.length<(nn+1)? "":sa[nn];
		  result=result.substring(0,on)+sub+result.substring(on+4);}
		return result;}
	

public static void printLayout(UILayout c,Object[] obja)
{	for (int i=0;i<obja.length;i++)
	{   ObjPrifix opf=(ObjPrifix) obja[i];
		c.loadObject(opf.obj,opf.getPrefix());	  
	}
}
	
public static void execMethod(Object bean,String name,Object[] obja)
{    Class  sClass=bean.getClass();
	 Class[]    ca=new Class[obja.length];
	 for (int i=0;i<ca.length;i++) ca[i]=obja[i].getClass();
	 try { Method m=sClass.getMethod("set"+name,ca);
		   m.invoke(bean,obja);}				   
	 catch(Exception e)
	  { 
	  	throw error("Can not find 'set"+name +"' method <BR>"+"bean Name="+bean.getClass().getName()); 
	  }}

public static RuntimeException error(String msg)
{return new RuntimeException("WebLayoutUTIL:"+msg);}

public static Vector makeYear(int n)
{   String result="";
	Date date=new Date();
	int year=date.getYear()+1900;
	for (int i=0;i<n;i++)
	 result+=(year--)+"|";
	return makeVector("| |"+result);}
	
public static String  makeSYear(int n)
	{   String result="";
		Date date=new Date();
		int year=date.getYear()+1900;
		for (int i=0;i<n;i++)
		 result+=(year--)+"_";
		 return "_"+removeLast(result,1);}			

public static String hexMerge(String s)
{   String result="";
	String[] bb=split(s,"^^^");
	for (int i=0;i<bb.length;i++)
	{ String[] aa=split(bb[i],"|");
	  for (int j=0;j<aa.length;j++)
		 result+=strToHex(aa[j])+"|";
	  result=removeLast(result,1);    
	  result+="^^^"; }
	result=removeLast(result,3);
	return result; }
	

} // WebLayoutUtii class End Here

class ObjPrifix
{   Object obj;
	String prefix="";
	ObjPrifix(Object obj,String[] aa)
	{  this.obj=obj;
	   if (aa.length==3) prefix=aa[2];}
	String getPrefix()
	{ if (prefix.length()==0) return "";
	  else return prefix+"_";}
}





