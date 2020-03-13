
<%@ page   import="com.jm.weblayout.*,com.jm.model.*" %>
<%@ page   import="com.jm.ajax.*" %>
<%@ page   import="com.jm.webutil.*" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>

<wbl:create id="pomolist">
<context button='onclick=doAction({$value_qq})   onmouseout=setClass(this,false) onmouseover=setClass(this,true) class=btn'  />
  <table      cellpadding="2" width="300">
  <map:title param='Pomodoro Processing|2' />
   <array>
   <context row='td.width=30%|70% td.align=right|left' />
   <row  >Name:       <text name=name value="My LOGO"/></row>
   <row  >Deadline:   <text name=deadline value="Sept 23 2015"/></row>
   <row  >Pomodos:    <number name=pomodos /></row>
   <row td.height=5># #|# #</row>
   <row>   

      # #
      <array><button value='Del Last'/> #  #<button value='Del First'/>#  #<button value='Add'/></array>
     <row td.height=5># #|# #</row>
   </row>
   </array>
 </table>
</wbl:create>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>My Angular 2 Pomodoro Tasks</title>
    <script src="node_modules/core-js/client/shim.min.js"></script>
    <script src="node_modules/zone.js/dist/zone.js"></script>
    <script src="node_modules/reflect-metadata/Reflect.js"></script>
    <script src="node_modules/systemjs/dist/system.js"></script>
    <script src="node_modules/rxjs/bundles/Rx.js"></script>
    <script src="js/systemjs.config.js"></script>
    <script type="text/javascript" src="js/jm.js"></script>
    <script src="js/jquery-1.11.3.min.js"></script>
    <script>
      System.import ('pomolist/pomodoro-tasks.js')
        .then(null, console.error.bind(console));
    </script>

    <link rel="stylesheet" href="node_modules/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="js/cbp.css">
  </head>
  <body>
     <table width=1000>
     <tr><td width=200></td><td width="300"><table border="2"><tr><td>
     <p>This is the Angular 2 DataGrid demo that allow external 
     HTML elements such use Pomodoro table to remove and add the
     object inside Angular 2 DataGrid by Use the JQuery<br>
     <strong>Example:Fill the Form and Click Add button and New Record
     will be add</strong><br>
     <a href="index.jsp">Back to Main Page</a></td></tr></table>
     </p></td>
     <td width="100"></td>
     <td>
     <%=pomolist%>
     <td><tr></table>
    <pomodoro-tasks></pomodoro-tasks>
  </body>
</html>
<script>

  function doAction(s)
  {
    switch(s) 
     {
      case 'Del Last' :window.pomolist.call("","L");break;
      case 'Del First':window.pomolist.call("","F");break;
      case 'Add':doAdd();break;
     }
  }

  function doAdd() 
  {
    var obj={
               name: $("#name").val(),
           deadline: $("#deadline").val(),
           pomodorosRequired: $("#pomodos").val()
         }
    window.pomolist.call(obj,"A");
  }
</script>
