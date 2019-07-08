package joveleex.demo.springboot.cache.ehcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringBootCacheEhcacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCacheEhcacheApplication.class, args);
	}

}
