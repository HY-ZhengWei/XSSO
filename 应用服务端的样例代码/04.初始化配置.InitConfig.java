package xxx.xxx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.thread.ThreadPool;
import org.hy.common.xml.XJava;
import org.hy.common.xml.plugins.AppInitConfig;

import xxx.xxx.ISSO;





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
    
    
    
    public InitConfig()
    {
        this(true);
    }
    
    
    
    public InitConfig(boolean i_IsStartJobs)
    {
        init(i_IsStartJobs);
    }
    
    
    
    private synchronized void init(boolean i_IsStartJobs)
    {
        if ( !$Init )
        {
            $Init = true;
            
            try
            {
                this.init("sys.Config.xml");
                this.init("startup.Config.xml");
                this.init((List<Param>)XJava.getObject("StartupConfig") ,Help.getClassPath(this));
                this.init(((Param)XJava.getObject("RootPackageName")).getValue());
            }
            catch (Exception exce)
            {
                System.out.println(exce.getMessage());
                exce.printStackTrace();
            }
            
            
            // 单点会话同步
            ((ISSODAO)XJava.getObject("SSODAO")).syncSSOSessions();
        }
    }
    
}
