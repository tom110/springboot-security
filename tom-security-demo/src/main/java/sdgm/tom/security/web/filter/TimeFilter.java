package sdgm.tom.security.web.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

//Conponent标签让这个filter在所有的请求上都起作用
//@Component
public class TimeFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter start");
        long start=new Date().getTime();
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("my filter do time:"+(new Date().getTime()-start));
        System.out.println("filter finish");
    }

    @Override
    public void destroy() {
        System.out.println("filter destroy");
    }
}
