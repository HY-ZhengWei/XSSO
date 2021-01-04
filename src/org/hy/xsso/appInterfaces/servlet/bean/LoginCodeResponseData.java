package org.hy.xsso.appInterfaces.servlet.bean;

import org.hy.common.xml.SerializableDef;





/**
 * 集成认证中心获取登录临时code的响应数据
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-23
 * @version     v1.0
 */
public class LoginCodeResponseData extends SerializableDef
{

    private static final long serialVersionUID = 2433487617090337488L;
    
    /** 用户登录临时code */
    private String code;

    
    
    /**
     * 获取：用户登录临时code
     */
    public String getCode()
    {
        return code;
    }

    
    /**
     * 设置：用户登录临时code
     * 
     * @param code 
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
}
