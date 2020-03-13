<%@ page   import="com.jm.weblayout.*,com.jm.model.*" %>
<%@ page   import="com.jm.ajax.*" %>
<%@ page   import="com.jm.webutil.*" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<%
   User user=WebSession.getUser(request);
   String title="RestFul API Tools";
  
%>
<wbl:create id="addressForm">
 <context button='onclick=userSubmit({$value_qq}) onmouseout=setClass(this,false) onmouseover=setClass(this,true) class=btn' 
          text='class=textbox size=30' password='class=textbox' />
 <table      cellpadding="2" width="700" >
   <map:title param='<%=title%>|2' />
    line2
   <row  >Services:<array><select name=service data="@services" />
         # #<button value="Execute"/># #<button value="Load"/>
         # #<button value="Post"/># #<button value="Clear"/></array></row>
   <row  >Method:<select name=method /></row>
   <row  >Argument 1:<text name="arg1"/></row>
   <row  >Argument 2:<text name="arg2"/></row>
   <row  >Argument 3:<text name="arg3"/></row>
   <row  >Parameter :<textarea name="param" rows=3 cols='40' value=""/></row>
    line2 
   <map:title param='Report Data|2' />  
    line2 
   <row  >URL PATH:<textarea name="result" rows=3 cols='60' value=""/></row>
   <row  >REPORT:
   <textarea  rows=8 cols='60' name='report' value=" "/></row>
   <row  >POST INPUT:
   <textarea  rows=8 cols='60' name='input' value=" "/></row>
   line2
 </table>
</wbl:create>
<%=addressForm%>
<a id=white href="index.jsp">Back to Main Page</a>
<script src="js/jquery-1.11.3.min.js"></script>
<script src="js/jm.js"></script>
<link rel="stylesheet" type="text/css" href="js/cbp.css">
<script>
var jm=new jm_micro();
$('#service').change(fc);
function fc(event)
         {    
           var val=$(event.target).val();
           var rest='rest/lookup/option/service.'+val;
           $('#method').load(rest);
         } 


function userSubmit(s)
{   
	switch(s) 
	 {
       case 'Execute': var url='rest/'+$('#service').val()+"/"+$('#method').val()
                       +"/"+$("#arg1").val()+"?"+$('#param').val();
                       $('#result').val(url);
                       jm.load(url,'report');
                       break;
        case 'Load':   jm.load($('#result').val(),'report');
                       break;
       case 'Clear':   jm.clear('report');
                       jm.clear('result');
                       jm.clear('input');
                       break;
       case 'Post':    post($('#result').val(),$('#input').val());
    }
}
  
function post(url,text) 
{
  
   $.ajax(url,
   {
     data: text, 
     type: 'POST',
     processData: false,
     contentType: 'application/json',
     dataType: "json",
  });

}


  
  

</script>

