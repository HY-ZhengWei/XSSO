package org.hy.xsso.appInterfaces.servlet.bean;

import org.hy.common.xml.SerializableDef;





/**
 * 访问Token的数据结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-22
 * @version     v1.0
 */
public class TokenResponseData extends SerializableDef
{
    
    private static final long serialVersionUID = 2029220892157842421L;

    /** 过期时长（单位：秒） */
    private Integer expire;
    
    /** 访问Token */
    private String  access_token;

    
    
    /**
     * 获取：过期时长（单位：秒）
     */
    public Integer getExpire()
    {
        return expire;
    }

    
    /**
     * 获取：访问Token
     */
    public String getAccess_token()
    {
        return access_token;
    }

    
    /**
     * 设置：过期时长（单位：秒）
     * 
     * @param expire 
     */
    public void setExpire(Integer expire)
    {
        this.expire = expire;
    }

    
    /**
     * 设置：访问Token
     * 
     * @param access_token 
     */
    public void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }
    
}
