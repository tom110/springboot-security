package sdgm.tom.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import sdgm.tom.security.core.validate.code.ValidateCode;

@ConfigurationProperties(prefix = "tom.security")
public class SecurityProperties {

    private ValidateCodeProperties code = new ValidateCodeProperties();
    /**
     * 社交登录相关配置
     */
    private SocialProperties social = new SocialProperties();

    public SocialProperties getSocial() {
        return social;
    }

    public void setSocial(SocialProperties social) {
        this.social = social;
    }

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }

    private BrowserProperties browser=new BrowserProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
