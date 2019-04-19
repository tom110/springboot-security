package sdgm.tom.security.core.social.qq.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;
import sdgm.tom.security.core.social.qq.api.QQ;
import sdgm.tom.security.core.social.qq.api.QQImpl;
import sdgm.tom.security.core.social.qq.api.QQOAuth2Template;
import sun.plugin2.main.server.AppletID;

public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 获取授权码的url地址
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 获取token令牌的url地址
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId,String addSecret) {
        super(new QQOAuth2Template(appId,addSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
        this.appId=appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}
