package xx.xx.xx;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.net.data.Communication;
import org.hy.common.xml.XJava;

import com.fms.xx.common.AMFContext;
import com.fms.xx.service.IUserService;

import xxx.xxx.User;
import xxx.xxx.ISSODAO;





public class LoginAction 
{
    
    public final static String $SessionID = "LoginUser";
    
    

    public User loadSession()
    {
        return this.loadSession("");
    }
    
    
    
    /**
     * 检查session是否失效
     * 
     * @param i_USID  票据
     * @return
     */
    public User loadSession(String i_USID)
    {
        // 应用服务的相关代码
        // ...
        // ...
        // ...
        // 其后执行下面的代码
        
        
        ISSODAO       v_SSODAO      = (ISSODAO) XJava.getObject("SSODAO");
        User          v_LoginUser   = (User)getSession().getAttribute($SessionID);
        Communication v_SessionData = null;
        
        if ( v_LoginUser != null )
        {   
            v_SessionData = (Communication)XJava.getObject(v_LoginUser.getSessionID());
            if ( v_SessionData != null )
            {
                // 保持集群会话活力及有效性
                v_SSODAO.aliveClusterUser(v_LoginUser.getSessionID() ,(User)v_SessionData.getData());
                return v_LoginUser;
            }
            else
            {
                // 单点已退出
                getSession().removeAttribute($SessionID);
                getSession().invalidate();
                
                return null;
            }
        }
        
        
        // 跨域单点登陆
        if ( !Help.isNull(i_USID) )
        {
            v_SessionData = (Communication)XJava.getObject(i_USID);
            
            if ( v_SessionData != null )
            {
                v_LoginUser = (User)v_SessionData.getData();
                        
                if ( v_LoginUser != null )
                {
                    xxx.xxx v_LocalUser = null;   // 本系统的用户（结构可能与其它系统的不一样）
                    
                    // 判定不是本系统的用户时，将初始化本系统的用户信息
                    // 当多个系统的用户结构不一样时，才需要下面的代码
                    // 当所有系统的用户结构是一样时，只须：getSession().setSessionAttribute("本系统的会话ID" ,v_LoginUser); 即可。
                    if ( !StringHelp.isContains(v_LoginUser.getUserType() ,"UserType01" ,"UserType02") )
                    {
                        v_LocalUser = 初始用户信息; // v_UserService.queryByLoginAccount(v_LoginUser.getLoginAccount());
                    }
                    // 当多个系统的用户结构不一样时，才需要下面的代码
                    // 当所有系统的用户结构是一样时，只须：getSession().setSessionAttribute("本系统的会话ID" ,v_LoginUser); 即可。
                    else
                    {
                        v_LocalUser = 初始用户信息; // v_UserService.queryByID(v_LoginUser.getUserID());
                    }
                    
                    if ( v_LocalUser != null )
                    {
                        System.out.println(Date.getNowTime().getFullMilli() + "  跨域单点登陆：" + v_LocalUser.getLoginAccount() + v_LocalUser.getUserName());
                        
                        v_LocalUser.setSessionID(v_LoginUser.getSessionID());  // 单点退出时用的票据
                        getSession().setSessionAttribute($SessionID ,v_LocalUser);
                        
                        // 保持集群会话活力及有效性
                        v_SSODAO.aliveClusterUser(v_SessionData.getDataXID() ,v_SessionData.getData());
                    }
                    
                    return v_LocalUser;
                }
            }
        }
        
        return null;
    }
    
    

    /**
     * 用户登陆
     * 
     * @param i_User
     * @return
     */
    public User login(User i_User) 
    {
        // 应用服务的登陆验证代码
        // ...
        // ...
        // ...
        // 验证成功后，执行下面的代码
        
        
        
        // 生成票据
        i_User.setSessionID(ISSODAO.$USID + getSession().getId());
        getSession().setSessionAttribute($SessionID, i_User);
        
        // 单点登陆
        ISSODAO  v_SSODAO = (ISSODAO) XJava.getObject("SSODAO");
        v_SSODAO.loginClusterUser(i_User.getSessionID() ,i_User);
        
        return i_User;
    }
    
    

    /**
     * 用户注销
     * 
     * @param i_User
     * @return
     */
    public User logoutUser(User i_User)
    {
        // 应用服务的相关代码
        // ...
        // ...
        // ...
        // 其后执行下面的代码
        
        
        
        User v_LoginUser = (User)getSession().getSessionAttribute($SessionID);
        if ( v_LoginUser != null )
        {
            getSession().removeAttribute($SessionID);
            getSession().invalidate();
                            
            // 单点退出
            ISSODAO v_SSODAO = (ISSODAO) XJava.getObject("SSODAO");
            v_SSODAO.logoutClusterUser(v_LoginUser.getSessionID());
        }
        
        return i_User;
    }
    
}