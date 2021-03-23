package org.hy.xsso.appInterfaces.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Date;
import org.hy.common.ExpireMap;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.license.AppKey;
import org.hy.common.license.SignProvider;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.xsso.appInterfaces.servlet.bean.TokenResponse;
import org.hy.xsso.appInterfaces.servlet.bean.TokenResponseData;
import org.hy.xsso.common.BaseServlet;





/**
 * 单点登录第一步：获取TokenID。它的有效时长只有2小时 
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-28
 * @version     v1.0
 */
public class GetAccessTokenServlet extends BaseServlet
{
    private static final long serialVersionUID = 5937371087484778420L;
    
    private static final Logger $Logger = new Logger(GetAccessTokenServlet.class);
    
    /**
     * 所有配置有效的应用AppKey数据
     */
    @SuppressWarnings("unchecked")
    private static Map<String ,AppKey>       $AppKeys          = (Map<String ,AppKey>)XJava.getObject("AppKeys");
    
    /** 
     * 生成的访问TokenID 
     * 
     * map.key    为AppKey
     * map.value  为TokenID
     */
    private static ExpireMap<String ,String> $AppKeyToTokenIDs = new ExpireMap<String ,String>();
    
    /** 
     * 生成的访问TokenID 
     * 
     * map.key    为TokenID
     * map.value  为AppKey
     */
    private static ExpireMap<String ,String> $TokenIDToAppKeys = new ExpireMap<String ,String>();
    
    
    
    /**
     * 通过访问TokenID匹配到应用的AppKey
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-12-31
     * @version     v1.0
     *
     * @param i_TokenID
     * @return
     */
    public static String getAppKey(String i_TokenID)
    {
        return $TokenIDToAppKeys.get(i_TokenID);
    }
    
    
    
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
              || Long.parseLong(v_Timestamp) < v_Now - 1000 * 60 * 3 )
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
            
            v_ResponseData.setCode($Succeed);
            v_ResponseData.setMessage("成功");
            v_ResponseData.setData(new TokenResponseData());
            
            if ( $AppKeyToTokenIDs.containsKey(v_AppKey.getAppKey()) )
            {
                v_ResponseData.getData().setAccess_token($AppKeyToTokenIDs.get(v_AppKey.getAppKey()));
                v_ResponseData.getData().setExpire((int)($AppKeyToTokenIDs.getExpireTimeLen(v_AppKey.getAppKey()) / 1000));
            }
            else
            {
                v_ResponseData.getData().setAccess_token(StringHelp.getUUID());
                v_ResponseData.getData().setExpire(7200);
                
                $AppKeyToTokenIDs.put(v_AppKey.getAppKey() ,v_ResponseData.getData().getAccess_token() ,7200);
                $TokenIDToAppKeys.put(v_ResponseData.getData().getAccess_token() ,v_AppKey.getAppKey() ,7200);
            }
            
            
            i_Response.getWriter().println(this.toReturn(v_ResponseData));
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            i_Response.getWriter().println(StringHelp.replaceAll("{'code':'10001' ,'message':'" + exce.getMessage() + "'}" ,"'" ,"\""));
        }
    }
    
    
    
    public void doPost(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        // 只支持Get请求
        // Nothing.
    }
    
}
