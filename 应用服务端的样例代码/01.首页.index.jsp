<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.hy.common.Help" %>
<%@ page import="org.hy.common.StringHelp" %>
<%@ page import="org.hy.common.xml.XJava" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    <title>应用服务</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	
	<script type="text/javascript"> 
	
	var USID = "";
	
	
	
    function getUSID(i_USID)
    {
    	if ( USID == "" && i_USID != null && i_USID != undefined && i_USID != "" )
   		{
    		USID = i_USID;
    		window.location.href = "02.登陆页面.login.do?USID=" + i_USID;
   		}
    	else
   		{
    		window.location.href = "02.登陆页面.login.do";
   		}
    }
    
	</script>  
	
  </head>
  
  <body>
  	
  	<%
  	String v_ClusterServer = StringHelp.replaceAll(XJava.getParam("SSOServersHttp").getValue() ,new String[]{" " ,"\t" ,"\r" ,"\n"} ,new String[]{""});
  	%>
  	
  	<!-- 建议放在整个页面的最后的位置 -->
	<script type="text/javascript" src="http://<%=v_ClusterServer%>/XSSO/sso?SSOCallBack=getUSID&r=<%= new Date().getTime() %>"></script>

  </body>
</html>
