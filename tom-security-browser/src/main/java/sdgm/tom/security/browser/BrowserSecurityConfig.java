package sdgm.tom.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import sdgm.tom.security.browser.authentication.TomAuthenticationFailHandler;
import sdgm.tom.security.browser.authentication.TomAuthenticationSuccessHandler;
import sdgm.tom.security.core.authentication.AbstractChannelSecurityConfig;
import sdgm.tom.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import sdgm.tom.security.core.properties.SecurityConstants;
import sdgm.tom.security.core.properties.SecurityProperties;
import sdgm.tom.security.core.validate.code.ValidateCodeFilter;
import sdgm.tom.security.core.validate.code.ValidateCodeSecurityConfig;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {


    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;


    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    //设置记住我的数据库数据层
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository=new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(true);//初始化记住我对应的表
        return tokenRepository;
    }

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TomAuthenticationSuccessHandler tomAuthenticationSuccessHandler;

    @Autowired
    private TomAuthenticationFailHandler tomAuthenticationFailHandler;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        ValidateCodeFilter validateCodeFilter=new ValidateCodeFilter();
//        validateCodeFilter.setAuthenticationFailureHandler(tomAuthenticationFailHandler);
//        validateCodeFilter.setSecurityProperties(securityProperties);
//        validateCodeFilter.afterPropertiesSet();
//
//        SmsCodeFilter smsCodeFilter=new SmsCodeFilter();
//        smsCodeFilter.setAuthenticationFailureHandler(tomAuthenticationFailHandler);
//        smsCodeFilter.setSecurityProperties(securityProperties);
//        smsCodeFilter.afterPropertiesSet();
//
//        http
//                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin() //表单登录
////                .loginPage("/tom-login.html") //表单页面
//                    .loginPage("/authentication/require") //需要身份认证时往这里跳转
//                    .loginProcessingUrl("/authentication/form") //表单提交地址
//                    .successHandler(tomAuthenticationSuccessHandler) //登录成功处理器
//                    .failureHandler(tomAuthenticationFailHandler) //登录失败处理器
//                .and()
//                    .rememberMe()
//                        .tokenRepository(persistentTokenRepository())
//                        .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
//                        .userDetailsService(userDetailsService)
//
////        http.httpBasic() //弹框登录
//                .and()
//                .authorizeRequests()
////                .antMatchers("/tom-login.html").permitAll() //匹配器
//                .antMatchers("/authentication/require",securityProperties.getBrowser().getLoginPage(),"/code/*").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and().csrf().disable()//跨站伪装防护关闭
//                .apply(smsCodeAuthenticationSecurityConfig);//添加短信中的配置

        applyPasswordAuthenticationConfig(http);

        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()


                .authorizeRequests()
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
                        "/v2/api-docs",//swagger api json
                        "/swagger-resources/configuration/ui",//用来获取支持的动作
                        "/swagger-resources",//用来获取api-docs的URI
                        "/swagger-resources/configuration/security",//安全选项
                        "/swagger-ui.html").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }

}
