<%@ page   import="com.jm.servlet.*" %>
<%@ page   import="com.jm.webutil.*" %>
<%@ page   autoFlush="false" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<jsp:useBean id="user" class="com.jm.model.User"  scope="session"/>
<%
 if (!user.isLogin()) {
 %>
 <jsp:forward page="login.jsp" />
<%
}

 String jspPage=request.getParameter(NC.PAGE);
 String action =request.getParameter(NC.ACTION);
 System.out.println("JspPage="+jspPage+" action="+action);
 user.setPage(jspPage);
 user.setAction(action);
 String   uri=jspPage==null? "Welcome.jsp":Dispatch.getJsp(request);
          uri=uri==null? jspPage:uri;
%>
<wbl:create id="menu" >
  <menu name='menu' pos='100'>
    
    Security^Add User,addUser.jsp^Show Users,showUsers.jsp^
    Change Password,changePwd.jsp^
    Add  Address,addAddress.jsp^
    Edit Parameters,editParam.jsp^
    RestFul Console,restConsole.jsp^
    Welcome,Welcome.jsp|
    Business^Order,*order.jsp|Save,save
  </menu>
</wbl:create>
<html>
<script type='text/javascript' src="js/coolmenu.js"></script>
<script type='text/javascript' src='js/prototype.js'></script>
<script type='text/javascript' src='js/autoassist.js'></script>
<link rel="stylesheet" type="text/css" href="js/cbp.css">
<body onload='doOnLoad()'>
 <%=menu%>
<br> 
<table width="600" height="600">
 <tr><td class='signuperror'  height="20"><div id='error' ></div></td></tr>
 <tr >
  <td align="middle"   valign="center">
  <form name='main'   action="main.jsp" method="POST">
   <%
      System.out.println(uri);
    %>
       <jsp:include page="<%=uri%>"  />
    <%
       String target=user.getPage();
              target=target==null? jspPage:target;
    %>
     <input type="hidden" name="jspPage"   value="<%=target%>" >
     <input type="hidden" name="myAction"  value="" >
     <input type="hidden" name="myData"    value="" >
     <input type="hidden" name="myData1"   value="" >
  </form>
  </td>
  </tr>
</table> 
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
 
 function doSubmit()
  {   doUrl(jspPage.value);  }
 function doUrl(url)
 {
   if (url.charAt(0) == "*")  
   { 
     url= url.substring(1);
     document.location = url; 
     return;
    }
   jspPage.value=url;
   myForm.submit();
 }
 
 function actionSubmit(s)
  {
     myForm.myAction.value=s;
     doSubmit();
  }
  
 function mainCheck()
 {
   alert("ok");
 }
</script>


