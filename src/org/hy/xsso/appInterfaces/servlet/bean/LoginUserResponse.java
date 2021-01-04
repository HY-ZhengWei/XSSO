package org.hy.xsso.appInterfaces.servlet.bean;

import org.hy.xsso.common.BaseResponse;





/**
 * 获取已登录的用户信息的响应对象
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-01-04
 * @version     v1.0
 */
public class LoginUserResponse extends BaseResponse
{

    private static final long serialVersionUID = 5185330192325073611L;
    
    /** 响应数据 */
    private Object data;

    
    
    /**
     * 获取：响应数据
     */
    public Object getData()
    {
        return data;
    }

    
    /**
     * 设置：响应数据
     * 
     * @param data 
     */
    public void setData(Object data)
    {
        this.data = data;
    }
    
}
