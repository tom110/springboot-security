package sdgm.tom.security.core.validate.code;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import sdgm.tom.security.core.properties.SecurityConstants;
import sdgm.tom.security.core.properties.SecurityProperties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean{

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    private SecurityProperties securityProperties;

    //存放所有需要验证验证码的url
    private Map<String,ValidateCodeType> urlMap=new HashedMap();
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(),ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(),ValidateCodeType.SMS);
    }

    private void addUrlToMap(String urlString, ValidateCodeType type) {
        if(StringUtils.isNotBlank(urlString)){
            String[] urls=StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString,",");
            for(String url:urls){
                urlMap.put(url,type);
            }
        }
    }
    @Autowired
    private ValidateCodeProcessorHolder  validateCodeProcessorHolder;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ValidateCodeType type=getValidateCodeType(httpServletRequest);

        if(type!=null){
            logger.info("校验请求（"+httpServletRequest.getRequestURI()+"）中的验证码，验证类型"+type);
            try{
                validateCodeProcessorHolder.findValidateCodeProcessor(type)
                        .validate(new ServletWebRequest(httpServletRequest,httpServletResponse));
                logger.info("验证码校验通过");
            }catch (ValidateCodeException exception){
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,exception);
                return;
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private ValidateCodeType getValidateCodeType(HttpServletRequest httpServletRequest) {
        ValidateCodeType result = null;
        if (!StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, httpServletRequest.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }
}
