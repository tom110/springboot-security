package sdgm.tom.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;
import sdgm.tom.security.app.authentication.openid.OpenIdAuthenticationSecurityConfig;
import sdgm.tom.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import sdgm.tom.security.core.authorize.AuthorizeConfigManager;
import sdgm.tom.security.core.properties.SecurityConstants;
import sdgm.tom.security.core.properties.SecurityProperties;
import sdgm.tom.security.core.social.TomSpringSocialConfigurer;
import sdgm.tom.security.core.validate.code.ValidateCodeSecurityConfig;

@Configuration
@EnableResourceServer
public class TomResourceServerConfig extends ResourceServerConfigurerAdapter{

    @Autowired
    private AuthenticationFailureHandler tomAuthenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler tomAuthenticationSuccessHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer tomSpringSocialConfigurer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(tomAuthenticationSuccessHandler)
                .failureHandler(tomAuthenticationFailureHandler);

        http
                //应用验证码安全配置
                .apply(validateCodeSecurityConfig)
                .and()
                //应用短信验证码认证安全配置
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                // 引用社交配置
                .apply(tomSpringSocialConfigurer)
                .and()

                .apply(openIdAuthenticationSecurityConfig)
                .and()
                .csrf().disable();

        authorizeConfigManager.config(http.authorizeRequests());
    }

}
