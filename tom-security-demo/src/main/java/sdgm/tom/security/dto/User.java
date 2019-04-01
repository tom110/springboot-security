package sdgm.tom.security.dto;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import sdgm.tom.security.validator.MyConstraint;

import javax.validation.constraints.Past;
import java.util.Date;

public class User {

    //jsonview控制返回json视图
    public interface UserSimpleView{};
    public interface UserDetailView extends UserSimpleView{};

    private String id;
    @MyConstraint(message = "这是一个测试！")
    private String username;
    //设置表单校验
    @NotBlank(message = "密码不能是空！")
    private String password;
    @Past(message = "时间必须是过去的时间！")
    private Date birthday;

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @JsonView(UserSimpleView.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(UserSimpleView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
