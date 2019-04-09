package sdgm.tom.security.core.properties;

public class BrowserProperties {
    //没有配置用户登录页，使用默认登录页
    private String loginPage="/tom-login.html";

    private LoginType loginType=LoginType.JSON;

    private int rememberMeSeconds=3600;

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }
}
