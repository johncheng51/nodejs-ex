<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<%@ include file="mapTable.jsp" %>

<wbl:create id='orderstart'>
<table  width='100%' >
<map:title param='Start|2'/>
<class:grid  param='2|2'>
<context row='td.width=20%|80% td.align=center|center' />
AAAA|BBB|CCCC|DDDD
</class:grid>
</table>
</wbl:create>
<%=orderstart%>


