package joveleex.demo.springboot.test.controller;

import joveleex.demo.springboot.test.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(String name) {
        return "Hello " + name + " !";
    }

    @PostMapping("/user")
    public String addUser(@RequestBody User user) {
        return user.toString();
    }
}
