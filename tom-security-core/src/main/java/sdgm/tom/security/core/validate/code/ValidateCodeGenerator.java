package sdgm.tom.security.core.validate.code;


import org.springframework.web.context.request.ServletWebRequest;

//接口封装验证码的实现
public interface ValidateCodeGenerator {
    ValidateCode generate(ServletWebRequest request);
}
