package org.hy.xsso.appInterfaces.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.license.AppKey;
import org.hy.common.license.SignProvider;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.xsso.appInterfaces.servlet.bean.TokenResponse;
import org.hy.xsso.appInterfaces.servlet.bean.TokenResponseData;





/**
 * 单点登录第一步：获取TokenID。它的有效时长只有2小时 
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-28
 * @version     v1.0
 */
public class GetAccessTokenServlet extends HttpServlet
{
    private static final long serialVersionUID = 5937371087484778420L;
    
    private static final Logger $Logger = new Logger(GetAccessTokenServlet.class);
    
    @SuppressWarnings("unchecked")
    private static Map<String ,AppKey> $AppKeys = (Map<String ,AppKey>)XJava.getObject("AppKeys");
    
    
    
    public void doGet(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        i_Response.setCharacterEncoding("UTF-8");
        i_Response.setContentType("application/json");
        
        TokenResponse v_ResponseData = new TokenResponse();
        
        try
        {
            long   v_Now       = Date.getNowTime().getTime();
            String v_AppKeyStr = i_Request.getParameter("appKey");
            String v_Timestamp = i_Request.getParameter("timestamp");
            String v_Signature = i_Request.getParameter("signature");
            
            if ( Help.isNull(v_AppKeyStr) || !$AppKeys.containsKey(v_AppKeyStr) )
            {
                v_ResponseData.setCode("10011");
                v_ResponseData.setMessage("AppKey无效！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            if ( Help.isNull(v_Timestamp) 
              || !Help.isNumber(v_Timestamp) 
              || Long.parseLong(v_Timestamp) > v_Now + 1000 * 60
              || Long.parseLong(v_Timestamp) < v_Now - 1000 * 60 * 60 * 2 )
            {
                v_ResponseData.setCode("10012");
                v_ResponseData.setMessage("时间戳无效或已过期！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            if ( Help.isNull(v_Signature) )
            {
                v_ResponseData.setCode("10013");
                v_ResponseData.setMessage("签名不正确！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            String  v_Code   = "appKey" + v_AppKeyStr + "timestamp" + v_Timestamp;
            AppKey  v_AppKey = $AppKeys.get(v_AppKeyStr);
            boolean v_Verify = SignProvider.verify(v_AppKey.getPublicKey().getBytes() ,v_Code ,v_Signature.getBytes("UTF-8"));
            if ( !v_Verify )
            {
                v_ResponseData.setCode("10013");
                v_ResponseData.setMessage("签名不正确！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            v_ResponseData.setCode("200");
            v_ResponseData.setMessage("正确");
            v_ResponseData.setData(new TokenResponseData());
            v_ResponseData.getData().setAccess_token(StringHelp.getUUID());
            
            i_Response.getWriter().println(this.toReturn(v_ResponseData));
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            i_Response.getWriter().println(StringHelp.replaceAll("{'code':'10001' ,'message':'" + exce.getMessage() + "'}" ,"'" ,"\""));
        }
    }
    
    
    
    /**
     * 返回Json字符格式的结果
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-12-28
     * @version     v1.0
     *
     * @param i_Data
     * @return
     * @throws Exception
     */
    private String toReturn(TokenResponse i_Data) throws Exception
    {
        XJSON v_XJ = new XJSON();
        
        v_XJ.setReturnNVL(false);
        
        return v_XJ.toJson(i_Data).toJSONString();
    }
    
    
    
    public void doPost(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        // 只支持Get请求
        // Nothing.
    }
    
}
