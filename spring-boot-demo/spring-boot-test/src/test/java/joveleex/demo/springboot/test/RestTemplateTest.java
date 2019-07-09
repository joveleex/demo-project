package joveleex.demo.springboot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestTemplateTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testWithRestTemplate() {
        ResponseEntity<String> result = testRestTemplate.getForEntity("/hello?name={0}", String.class, "joveleex");
        System.out.println(result.getBody());
    }
}
