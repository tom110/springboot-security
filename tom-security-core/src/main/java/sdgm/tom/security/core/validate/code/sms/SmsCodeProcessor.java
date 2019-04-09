package sdgm.tom.security.core.validate.code.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import sdgm.tom.security.core.validate.code.Impl.AbstractValidateCodeProcessor;
import sdgm.tom.security.core.validate.code.ValidateCode;

@Component("smsCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Autowired
    private SmsCodeSender smsCodeSender;
    @Override
    protected void send(ServletWebRequest request, ValidateCode smsCode) throws Exception {
        String mobile= ServletRequestUtils.getRequiredStringParameter(request.getRequest(),"mobile");
        //使用短信服务商发送短信
        smsCodeSender.send(mobile,smsCode.getCode());
    }
}
