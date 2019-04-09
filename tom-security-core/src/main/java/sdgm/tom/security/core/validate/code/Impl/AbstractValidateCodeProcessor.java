package sdgm.tom.security.core.validate.code.Impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import sdgm.tom.security.core.properties.SecurityConstants;
import sdgm.tom.security.core.validate.code.*;

import java.util.Map;

/**
 * 此抽象方法用于封装整个验证码产生流程，把流程分片
 * */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor{

    private SessionStrategy sessionStrategy=new HttpSessionSessionStrategy();

    //依赖搜索，组件容器收集器，此种方式会把所有的ValidateCodeGenerator实现放到Map中
    @Autowired
    private Map<String,ValidateCodeGenerator> validateCodeGenerators;

    @Override
    public void create(ServletWebRequest request) throws Exception{
        C validateCode=generate(request);
        save(request,validateCode);
        send(request,validateCode);
    }

    //根据请求返回不同的ValidateCodeGenerator
    @SuppressWarnings("unchecked")
    protected C generate(ServletWebRequest request){
        String type=getProcessorType(request);
        ValidateCodeGenerator validateCodeGenerator=validateCodeGenerators.get(type+"CodeGenerator");
        return (C)validateCodeGenerator.generate(request);
    }

    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/");
    }


    protected void save(ServletWebRequest request, C validateCode) throws Exception{
        sessionStrategy.setAttribute(request,getSessionKey(request),validateCode);
    }

    protected abstract void send(ServletWebRequest request,C validateCode) throws Exception;

    /**
     * 根据请求的url获取校验码的类型
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    /**
     * 构建验证码放入session时的key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }


    @SuppressWarnings("unchecked")
    @Override
    public void validate(ServletWebRequest request) {

        ValidateCodeType processorType = getValidateCodeType(request);
        String sessionKey = getSessionKey(request);

        C codeInSession = (C) sessionStrategy.getAttribute(request, sessionKey);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(processorType + "验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException(processorType + "验证码已过期");
        }

        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);
    }
}
