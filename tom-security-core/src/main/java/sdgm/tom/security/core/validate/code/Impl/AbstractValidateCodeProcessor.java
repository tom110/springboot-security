package sdgm.tom.security.core.validate.code.Impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;
import sdgm.tom.security.core.validate.code.ValidateCodeGenerator;
import sdgm.tom.security.core.validate.code.ValidateCodeProcessor;

import java.util.Map;

/**
 * 此抽象方法用于封装整个验证码产生流程，把流程分片
 * */
public abstract class AbstractValidateCodeProcessor<C> implements ValidateCodeProcessor{

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
        return StringUtils.substringAfter(request.getRequest().getRequestURI(),"/code/");
    }


    protected void save(ServletWebRequest request, C validateCode) throws Exception{
        sessionStrategy.setAttribute(request,SESSION_KEY_PREFIX+getProcessorType(request),validateCode);
    }

    protected abstract void send(ServletWebRequest request,C validateCode) throws Exception;
}
