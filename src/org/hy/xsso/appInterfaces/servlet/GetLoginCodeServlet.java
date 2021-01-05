package org.hy.xsso.appInterfaces.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Date;
import org.hy.common.ExpireMap;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.license.AES;
import org.hy.common.license.AppKey;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.xsso.appInterfaces.servlet.bean.LoginCodeResponse;
import org.hy.xsso.appInterfaces.servlet.bean.LoginCodeResponseData;
import org.hy.xsso.appInterfaces.servlet.bean.UserSSO;
import org.hy.xsso.common.BaseServlet;





/**
 * 单点登录第二步：获取登录临时Code。它是一次性的，使用一次后失效。 
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-29
 * @version     v1.0
 */
public class GetLoginCodeServlet extends BaseServlet
{
    
    private static final long serialVersionUID = 460399102486930832L;

    private static final Logger $Logger = new Logger(GetLoginCodeServlet.class);
    
    /** 
     * 生成的临时Code
     * 
     * map.key    为临时Code
     * map.value  为UserSSO
     */
    private static ExpireMap<String ,UserSSO> $CodeToAppKeys = new ExpireMap<String ,UserSSO>();
    
    /**
     * 所有配置有效的应用AppKey数据
     */
    @SuppressWarnings("unchecked")
    private static Map<String ,AppKey>        $AppKeys       = (Map<String ,AppKey>)XJava.getObject("AppKeys");
    
    
    
    /**
     * 通过访问登录临时Code匹配到应用的AppKey、SessionID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-01-04
     * @version     v1.0
     *
     * @param i_LoginCode
     * @return
     */
    public synchronized static UserSSO getAppKey(String i_LoginCode)
    {
        return $CodeToAppKeys.remove(i_LoginCode);
    }
    
    
    
    public void doGet(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        // 只支持Post请求
        // Nothing.
    }
    
    
    
    public void doPost(HttpServletRequest i_Request, HttpServletResponse i_Response) throws ServletException, IOException 
    {
        i_Response.setCharacterEncoding("UTF-8");
        i_Response.setContentType("application/json");
        
        LoginCodeResponse v_ResponseData = new LoginCodeResponse();
        
        try
        {
            String v_Token = i_Request.getParameter("token");
            if ( Help.isNull(v_Token) )
            {
                v_ResponseData.setCode("20011");
                v_ResponseData.setMessage("请求票据无效！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            String v_AppKey = GetAccessTokenServlet.getAppKey(v_Token);
            if ( Help.isNull(v_AppKey) )
            {
                v_ResponseData.setCode("20011");
                v_ResponseData.setMessage("请求票据无效！");
                
                i_Response.getWriter().println(this.toReturn(v_ResponseData));
                return;
            }
            
            String v_BodyData  = this.getPostData(i_Request);
            if ( Help.isNull(v_BodyData) )
            {
                v_ResponseData.setCode($Succeed);
                v_ResponseData.setMessage("成功");
                v_ResponseData.setData(new LoginCodeResponseData());
                
                v_ResponseData.getData().setCode(StringHelp.getUUID());
                
                AES v_AES = new AES(2 ,$AppKeys.get(v_AppKey).getPrivateKey());
                
                UserSSO v_User = new UserSSO();
                v_User.setAppKey(v_AppKey);
                v_User.setSessionID(i_Request.getSession().getId());
                
                Cookie v_Cookie = new Cookie("UCID", URLEncoder.encode(v_AES.encrypt(v_User.getSessionID() + "@" + v_AppKey + "@" + Date.getNowTime().getTime()) ,"UTF-8"));
                i_Response.addCookie(v_Cookie);
                
                $CodeToAppKeys.put(v_ResponseData.getData().getCode() ,v_User ,5 * 60);
                
                $Logger.info("生成临时登录Code=" + v_ResponseData.getData().getCode() + " ,UCID=" + v_Cookie.getValue() + " ,AppKey=" + v_AppKey);
            }
            
            
            i_Response.getWriter().println(this.toReturn(v_ResponseData));
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            i_Response.getWriter().println(StringHelp.replaceAll("{'code':'20001' ,'message':'" + exce.getMessage() + "'}" ,"'" ,"\""));
        }
    }
    
}
