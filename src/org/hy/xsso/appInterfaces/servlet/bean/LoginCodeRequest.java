package org.hy.xsso.appInterfaces.servlet.bean;

import org.hy.common.xml.SerializableDef;





/**
 * 集成认证中心获取登录临时code的请求对象
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-23
 * @version     v1.0
 */
public class LoginCodeRequest extends SerializableDef
{

    private static final long serialVersionUID = -6999560564085315445L;
    
    /** 票据号 */
    private String tokenID;
    
    /** 用户信息 */
    private UserSSO user;
    
    
    
    /**
     * 获取：用户信息
     */
    public UserSSO getUser()
    {
        return user;
    }
    
    
    /**
     * 设置：用户信息
     * 
     * @param user 
     */
    public void setUser(UserSSO user)
    {
        this.user = user;
    }
    
}
