package xxx.xxx;

import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hy.common.net.data.Communication;
import org.hy.common.xml.XJava;

import com.fms.calc.common.BaseController;

import xxx.xxx.LoginAction;
import xxx.xxx.ISSODAO;
import xxx.xxx.User;





/**
 * 单点登陆DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-10-12
 * @version     v1.0
 */
public class SessionInterceptor implements MethodInterceptor 
{
    
    public Object invoke(MethodInvocation invocation) throws Throwable 
    {
        User v_LoginUser = (User)getSession().getAttribute(LoginAction.$SessionID);
        
        if ( v_LoginUser == null ) 
        {
            throw new RuntimeException("会话超时或用户已单点退出！");
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
                getSession().setAttribute(LoginAction.$SessionID ,v_LoginUser);
                
                ISSODAO v_SSODAO = (ISSODAO) XJava.getObject("SSODAO");
                v_SSODAO.aliveClusterUser(v_SessionData.getDataXID() ,(User)v_SessionData.getData());
            }
        }
        
        return invocation.proceed();
    }
    
}
