
<%@ page   import="com.jm.weblayout.*,com.jm.model.*" %>
<%@ page   import="com.jm.ajax.*,com.jm.service.*" %>
<%@ page   import="com.jm.webutil.*,java.util.*" %>
<%@ taglib uri="/WEB-INF/weblayout.tld"  prefix="wbl" %>
<%
   UserService userService=UserServiceImpl.getService();
   Map<String,String> table=userService.usersroles(); 
   int count=0;
   
%>
<wbl:create id="showUsersForm">
 <table  cellpadding="2"  width="400">
   <map:title param='User Information|7' />
   <array>
    <context button='onclick=showSummit({$name_qq}) onmouseout=setClass(this,false) onmouseover=setClass(this,true) class=btn'
             row ="td.width=2%|8%|22%|35%|20%|4%|4%" 
             link="onmouseout='setLink(this,false)' onmouseover='setLink(this,true)' class='steplink' href='javascript:doLink({$name_qq},{$value_qq})'"
     />
   <row># #|Index|User Id|Roles|Edit|Delete|# #</row>
 <%
 
 for (String login:table.keySet())
 {
     count++;
     String role= table.get(login);
     if (count>=6) break;
 %>
   <row># #|<%=count%>.|<%=login%>|<%=role%><link name=<%=login%>  value='modify' />
              <checkbox name="check<%=count%>" value=<%=login%> /># #</row>
 <%}%>             
    <row td.colspan="7">#  #</row>          
    <row><array td="colspan=7 align=center"><button name="delete" value="Delete"  />#      #<button name="clear" value="Clear All"  />
   #     #<button name="setAll" value="Select All"  />
   </array></row>           
   </array>
 </table>
</wbl:create>
<%=showUsersForm%>
<script>
function doLink(name,value)
{
   myForm.myData.value=name;
   actionSubmit(value);
}
var totalCount=<%=count%>;
function showSummit(name)
{
   switch (name)
   {
    case 'delete': if (confirm("Are you sure about deleting checked users? "))
                     actionSubmit(name); 
                   break; 
    case 'clear':
            for (var i=1; i<=totalCount; i++)
            myForm["check"+i].checked= false;
            break;
    case 'setAll': 
            for (var i=1; i<=totalCount; i++)
            myForm["check"+i].checked= true;
            break;

   }
}


</script>



  
  
 