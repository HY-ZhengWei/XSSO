package xxx.xxx;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

ipmort xxx.xxx.SessionInterceptor





/**
 * Web项目的配置类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2019-10-23
 * @version     v1.0
 */
@Configuration
@EnableCaching
public class WebConfig implements WebMvcConfigurer 
{

    @Resource
    private SessionInterceptor sessionInterceptor;
    

    
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }

}
