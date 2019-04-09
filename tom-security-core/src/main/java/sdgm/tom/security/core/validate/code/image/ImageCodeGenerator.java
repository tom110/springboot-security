package sdgm.tom.security.core.validate.code.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import sdgm.tom.security.core.properties.SecurityProperties;
import sdgm.tom.security.core.validate.code.ValidateCodeGenerator;
import sdgm.tom.security.core.validate.code.image.ImageCode;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    private Random rm = new Random();
    private String text;        //验证码文本
    private String codes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";//验证码
    private String[] fontNames = {"宋体","华文楷体", "黑体", "微软雅黑", "楷体_GB2312"}; //字体名称

    @Override
    public ImageCode generate(ServletWebRequest request) {
        int width = ServletRequestUtils.getIntParameter(request.getRequest(),"width",securityProperties.getCode().getImage().getWidth());   //图片宽度
        int height = ServletRequestUtils.getIntParameter(request.getRequest(),"height",securityProperties.getCode().getImage().getHeight());    //图片高度

        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        StringBuilder sb = new StringBuilder();
        int length=ServletRequestUtils.getIntParameter(request.getRequest(),"length",securityProperties.getCode().getImage().getLength());
        for (int i = 0; i < length; i++) {
            int index = rm.nextInt(codes.length());
            String s= codes.charAt(index)+"";
            sb.append(s);
            float x = 1.0f * width / (length+1) * (i+1);
            g.setFont(randomFont());
            g.setColor(randomColor());
            g.drawString(s,x,height-5);
        }
        this.text = sb.toString();
        drawLine(image,width,height);

        return new ImageCode(image,sb.toString(),securityProperties.getCode().getImage().getExpireIn());
    }

    /*
     *获取随机字体
     */
    public Font randomFont(){
        int style = rm.nextInt(4);  //字体样式：0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int size = rm.nextInt(4)+20;    //字体大小
        int index = rm.nextInt(fontNames.length);
        String fontName = fontNames[index];
        Font font = new Font(fontName,style,size);
        return font;
    }
    /*
     * 获取随机颜色
     */
    public Color randomColor(){
        int r = 200+rm.nextInt(20);
        int g = 200+rm.nextInt(20);
        int b = 200+rm.nextInt(20);
        Color color = new Color(r,g,b);
        return color;
    }
    /*
     *对验证码图片画干扰线，防止暴力破解
     */
    public BufferedImage drawLine(BufferedImage image,int width,int height){
        Graphics2D g = image.createGraphics();
        for(int i=0;i<3;i++){               //三条干扰线
            int x1 = rm.nextInt(width);
            int y1 = rm.nextInt(height);
            int x2 = rm.nextInt(width);
            int y2 = rm.nextInt(height);
            g.drawLine(x1,y1,x2,y2);
        }
        return image;
    }
}
