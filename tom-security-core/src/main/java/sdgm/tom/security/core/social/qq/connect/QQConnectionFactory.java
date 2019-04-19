package sdgm.tom.security.core.social.qq.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import sdgm.tom.security.core.social.qq.api.QQ;

public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }

}
