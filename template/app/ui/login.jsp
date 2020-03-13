<%@ page   import="com.jm.weblayout.*" %>
<%@ page   import="com.jm.ajax.*" %>
<%@ page   import="com.jm.webutil.*" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<%
   
    AjaxManager am=AjaxManager.getInstance();
    AjaxService[] asa=new AjaxService[]{ new AutoAssist("user^user"),
                                         new AjaxContend("select^user","role"),
                                         new Validate("login^user^select_role^password")};
    am.setService(asa,request);
    boolean ok=am.isAjax(out);
    if (ok) return;
    
%>

<wbl:create id="login">
 <context button='onclick=validatelogin()  onmouseout=setClass(this,false) onmouseover=setClass(this,true) class=btn' 
          text='class=textbox' password='class=textbox'      />
  <table      cellpadding="2" width="300">
   <map:title param='Security Login|2' />
   <array>
   <context row='td.width=30%|70% td.align=right|left' />
   <row  >User Name:  <text name=user  onblur='loadselect()'/></row>
   <row  >Password:   <password name=password /></row>
   <row  >Role:       <array><div name=select><select name=role /></div></array></row>
    </array>
   <row  td.align='center' td.colspan='2'><array ><button name='send' value='Submit'/></array></row>
   <row  td.align='center' td.colspan='2'><div td='class=signuperror' name=message /></row>
 </table>
 

</wbl:create>
<script type='text/javascript' src='js/prototype.js'></script>
<script type='text/javascript' src='js/autoassist.js'></script>
<link rel="stylesheet" type="text/css" href="js/cbp.css">
<html>
<body>
<div id="login"></div>
<form name="login" action="main.jsp"  method="POST" >
 <table  width="800" height="500">
 <tr><td valign="middle" align="center">
<%=login%>
<td></tr>
</table>
</form>
</body>
</html>
<%=am%>