<%@ page   import="com.jm.weblayout.*,com.jm.model.*,java.util.*" %>
<%@ page   import="com.jm.ajax.*,com.jm.service.*" %>
<%@ page   import="com.jm.webutil.*,java.util.*" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>

<wbl:create id="editParamForm">
<context button='onclick=paramSubmit({$name_qq}) onmouseout=setClass(this,false) onmouseover=setClass(this,true) class=btn' 
 link="onmouseout='setLink(this,false)' onmouseover='setLink(this,true)' class='steplink' "
     />
 <table  cellpadding="2"  width="500" height="380">
   <map:title param='Edit Param|2' />
   <row td.width='28%|75%' td.align='center|center' td.valign='top|top'>
   <table>
   <row> <select class='signuptext' multiple="true" name="types"  size="21"  onchange="doSelect(this)" /></row>
   <row><text name="textInput" size='15' /></row>
   </table>
   <textarea name='textarea' cols="40"  rows="20"/></row>
   <row ><array td='colspan=2 align=center' >
             #   #<button name="add" value="Add" />
             #   #<button name="save" value="Save"/> 
             #   #<button name="remove" value="Remove" />
             #   #<button name="upload" value="UpLoad" />
             #   #<link name="download" value="DownLoad" href='javascript:doLink()'/></array></row>
  </table>
</wbl:create>
<%
   ParamService param=ParamServiceImpl.getService();
   Vector v=WUtil.makeVector(param.getAllTypes());
   editParamForm.setData("types",v);
   User user=WebSession.getUser(request);
   editParamForm.setValue("textarea",user.getData1());
   String textInput=request.getParameter("textInput");
   textInput=textInput==null? "":textInput;
    editParamForm.setValue("textInput",textInput);
%>
<%=editParamForm%>

<script>
function doSelect(select)
{
   myForm.myData.value   =select.value;
   myForm.textInput.value=select.value;
   actionSubmit("select")
}
function paramSubmit(com)
{
  if (com=='upload')  
     {
      showAddr("upload.jsp");
      return;
     }  
  myForm.myData.value=myForm.textInput.value;
  myForm.myData1.value=myForm.textarea.value;
  actionSubmit(com);
}
function doLink()
{
   var text=myForm.textInput.value;
   var url="download.jsp?myData="+text;
   document.location = url;
}
</script>



  
  
 