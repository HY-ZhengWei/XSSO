package org.hy.xsso.appInterfaces.servlet.bean;

import org.hy.common.xml.SerializableDef;





/**
 * 访问Token的数据结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-22
 * @version     v1.0
 */
public class TokenResponse extends SerializableDef
{
    
    private static final long serialVersionUID = 2652864387178948149L;

    /** 响应代码 */
    private String code;
    
    /** 响应消息 */
    private String message;
    
    /** 响应数据 */
    private TokenResponseData data;

    
    
    /**
     * 获取：响应代码
     */
    public String getCode()
    {
        return code;
    }

    
    /**
     * 获取：响应消息
     */
    public String getMessage()
    {
        return message;
    }

    
    /**
     * 获取：响应数据
     */
    public TokenResponseData getData()
    {
        return data;
    }

    
    /**
     * 设置：响应代码
     * 
     * @param code 
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    
    /**
     * 设置：响应消息
     * 
     * @param message 
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    
    /**
     * 设置：响应数据
     * 
     * @param data 
     */
    public void setData(TokenResponseData data)
    {
        this.data = data;
    }
    
}
