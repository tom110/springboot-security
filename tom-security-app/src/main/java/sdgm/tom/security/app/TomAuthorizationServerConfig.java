package sdgm.tom.security.app;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import sdgm.tom.security.core.properties.OAuth2ClientProperties;
import sdgm.tom.security.core.properties.SecurityProperties;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class TomAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;


    @Autowired
    private TokenStore tokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
        // 如果jwt转换器和jwt增强器都存在
        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            // 将其加入到增强链上
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();

            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);

            enhancerChain.setTokenEnhancers(enhancers);

            endpoints.tokenEnhancer(enhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //        // 存储在内存中
//        clients.inMemory()
//                // 设置clientid
//                .withClient("earthchen")
//                // 设置clientsecret
//                .secret("earthchensecret")
//                // 设置令牌过期时间
//                .accessTokenValiditySeconds(7200)
//                // 允许的授权模式
//                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
//                // 配置oauth能获取的权限
//                .scopes("all")
//                .and()
//                // 第二个app
//                .withClient("earthchen_web");
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
            for (OAuth2ClientProperties config : securityProperties.getOauth2().getClients()) {
                builder.withClient(config.getClientId())
                        .secret(config.getClientSecret())
                        .accessTokenValiditySeconds(config.getAccessTokenValidateSeconds())
                        .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                        .refreshTokenValiditySeconds(2592000)
                        .scopes("all", "write", "read");
            }
        }
    }
}
