package sdgm.tom.security.service;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String greeting(String name) {
        System.out.println("greeting hello service");
        return "hello "+name;
    }
}
