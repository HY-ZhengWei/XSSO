package org.hy.xsso.common;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.thread.ThreadPool;
import org.hy.common.xml.XJava;
import org.hy.common.xml.plugins.AppInitConfig;





/**
 * Web初始化信息
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-12
 * @version     v1.0  
 */
public final class InitConfig extends AppInitConfig
{
    
    private static boolean $Init = false;
    
    private String xmlRoot;
    
    
    
    public InitConfig()
    {
        this(Help.getWebINFPath());
    }
    
    
    
    public InitConfig(String i_XmlRoot)
    {
        this.xmlRoot = i_XmlRoot;
        init();
    }
    
    
    
    @SuppressWarnings("unchecked")
    private synchronized void init()
    {
        if ( !$Init )
        {
            $Init = true;
            
            try
            {
                this.initW("sys.Config.xml" ,this.xmlRoot);
                this.initW("startup.Config.xml" ,this.xmlRoot);
                this.initW((List<Param>)XJava.getObject("StartupConfig") ,this.xmlRoot);
                this.initW(((Param)XJava.getObject("RootPackageName")).getValue());
                init_TPool();
                this.initW("job.Config.xml" ,this.xmlRoot);
            }
            catch (Exception exce)
            {
                System.out.println(exce.getMessage());
                exce.printStackTrace();
            }
            
            
            SSOCluster.syncSSOSessions();
        }
    }
    
    
    
    private void init_TPool()
    {
        ThreadPool.setMaxThread(    this.getIntConfig("TPool_MaxThread"));
        ThreadPool.setMinThread(    this.getIntConfig("TPool_MinThread"));
        ThreadPool.setMinIdleThread(this.getIntConfig("TPool_MinIdleThread"));
        ThreadPool.setIntervalTime( this.getIntConfig("TPool_IntervalTime"));
        ThreadPool.setIdleTimeKill( this.getIntConfig("TPool_IdleTimeKill"));
    }
    
    
    
    private int getIntConfig(String i_XJavaID)
    {
        return Integer.parseInt(XJava.getParam(i_XJavaID).getValue());
    }
    
}
