package sdgm.tom.security.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import sdgm.tom.security.dto.User;
import sdgm.tom.security.exception.UserNotExistException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public Object getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

//    @RequestMapping(value = "/user",method = RequestMethod.GET)
    @GetMapping
    // RequestParam可以设置默认值和是否必填
    @JsonView(User.UserSimpleView.class)
    public List<User> query(@RequestParam(name="username",required = false,defaultValue = "tom") String name
                        ,@PageableDefault(page = 7,size=7,sort = "username,asc") Pageable pageable){

        System.out.println(name);

        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getSort());

        List<User> users=new ArrayList<User>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }

    //RequestMapping使用正则表达式规定只能接受一个数字，让不符合请求规则的请求
//    @RequestMapping(value="/user/{id:\\d+}",method = RequestMethod.GET)
    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable(name="id") String id){
        User user=new User();
        user.setUsername("tom");
        return user;
//        throw new UserNotExistException("user not exist!",id);

    }


    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors){

        //bindingResult可以捕获校验错误
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach(error->System.out.println(error.getDefaultMessage()));
        }

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    @PutMapping("{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors){

        //bindingResult可以捕获校验错误
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach(error->{
                //输出错误信息的同时，输出错误对应的列
//                FieldError fieldError=(FieldError)error;
//                String message=fieldError.getField()+" "+error.getDefaultMessage();
//                System.out.println(message);
                //输出默认信息
                System.out.println(error.getDefaultMessage());
            });
        }

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    @DeleteMapping("{id:\\d+}")
    public void delete(@PathVariable String id){
        System.out.println(id);
    }
}
