package sdgm.tom.security.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import sdgm.tom.security.web.filter.TimeFilter;
import sdgm.tom.security.web.interceptor.TimeInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class webConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private TimeInterceptor timeInterceptor;

    //引入interceptor,需要继承WebMvcConfigurerAdapter,在addInterceptors里配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }

    //注册Filter，可以规定过滤请求
    @Bean
    public FilterRegistrationBean timeFilter(){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        TimeFilter timeFilter=new TimeFilter();
        filterRegistrationBean.setFilter(timeFilter);
        List<String> urls=new ArrayList<>();
        urls.add("/*");
        return filterRegistrationBean;
    }
}
