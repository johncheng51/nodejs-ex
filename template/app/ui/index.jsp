
<%@ page   import="com.jm.weblayout.*,com.jm.model.*" %>
<%@ page   import="com.jm.ajax.*" %>
<%@ page   import="com.jm.webutil.*" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<wbl:create id="index">
 <map:title param='Available Application Demo|2' />
  <array>
   <context row='td.valign=top|top td.width=40%|60%' />
   <row ><link href="/ui/index.html" value='Angular 2.0 Timer'/>
     [Simple Angular 2.0 application. <br> <strong>Hit "Start" to begin timer count.</strong>]
  </row>
   <row ><link href="/ui/pomodo.html" value='Angular 2.0 Counter'/>
     [ Simple Angular 2.0 application. <br> Hit "Reset" to stop timer count. ]
  </row>
   <row ><link href="/ui/hello.html" value='Angular 2.0 Hello'/>
      Use outside button with jQuery to control the Augular 2.0 components.
  </row>
   <row ><link href="/ui/pomolist.jsp" value='Angular 2.0 DataGrid'/>
      Use outside form with jQuery to control the Augular 2.0 components.
  </row>
  <row ><link href="/ui/podores.html" value='Full Angular 2.0 Component'/>
      Augular 2.0 with more tree structure layout Please review the source file through Debug
  </row>
  <row ><link href="/ui/shoeDemo.html" value='shoeDemo'/>
      Demo using jQuery and Ajax with RestFul service to pick shoe from the drop down list.
  </row>
  <row><link href="/ui/restConsole.jsp" value='restConsole'/>
      [Use a RestFul Service to explore all available services and functions. For example, select "lookup" from services list, type "lookup" in arg1, then click execute button to get the results of "URL PATH" and "REPORT".]
  </row>
  <row> <link href="/ui/main.jsp"  value='Main Page'/>
      Web Application of Login Demo. Please login with john/john. 
  </row>
  <row> <link href="/ui/tables.jsp" value='Edit DataBase Table'/>
       Edit DataBase Table. 
  </row>
  <row> <link href="/ui/search.jsp" value='Search the Files' />
       Search Environmental Files. 
  </row>
  <row> <link href="/ui/test.jsp" value='Get the Environmental Variables' />
       Show All Environmental Variables.
  </row>
  </array>
</wbl:create>

<link rel="stylesheet" type="text/css" href="js/cbp.css">

<!DOCTYPE html>
<html>
<head>
	<title>John Cheng Full Stack Java Web App</title>
</head>
<body>
   <h1> John Cheng Full Stack Java Web App</h1>
    <table width="700" height='400'>
      <tr><td valign="middle" align="center">
          <table border="2"  width="600"  height='300'><%=index%></table></td></tr>
    </table>  
  
</body>
</html>
