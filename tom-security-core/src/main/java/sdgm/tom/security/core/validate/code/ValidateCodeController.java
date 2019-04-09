package sdgm.tom.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import sdgm.tom.security.core.properties.SecurityProperties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@RestController
public class ValidateCodeController {


    public static final String SESSION_KEY="SESSION_KEY_IMAGE_CODE";

    /**此处一定要定义为ValidateCodeGenerator，
     * 不然会没有找到ImageCodeGenerator这个类，
     * 因为ImageCodeGenerator是监测ImageCodeGenerator不存在初始化的
    **/
     @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    private SessionStrategy sessionStrategy=new HttpSessionSessionStrategy();

    @GetMapping("/code/image")
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode=imageCodeGenerator.generate(request);
        sessionStrategy.setAttribute(new ServletWebRequest(request),SESSION_KEY,imageCode);
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());

    }
}
