package sdgm.tom.security.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import sdgm.tom.security.exception.UserNotExistException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    //让所有controller里面触发UserNotExistException错误的到这个地方来处理，可以实现方法的传参
    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> handlerUserNotExistException(UserNotExistException ex){
        Map<String,Object> result=new HashMap<>();
        result.put("id",ex.getId());
        result.put("message", ex.getMessage());
        return result;
    }
}
