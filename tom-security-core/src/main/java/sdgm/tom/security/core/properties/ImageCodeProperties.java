package sdgm.tom.security.core.properties;

public class ImageCodeProperties extends SmsCodeProperties{
    private int width = 60;   //图片宽度
    private int height = 25;    //图片高度

    //让图形验证码的长度为4，覆盖掉父类短信验证码
    public ImageCodeProperties(){
        setLength(4);
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
