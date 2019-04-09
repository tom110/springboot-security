package sdgm.tom.security.core.validate.code.image;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import sdgm.tom.security.core.validate.code.Impl.AbstractValidateCodeProcessor;

import javax.imageio.ImageIO;

@Component("imageCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode>{

    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
        ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }
}
