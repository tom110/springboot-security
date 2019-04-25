package sdgm.tom.security.core.properties;

public class BrowserProperties {

    /**
     * session配置
     */
    private SessionProperties session = new SessionProperties();
    //没有配置用户登录页，使用默认登录页
    private String loginPage="/tom-login.html";

    private String signUpPage="/tom-signUp.html";

    private LoginType loginType=LoginType.JSON;

    private int rememberMeSeconds=3600;

    /**
     * 退出成功时跳转的url，如果配置了，则跳到指定的url，如果没配置，则返回json数据。
     */
    private String signOutUrl;

    public String getSignOutUrl() {
        return signOutUrl;
    }

    public void setSignOutUrl(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }

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

    public String getSignUpPage() {
        return signUpPage;
    }

    public void setSignUpPage(String signUpPage) {
        this.signUpPage = signUpPage;
    }

    public SessionProperties getSession() {
        return session;
    }

    public void setSession(SessionProperties session) {
        this.session = session;
    }
}
