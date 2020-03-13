<%@ page   import="com.jm.ajax.*,com.jm.service.*" %>
<%@ page contentType="plain/text;test.txt"  %>
<%@ page    %>
<%
  String type=request.getParameter("myData");
  ParamService param=ParamServiceImpl.getService();
  out.clear();
  response.setHeader("Content-Disposition", "attachment; filename=\""+type+".txt");
  out.print(param.getParamStr(type));
%>

