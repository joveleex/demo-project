package joveleex.demo.springboot.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    /**
     * Spring Security提供了多个加密方案，官方推荐使用BCryptPasswordEncoder
     * new BCryptPasswordEncoder(10) 10是个迭代系数，值在4～31之间，默认为10，系数越高，密钥迭代次数越多
     */
    private static void generator() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        System.out.println(encoder.encode("123456"));
    }

    public static void main(String[] args) {
        PasswordGenerator.generator();
    }
}
