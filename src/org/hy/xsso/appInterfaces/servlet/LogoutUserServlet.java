package org.hy.xsso.appInterfaces.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.log.Logger;
import org.hy.xsso.common.BaseResponse;
import org.hy.xsso.common.BaseServlet;
import org.hy.xsso.net.LogoutListener;





/**
 * 单点登录第五步：用户退出。 
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-01-05
 * @version     v1.0
 */
public class LogoutUserServlet extends BaseServlet
{
    
    private static final long serialVersionUID = 3708848763509595148L;
    
    private static final Logger $Logger = new Logger(LogoutUserServlet.class);
    
    
    
    public void doGet(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        i_Response.setCharacterEncoding("UTF-8");
        i_Response.setContentType("application/json");
        
        BaseResponse v_ResponseData = new BaseResponse();
        
        try
        {
            String v_USID  = i_Request.getParameter("USID");
            String v_Token = i_Request.getParameter("token");
            
            if ( Help.isNull(v_Token) )
            {
                v_ResponseData.setCode("50011");
                v_ResponseData.setMessage("请求票据无效！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            String v_AppKey = GetAccessTokenServlet.getAppKey(v_Token);
            if ( Help.isNull(v_AppKey) )
            {
                v_ResponseData.setCode("50011");
                v_ResponseData.setMessage("请求票据无效！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            if ( Help.isNull(v_USID) )
            {
                v_ResponseData.setCode("50012");
                v_ResponseData.setMessage("编码无效或已过期！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            LogoutListener.logout(v_USID);
            
            v_ResponseData.setCode($Succeed);
            v_ResponseData.setMessage("成功");
            i_Response.getWriter().println(this.toReturn(v_ResponseData));
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            i_Response.getWriter().println(StringHelp.replaceAll("{'code':'50001' ,'message':'" + exce.getMessage() + "'}" ,"'" ,"\""));
        }
    }
    
    
    
    public void doPost(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        // 只支持Get请求
        // Nothing.
    }
    
}
