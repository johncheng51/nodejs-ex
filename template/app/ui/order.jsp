<%@ page   import="com.jm.servlet.*" %>
<%@ page   import="com.jm.webutil.*" %>
<%@ page   autoFlush="false" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<jsp:useBean id="user" class="com.jm.model.User"  scope="session"/>
<jsp:useBean id="navigator" class="com.jm.servlet.Navigator" scope="session"/>
<%
  
   navigator.setPage(request);
   String target  = navigator.getPage();
  
%>

<wbl:create id="orderForm" >

 <table width='500' >
  <map:title param='Foreign Tire Order System|4' />
  <context button='onclick=userSubmit({$name_qq}) onmouseout=setClass(this,false) onmouseover=setClass(this,true) class=btn'  />
  <row td.colspan=4 td.align=center><div name='error' class='signuperror' /></row>
  rule4
  <row td.colspan=4 td.align=center>
 
  <puretext>      
   <jsp:include  page="<%=target%>" />
  </puretext>
  </row>
  rule4
  <row td.width='25%|25%|25%|25%' td.align='center|center|center|center'>
       <array><button skip='<%=navigator.isFirst()%>' name='Previous' value='Previous'/></array>
       <array><button name='Cancel' value='Cancel'/></array>
       <array><button name='Submit' value='Submit' skip='<%=!navigator.isLast()%>' /></array> 
       <array><button skip='<%=navigator.isLast()%>' name='Next' value='Next'/></array> 
  </row>
  line4
 </table>
</wbl:create>
<html>

<script type='text/javascript' src="js/coolmenu.js"></script>
<script type='text/javascript' src='js/prototype.js'></script>
<script type='text/javascript' src='js/autoassist.js'></script>
<link rel="stylesheet" type="text/css" href="js/cbp.css">
<body onload="doOnLoad()">
<form name='main'   action="order.jsp" method="POST">
<%=orderForm%>
     <input type="hidden" name="jspPage"   value="<%=target%>" >
     <input type="hidden" name="myAction"  value="" >
     <input type="hidden" name="myData"    value="" >
     <input type="hidden" name="myData1"   value="" >

</form>
</body>
</html>

<script>

 var myForm;
 var jspPage;
 function doOnLoad()
 {
   myForm =document.main;
   jspPage=myForm.jspPage;
   checkError();
 }

function checkError()
 {
   var msg='<%=user.getError()%>';
   error.innerHTML=msg;
   var count=-1;
   var pe=new PeriodicalExecuter(function()
       { count++;
         if (count==0) return; 
         error.innerHTML="";  }, 4);    
 }
 
function userSubmit(value)
{
 switch(value)
 {    case 'Cancel': if (confirm("Are you sure to cancel this order? "))
                     document.location ='main.jsp';
                     break; 
      default      : myForm.myAction.value=value;
                     myForm.submit();             
 }
}
</script>



