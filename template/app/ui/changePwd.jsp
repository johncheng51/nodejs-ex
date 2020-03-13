<%@ page   import="com.jm.weblayout.*,com.jm.model.*" %>
<%@ page   import="com.jm.ajax.*" %>
<%@ page   import="com.jm.webutil.*" %>
<%@ page   errorPage="error.jsp"     %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<wbl:create id="changePwdForm">
 <table      cellpadding="2" width="300">
 <context button=' onmouseout=setClass(this,false) onmouseover=setClass(this,true) class=btn'   />
   <map:title param='Change Password|2' />
   <row  >New Password:   <password name=password /></row>
   <row  >Comfirm:        <password name=comfirm  /></row>
   <row  ><array td='colspan=2 align=center'><button value="Submit"  onclick='actionSubmit("change")'/>
                 <button value="Cancel"  onclick='actionSubmit("cancel")'/></array></row>
  </table>
</wbl:create>
 <%
    User user=WebSession.getUser(request);
    boolean submitok=!user.isFromMain() && user.isProcessOk();
    if (submitok) out.println("<H3> Change Password OK </H3>");
    else out.println(changePwdForm);
  %>





  
  
 