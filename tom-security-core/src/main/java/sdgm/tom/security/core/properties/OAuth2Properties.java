package sdgm.tom.security.core.properties;



public class OAuth2Properties{
    /**
     * 客户端配置
     */
    private OAuth2ClientProperties[] clients = {};

    /**
     * jwt的签名
     */
    private String jwtSigningKey = "tom";

    public OAuth2ClientProperties[] getClients() {
        return clients;
    }

    public void setClients(OAuth2ClientProperties[] clients) {
        this.clients = clients;
    }

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }

    public void setJwtSigningKey(String jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }


}
