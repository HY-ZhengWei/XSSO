/**
 * 回调方法。当有多个单点验证服务器时，此方法应执行多次。
 * 
 * @param i_SSOServer  单点验证服务器的HostName及端口，如,192.168.1.1:80
 * @param i_USID       票据号
 */
function ssoLogin(i_SSOServer ,i_USID)
{
	if ( i_SSOServer != null && i_SSOServer != undefined && i_SSOServer != "" )
	{
		if ( i_USID != null && i_USID != undefined && i_USID != "" )
		{
			$.ajax(  
		    {
		        type     : 'get',  
		        url      : 'https://' + i_SSOServer + '/XSSO/sso?USID=' + i_USID,  
		        dataType : 'jsonp',  
		        jsonp    : "jsoncallback",  
		        success  : function(data) {},  
		        error    : function() {}  
		    });
		}
	}
}