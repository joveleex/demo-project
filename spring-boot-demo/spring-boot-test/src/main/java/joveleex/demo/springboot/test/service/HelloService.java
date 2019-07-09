package joveleex.demo.springboot.test.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @SuppressWarnings("WeakerAccess")
    public String sayHello(String name) {
        return "Hello " + name + " !";
    }
}
