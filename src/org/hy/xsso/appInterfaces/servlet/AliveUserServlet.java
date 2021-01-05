package org.hy.xsso.appInterfaces.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.net.data.Communication;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.xsso.common.BaseResponse;
import org.hy.xsso.common.BaseServlet;
import org.hy.xsso.net.AliveListener;





/**
 * 单点登录第四步：保活用户。 
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-01-04
 * @version     v1.0
 */
public class AliveUserServlet extends BaseServlet
{
    
    private static final long serialVersionUID = 3708848763509595148L;
    
    private static final Logger $Logger = new Logger(AliveUserServlet.class);
    
    
    
    public void doGet(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        i_Response.setCharacterEncoding("UTF-8");
        i_Response.setContentType("application/json");
        
        BaseResponse v_ResponseData = new BaseResponse();
        
        try
        {
            String v_USID = i_Request.getParameter("USID");
            if ( Help.isNull(v_USID) )
            {
                v_ResponseData.setCode("40011");
                v_ResponseData.setMessage("编码无效或已过期！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            Communication v_UserData = (Communication)XJava.getObject(v_USID);
            if ( v_UserData == null || v_UserData.getData() == null )
            {
                v_ResponseData.setCode("40011");
                v_ResponseData.setMessage("编码无效或已过期！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            AliveListener.alive(v_UserData.getDataXID() ,v_UserData.getData() ,v_UserData.getDataExpireTimeLen());
            
            v_ResponseData.setCode($Succeed);
            v_ResponseData.setMessage("正确");
            i_Response.getWriter().println(this.toReturn(v_ResponseData));
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            i_Response.getWriter().println(StringHelp.replaceAll("{'code':'40001' ,'message':'" + exce.getMessage() + "'}" ,"'" ,"\""));
        }
    }
    
    
    
    public void doPost(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        // 只支持Get请求
        // Nothing.
    }
    
}
