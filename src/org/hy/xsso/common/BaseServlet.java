package org.hy.xsso.common;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.hy.common.file.FileHelp;
import org.hy.common.xml.XJSON;





/**
 * 基础服务类
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-31
 * @version     v1.0
 */
public class BaseServlet extends HttpServlet
{
    
    private static final long serialVersionUID = 123638522879125889L;
    
    public static final String $Succeed = "200";

    

    /**
     * 返回Json字符格式的结果
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-12-28
     * @version     v1.0
     *
     * @param i_Data
     * @return
     * @throws Exception
     */
    protected String toReturn(Object i_Data) throws Exception
    {
        XJSON v_XJ = new XJSON();
        
        v_XJ.setReturnNVL(false);
        
        return v_XJ.toJson(i_Data).toJSONString();
    }
    
    
    
    /**
     * 获取Post请求Body中的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-12-31
     * @version     v1.0
     *
     * @param i_Request
     * @return
     */
    protected String getPostData(HttpServletRequest i_Request) 
    {
        return FileHelp.getContent(i_Request);
    }
}
