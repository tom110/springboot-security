package sdgm.tom.security.browser.support;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperCreater {
    @Bean
    @Primary
    public ObjectMapper tomObjectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper;
    }
}
