package org.hy.xsso.appInterfaces.servlet.bean;

import org.hy.xsso.common.BaseResponse;





/**
 * 集成认证中心获取登录临时code的响应对象
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-23
 * @version     v1.0
 */
public class LoginCodeResponse extends BaseResponse
{

    private static final long serialVersionUID = 5185330192325073611L;
    
    /** 响应数据 */
    private LoginCodeResponseData data;

    
    
    /**
     * 获取：响应数据
     */
    public LoginCodeResponseData getData()
    {
        return data;
    }

    
    /**
     * 设置：响应数据
     * 
     * @param data 
     */
    public void setData(LoginCodeResponseData data)
    {
        this.data = data;
    }
    
}
