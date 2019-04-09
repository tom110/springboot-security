package sdgm.tom.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sdgm.tom.security.core.properties.SecurityProperties;

@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;


    /**
     * 使用此种配置方式与直接在ImageCodeGenerator上加@Compont效果一样，
     * 不过此种方式有一个好处，可以加ConditionalOnMissingBean注解
     * ConditionalOnMissingBean注解可以让系统在不存在imageCodeGenerator时才采用下面的配置，
     * 这样如果用户自己配置imageCodeGenerator就会覆盖掉我们默认的imageCodeGenerator,
     * 这样就实现了以增量的方式来拓展内容
    **/
    @Bean
    @ConditionalOnMissingBean(name="imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        ImageCodeGenerator codeGenerator=new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }
}
