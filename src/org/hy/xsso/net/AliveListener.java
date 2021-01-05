package org.hy.xsso.net;

import org.hy.common.Date;
import org.hy.common.ExpireMap;
import org.hy.common.Help;
import org.hy.common.MethodReflect;
import org.hy.common.net.CommunicationListener;
import org.hy.common.net.data.Communication;
import org.hy.common.net.data.CommunicationRequest;
import org.hy.common.net.data.CommunicationResponse;
import org.hy.common.xml.XJava;
import org.hy.xsso.common.AppCluster;
import org.hy.xsso.common.Log;





/**
 * 保持会话活力及有效性的通讯监听事件 
 *
 * @author      ZhengWei(HY)
 * @createDate  2017-02-04
 * @version     v1.0
 */
public class AliveListener implements CommunicationListener
{
    
    private static ExpireMap<String ,Date> $CacheTimes;
    
    
    
    public AliveListener()
    {
        if ( $CacheTimes == null )
        {
            $CacheTimes = new ExpireMap<String ,Date>();
        }
    }
    
    
    
    /**
     *  数据通讯的事件类型。即通知哪一个事件监听者来处理数据通讯（对应 ServerSocket.listeners 的分区标识）
     *  
     *  事件类型区分大小写
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-02-04
     * @version     v1.0
     *
     * @return
     */
    public String getEventType()
    {
        return "alive";
    }
    
    
    
    /**
     * 数据通讯事件的执行动作
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-02-04
     * @version     v1.0
     *
     * @param i_RequestData
     * @return
     */
    public CommunicationResponse communication(CommunicationRequest i_RequestData)
    {
        CommunicationResponse v_ResponseData = new CommunicationResponse();
        
        if ( Help.isNull(i_RequestData.getDataXID()) )
        {
            v_ResponseData.setResult(1);
            return v_ResponseData;
        }
        
        if ( i_RequestData.getData() == null )
        {
            v_ResponseData.setResult(2);
            return v_ResponseData;
        }
        
        if ( !MethodReflect.isExtendImplement(i_RequestData.getData() ,Communication.class) )
        {
            v_ResponseData.setResult(3);
            return v_ResponseData;
        }
        
        // 设置会话时间
        Communication v_SessionData = (Communication)i_RequestData.getData();
        if ( v_SessionData.getSessionTime() == null )
        {
            Date v_SessionTime = XJava.getSessionMap().getCreateTime(i_RequestData.getDataXID());
            if ( v_SessionTime != null )
            {
                v_SessionData.setSessionTime(v_SessionTime);
            }
            else
            {
                v_SessionData.setSessionTime(new Date());
            }
        }
        
        alive(i_RequestData.getDataXID() ,i_RequestData.getData() ,i_RequestData.getDataExpireTimeLen());
        
        v_ResponseData.setDataXID(i_RequestData.getDataXID());
        return v_ResponseData;
    }
    
    
    
   /**
    * 持会话活力及有效性
    * 
    * @author      ZhengWei(HY)
    * @createDate  2021-01-04
    * @version     v1.0
    *
    * @param i_DataXID            会话ID
    * @param i_Data               会话数据（一般为登陆的用户信息）
    * @param i_DataExpireTimeLen  数据的过期时长(单位：秒)。小于等于0或为空，表示永远有效
    */
    public static void alive(String i_DataXID ,Object i_Data ,long i_DataExpireTimeLen)
    {
        XJava.putObject(i_DataXID ,i_Data ,i_DataExpireTimeLen);
        
        int     v_Interval = Integer.parseInt(XJava.getParam("AliveIntervalTime").getValue());
        Date    v_Time     = $CacheTimes.get(i_DataXID);
        boolean v_IsPush   = (v_Time == null);
        
        if ( v_IsPush )
        {
            Log.log(":USID L保持集群会话活力。" ,i_DataXID);
            
            if ( v_Interval > 0 )
            {
                $CacheTimes.put(i_DataXID ,new Date() ,v_Interval);
            }
            
            AppCluster.aliveCluster(i_DataXID ,i_Data ,i_DataExpireTimeLen);
        }
    }
    
}
