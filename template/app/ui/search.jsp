<%@ page   import="com.jm.webutil.*" %>
<%@ page   import="java.io.*" %>
<html>
<link rel="stylesheet" type="text/css" href="js/cbp.css">
<H1>RED HAT CLOUD ENV TOOLS</H1>
<table border="2" width="300" >
<form   action="search.jsp">
	<tr><td> Input the Data </td>
	    <td><input type="text" name="search"/></td></tr>
	    <tr><td><input type="radio" name='type' value="ENV"/>ENV</td>
	    <td><input type="radio" name='type' value="PROP"/>PROP</td></tr>
	<tr align="center" ><td colspan="2"><input type="submit" name="submit" value="Submit"/></td></tr>
</form>
</table>

<%
    String search=request.getParameter("search");
    String type  =request.getParameter("type");
    type=(type==null)? "":type;
    System.out.println("search="+search+" type="+type);
    if (search==null) return;
    String result=WebSession.printFiles(search,type);
%>
<%=result%>
</html>