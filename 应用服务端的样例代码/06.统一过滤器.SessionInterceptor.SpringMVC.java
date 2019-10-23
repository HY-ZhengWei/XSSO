package xxx.xxx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hy.common.net.data.Communication;
import org.hy.common.xml.XJava;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import xxx.xxx.BaseController;
import xxx.xxx.ISSODAO;
import xxx.xxx.User;





/**
 * 通用spring-MVC登录拦截器
 *
 * @author      ZhengWei(HY)
 * @createDate  2019-06-26
 * @version     v1.0
 */
@Component
public class SessionInterceptor implements HandlerInterceptor 
{

    @Override
    public boolean preHandle(HttpServletRequest i_Request ,HttpServletResponse i_Response ,Object handler) throws Exception
    {
        HttpSession v_Session = i_Request.getSession();
        User        v_User    = (User)v_Session.getAttribute(BaseController.$SessionUser);
        
        if ( v_User == null )
        {
            i_Response.setHeader("Content-type" ,"text/html;charset=UTF-8");
            i_Response.getWriter().write("您尚未登录，无法使用该功能！");
            return false;
        }
        else
        {
            ISSODAO       v_SSODAO      = (ISSODAO) XJava.getObject("SSODAO");
            Communication v_SessionData = (Communication)XJava.getObject(v_User.getSessionID());
            
            // 尝试从单点服务上获取会话信息
            if ( v_SessionData == null )
            {
                v_SessionData = (Communication)v_SSODAO.syncSSOSession(v_User.getSessionID());
            }
            
            if ( v_SessionData == null )
            {
                v_Session.removeAttribute(BaseController.$SessionUser);
                v_Session.invalidate();
                throw new RuntimeException("会话超时或用户已单点退出！");
            }
            else
            {
                v_Session.setAttribute(BaseController.$SessionUser ,v_User);
                
                ISSODAO v_SSODAO = (ISSODAO) XJava.getObject("SSODAO");
                v_SSODAO.aliveClusterUser(v_SessionData.getDataXID() ,(User)v_SessionData.getData());
            }
        }
        return true;
    }



    @Override
    public void postHandle(HttpServletRequest request ,HttpServletResponse response ,Object handler ,ModelAndView modelAndView) throws Exception
    {
    }



    @Override
    public void afterCompletion(HttpServletRequest request ,HttpServletResponse response ,Object handler ,Exception ex) throws Exception
    {
    }

}