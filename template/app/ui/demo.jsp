<%@ page   import="com.jm.weblayout.*,com.jm.model.*" %>
<%@ page   import="com.jm.ajax.*" %>
<%@ page   import="com.jm.webutil.*" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<%
   User user=WebSession.getUser(request);
   boolean modify=user.haveAddrBean();
   String title= modify? "Modify Address":"Add Address";
   String button = modify? "Modify":"Add";
%>

<wbl:create id="addressForm">
 <context button='onclick=userSubmit({$name_qq}) onmouseout=setClass(this,false) onmouseover=setClass(this,true) class=btn' 
          text='class=textbox' password='class=textbox' />
 <table      cellpadding="2" width="300">
   <map:title param='<%=title%>|2' />
   <row  >Street:  <text name=street /></row>
   <row  >City:    <text name=city  /></row>
   <row  >State:   <text name=state /></row>
   <row  >Zip Code: <text name=zipcode /></row> 
   <row  >Date :    <date  name=date /></row>     
   <row  td.align='center' td.colspan='2'>
   <array >#  #<button name='<%=button%>' value='<%=button%>'/></array></row>
   <row  td.align='center' td.colspan='2'><div td='class=signuperror' name=message /></row>
 </table>
</wbl:create>
<%
  addressForm.loadObject(user.getFormBean(),"");
%>
<%=addressForm%>
<script>

function userSubmit(s)
{   
  actionSubmit(s);
}
</script>