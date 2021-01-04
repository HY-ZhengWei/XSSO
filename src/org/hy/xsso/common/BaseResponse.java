package org.hy.xsso.common;

import org.hy.common.xml.SerializableDef;





/**
 * 通用的请求响应类
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-31
 * @version     v1.0
 */
public class BaseResponse extends SerializableDef
{

    private static final long serialVersionUID = -393692673433172315L;
    
    
    
    /** 响应代码 */
    private String code;
    
    /** 响应消息 */
    private String message;
    
    
    
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
    
}
