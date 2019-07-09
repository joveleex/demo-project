package joveleex.demo.springboot.test;

import joveleex.demo.springboot.test.pojo.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@JsonTest
public class JSONTest {

    @Resource
    private JacksonTester<User> jacksonTester;

    @Test
    public void testSerialize() throws IOException {
        User user = new User();
        user.setUsername("joveleex");
        user.setPassword("123456");
        Assertions.assertThat(jacksonTester.write(user)).isEqualToJson("/user.json");
        Assertions.assertThat(jacksonTester.write(user)).hasJsonPathStringValue("@.username");
        Assertions.assertThat(jacksonTester.write(user)).extractingJsonPathStringValue("@.username").isEqualTo("joveleex");
    }

    @Test
    public void testDeserialize() throws IOException {
        String content = "{\"username\": \"joveleex\", \"password\": \"123456\"}";
        Assertions.assertThat(jacksonTester.parseObject(content).getUsername()).isEqualTo("joveleex");
    }
}
