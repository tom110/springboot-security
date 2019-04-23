package sdgm.tom.security.core.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;
import sdgm.tom.security.core.properties.SecurityProperties;

import javax.sql.DataSource;

@EnableSocial
@Configuration
public class SocialConfig extends SocialConfigurerAdapter {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;


    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository =
                new JdbcUsersConnectionRepository(dataSource,
                        connectionFactoryLocator,
                        Encryptors.noOpText());
        if (connectionSignUp!=null){
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    //默认使用内存repostitory,这里覆盖掉他
    @Bean
    public UsersConnectionRepository usersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator){
        return getUsersConnectionRepository(connectionFactoryLocator);
    }

    @Bean
    public SpringSocialConfigurer tomSocialSecurityConfig() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        TomSpringSocialConfigurer configurer = new TomSpringSocialConfigurer(filterProcessesUrl);
        //设置social中的注册页为
        configurer.signupUrl(securityProperties.getBrowser().getSignUpPage());
        return configurer;
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator));
    }
}
