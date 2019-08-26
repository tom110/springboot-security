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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;
import sdgm.tom.security.browser.authentication.TomAuthenticationFailHandler;
import sdgm.tom.security.browser.authentication.TomAuthenticationSuccessHandler;
import sdgm.tom.security.browser.session.TomExpiredSessionStrategy;
import sdgm.tom.security.browser.session.TomInvalidSessionStrategy;
import sdgm.tom.security.core.authentication.AbstractChannelSecurityConfig;
import sdgm.tom.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import sdgm.tom.security.core.authorize.AuthorizeConfigManager;
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



    //设置记住我的数据库数据层
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository=new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true);//初始化记住我对应的表
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

    @Autowired
    private SpringSocialConfigurer tomSocialSecurityConfig;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

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
                    .apply(tomSocialSecurityConfig)
                .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    .userDetailsService(userDetailsService)
                .and()
                    .sessionManagement()
                    .invalidSessionStrategy(invalidSessionStrategy)
                    .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                    .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                    .expiredSessionStrategy(sessionInformationExpiredStrategy)
//                    .sessionManagement()
//                    .invalidSessionStrategy(invalidSessionStrategy)
////                    .invalidSessionUrl(securityProperties.getBrowser().getSession().getSessionInvalidUrl()) //session失效提示页面
//                    // 设置最大session数量
//                    .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
//                    //当session数量达到最大时，阻止后来的用户登录,没有的话踢掉前登录用户
//                    .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
//                    // session超时处理策略
//                    .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                    .logout()
                    .logoutUrl("/signOut")
                    .logoutSuccessHandler(logoutSuccessHandler) //handler和下面的url互斥
//                    .logoutSuccessUrl("/tom-logout.html")
                    .deleteCookies("JSESSIONID")
                .and()
                .authorizeRequests()
                .and()
                .csrf().disable();

        authorizeConfigManager.config(http.authorizeRequests());
    }

}
