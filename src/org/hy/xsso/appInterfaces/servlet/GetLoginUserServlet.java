package org.hy.xsso.appInterfaces.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.license.AES;
import org.hy.common.license.AppKey;
import org.hy.common.net.data.Communication;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.xsso.appInterfaces.servlet.bean.LoginUserResponse;
import org.hy.xsso.appInterfaces.servlet.bean.UserSSO;
import org.hy.xsso.common.BaseServlet;





/**
 * 单点登录第三步：获取已登录的用户信息。 
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-01-04
 * @version     v1.0
 */
public class GetLoginUserServlet extends BaseServlet
{
    
    private static final long serialVersionUID = -127442566330442288L;
    
    private static final Logger $Logger = new Logger(GetLoginUserServlet.class);
    
    /**
     * 所有配置有效的应用AppKey数据
     */
    @SuppressWarnings("unchecked")
    private static Map<String ,AppKey> $AppKeys = (Map<String ,AppKey>)XJava.getObject("AppKeys");
    
    
    
    public void doGet(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        i_Response.setCharacterEncoding("UTF-8");
        i_Response.setContentType("application/json");
        
        LoginUserResponse v_ResponseData = new LoginUserResponse();
        
        try
        {
            String v_Code = i_Request.getParameter("code");
            String v_UCID = i_Request.getParameter("ucid");
            if ( Help.isNull(v_Code) )
            {
                v_ResponseData.setCode("30011");
                v_ResponseData.setMessage("编码无效或已过期！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            UserSSO v_User = GetLoginCodeServlet.getAppKey(v_Code);
            if ( v_User == null )
            {
                v_ResponseData.setCode("30011");
                v_ResponseData.setMessage("编码无效或已过期！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            if ( Help.isNull(v_UCID) )
            {
                v_ResponseData.setCode("30012");
                v_ResponseData.setMessage("会话编码无效！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            AES    v_AES       = new AES(2 ,$AppKeys.get(v_User.getAppKey()).getPrivateKey());
            String v_SID       = "";
            String v_AppKey    = "";
            long   v_Timestamp = 0L;
            try
            {
                v_SID       = v_AES.decrypt(v_UCID);
                v_Timestamp = Long.parseLong(v_SID.split("@")[2]);
                v_AppKey    = v_SID.split("@")[1];
                v_SID       = v_SID.split("@")[0];
            }
            catch (Exception exce)
            {
                v_ResponseData.setCode("30012");
                v_ResponseData.setMessage("会话编码无效！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            /** @Todo 验证AppKey与用户的关系 */
            
            // 严格验证 SessionID 的一致性
//            if ( !v_SID.equals(v_User.getSessionID()) )
//            {
//                v_ResponseData.setCode("30012");
//                v_ResponseData.setMessage("会话编码无效！");
//                
//                i_Response.getWriter().println(this.toReturn(v_ResponseData));
//                return;
//            }
            
            Communication v_Communication = (Communication)XJava.getObject("USID" + v_SID);
            if ( v_Communication == null || v_Communication.getData() == null )
            {
                v_ResponseData.setCode("30013");
                v_ResponseData.setMessage("会话不存在！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            Object v_RetData = null;
            if ( v_Communication.getData() instanceof com.fms.xx.sys.model.User )
            {
                com.fms.xx.sys.model.User v_Fms     = (com.fms.xx.sys.model.User)v_Communication.getData();
                UserSSO                   v_RetUser = new UserSSO();
                
                v_RetUser.setAppKey(      v_AppKey);
                v_RetUser.setUsid(        v_Fms.getSessionID());
                v_RetUser.setUserNo(      v_Fms.getModelID());
                v_RetUser.setLoginAccount(v_Fms.getLoginAccount());
                v_RetUser.setMobile(      v_Fms.getPhone());
                v_RetUser.setUserSource(  v_Fms.getRegSign());
                v_RetUser.setUserName(    v_Fms.getUserName());
                v_RetUser.setUserType(    v_Fms.getUserType());
                
                v_RetData = v_RetUser; 
            }
            else if ( v_Communication.getData() instanceof UserSSO )
            {
                v_RetData = (UserSSO)v_Communication.getData();
            }
            else
            {
                v_RetData = v_Communication.getData();
            }
            
            v_ResponseData.setCode($Succeed);
            v_ResponseData.setMessage("成功");
            v_ResponseData.setData(v_RetData);
            i_Response.getWriter().println(this.toReturn(v_ResponseData));
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            i_Response.getWriter().println(StringHelp.replaceAll("{'code':'30001' ,'message':'" + exce.getMessage() + "'}" ,"'" ,"\""));
        }
    }
    
    
    
    public void doPost(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        // 只支持Get请求
        // Nothing.
    }
    
}
