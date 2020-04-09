<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    <title>应用服务</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	
	<script type="text/javascript"> 
	
	function postGoto(i_URL ,i_ParamNames ,i_ParamValues) 
    {
        var v_TempForm = document.createElement("form");
        v_TempForm.action        = i_URL;
        v_TempForm.target        = "_self";
        v_TempForm.method        = "post";
        v_TempForm.style.display = "none";
        
        for (var v_Index in i_ParamNames) 
        {
            var v_Hidden = document.createElement("input");
            v_Hidden.type  = "hidden";
            v_Hidden.id    = i_ParamNames[v_Index];
            v_Hidden.name  = i_ParamNames[v_Index];
            v_Hidden.value = i_ParamValues[v_Index];
            v_TempForm.appendChild(v_Hidden);
        }
        
        document.body.appendChild(v_TempForm);
        v_TempForm.submit();
    }
    
    
	
	var USID = "";
	
    function getUSID(i_USID)
    {
    	if ( USID == "" && i_USID != null && i_USID != undefined && i_USID != "" )
   		{
    		USID = i_USID;
    		postGoto("${ctx}/02.登陆页面.login.do" ,["USID"] ,[i_USID]);
   		}
    	else
   		{
    		window.location.href = "02.登陆页面.login.do";
   		}
    }
    
	</script>  
	
  </head>
  
  <body>
  	
  	<!-- 建议放在整个页面的最后的位置 -->
	<script type="text/javascript" src="https://${SSOServersHttp!}/XSSO/sso?SSOCallBack=getUSID&r=${.now?string["yyyyMMddhhmmSSsss"]}"></script>

  </body>
</html>
