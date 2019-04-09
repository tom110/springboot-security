package sdgm.tom.security.browser.authentication;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sdgm.tom.security.browser.support.ObjectMapperCreater;
import sdgm.tom.security.core.properties.LoginType;
import sdgm.tom.security.core.properties.SecurityProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("tomAuthenticationSuccessHandler")
//public class TomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
public class TomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapperCreater objectMapperCreater;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("登陆成功");

        //如果配置里是json，登录成功返回json，如果配置是跳转，登录成功跳转到父类成功处理
        if(LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())){
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapperCreater.tomObjectMapper().writeValueAsString(authentication));
        }else{
            super.onAuthenticationSuccess(httpServletRequest,httpServletResponse,authentication);
        }


    }
}
