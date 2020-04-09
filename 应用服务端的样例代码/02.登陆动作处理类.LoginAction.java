package xx.xx.xx;

import javax.servlet.http.HttpServletRequest;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.net.data.Communication;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fms.xx.common.AMFContext;
import com.fms.xx.service.IUserService;

import xxx.xxx.User;
import xxx.xxx.ISSODAO;





@Controller
public class LoginAction 
{
    
    public final static String $SessionID = "LoginUser";
    
    @Autowired
    @Qualifier("SSODAO")
    private ISSODAO ssoDAO;                                      // 注入方式1：适用于SpringMVC的注入
    
    @Xjava
    private ISSODAO ssoDAO;                                      // 注入方式2：适用于XJava的注入
    
    private ISSDAO  ssoDAO = (ISSODAO)XJava.getObject("SSODAO"); // 注入方式3：明码赋值
    
    

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
    @RequestMapping(value="/loginByXSSO" ,method=RequestMethod.POST)
    public User loadSession(@RequestParam("USID") String i_USID ,HttpServletRequest i_Request ,ModelMap io_Model)
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
        }
        
        
        // 跨域单点登陆（或主动单点服务上获取用户）。票据不是对方系统传递来的，而是本系统页面访问单点服务获取的
        if ( !Help.isNull(i_USID) )
        {
            v_SessionData = (Communication)XJava.getObject(i_USID);
            
            // 尝试从单点服务上获取会话信息
            if ( v_SessionData == null )
            {
                v_SessionData = (Communication)this.ssoDAO.syncSSOSession(i_USID);
            }
            
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
                        getSession().setMaxInactiveInterval((int)this.ssoDAO.getSSOSessionTimeOut());
                        getSession().setSessionAttribute($SessionID ,v_LocalUser);
                        
                        // 保持集群会话活力及有效性
                        v_SSODAO.aliveClusterUser(v_SessionData.getDataXID() ,v_SessionData.getData());
                    }
                    
                    return v_LocalUser;
                }
            }
        }
        else
        {
            // 释放业务Session
            // ...
            // ...
            // ...
            
            // 单点已退出
            getSession().removeAttribute($SessionID);
            getSession().invalidate();
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
        
        
        
        // i_User 是与单点登录服务相互通讯的数据结构。
        //        如果与本服务的User对象结构不一样，可以转换后使用。
        
        
        // 生成票据
        i_User.setSessionID(ISSODAO.$USID + getSession().getId());
        getSession().setMaxInactiveInterval((int)this.ssoDAO.getSSOSessionTimeOut());
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