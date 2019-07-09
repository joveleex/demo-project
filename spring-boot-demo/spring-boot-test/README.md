# 基本用法

添加spring-boot-starter-test依赖
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
```



添加了依赖创建SpringBoot项目时会自动创建测试类

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTestApplicationTests {
  
    @Test
    public void contextLoads() {
    }
}
```

> - @RunWith(SpringRunner.class)：会将JUnit执行类修改为SpringRunner
>
> - @SpringBootTest：提供SpringTestContext中的常规测试功能之外，还提供默认的ContextLoader、自动搜索@SpringBootConfiguration、自定义环境属性、为不通的webEnvironment模式提供支持
>   - MOCK：当classpath下存在servletAPIS时，创建WebApplicationContext并提供mockServlet环境，
>     当classpath下存在SpringWebFlux时，创建ReactiveWebApplicationContext
>     若都不存在，创建常规的ApplicationContext
>   - RANDOM_PORT：提供真实的Servlet环境，使用内嵌容器，端口随机
>   - DEFINED_PORT：提供真实的Servlet环境，使用内嵌容器，端口固定
>   - NONE：加载普通的ApplicationContext，不提供Servlet环境，一般用于非Web测试

- Spring测试中，一般使用@ContextConfiguration(classes=)或@ContextConfiguration(locations=)来指定要加载的Spring配置
- 在SpringBoot中不需要这些操作，SpringBoot的@*Test注解将会去包含测试类的包下查找带有@SpringBootApplication或者@SpringBootConfiguration注解的主配置类



# 测试Controller

对Controller进行测试需要模拟请求，SpringBoot提供了MockMvc来模拟HTTP请求

```java
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
```

```java
@Data
public class User {

    private String username;

    private String password;
}
```

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void before() {
      	// 通过WebApplicationContext来构建模拟的ServletContext环境
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testHello() throws Exception {
        MvcResult mvcResult = mockMvc
          			// 构建GET请求，路径/hello，类型application/x-www-form-urlencoded，参数name
                .perform(MockMvcRequestBuilders
                        .get("/hello")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "joveleex"))
          			// 校验响应码是否为200
                .andExpect(MockMvcResultMatchers.status().isOk())
          			// 控制台打印请求详细信息
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
      	// 控制台打印接口返回的内容
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        user.setUsername("joveleex");
        user.setPassword("123456");
        String userJson = JSON.toJSONString(user);
        MvcResult mvcResult = mockMvc
          			// 构建POST请求，路径/user，类型application/json，参数userJson
                .perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
			          // 校验响应码是否为200
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // 控制台打印接口返回的内容
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}

```



除了使用MockMvc测试，SpringBoot还提供了TestRestTemplate来以RestTemplate的方式对Controller测试

需要将@SpringBootTest的webEnvironment属性值改为DEFINED_PORT或RANDOM_PORT

将使用一个真实的Servlet环境而不是MOCK提供的模拟的Servlet环境

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestTemplateTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testWithRestTemplate() {
        ResponseEntity<String> result = testRestTemplate
          .getForEntity("/hello?name={0}", String.class, "joveleex");
        System.out.println(result.getBody());
    }
}
```



# 测试JSON

> 通过@JsonTest可以测试JSON序列化和反序列化是否工作正常
>
> @JsonTest会自动配置Jackson ObjectMapper、@JsonComponent以及Jackson Modules
>
> 如果使用Gson代替Jackson，该注解将配置Gson

```java
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
      	// 序列化当前User对象并同classpath下的user.json做比对
        Assertions
          .assertThat(jacksonTester.write(user))
          .isEqualToJson("/user.json");
      	// 序列化当前User对象并判断生成的JSON中是否存在名叫username的key
      	Assertions
          .assertThat(jacksonTester.write(user))
          .hasJsonPathStringValue("@.username");
      	// 序列化当前User对象并判断生成的JSON中名叫username的key对应的value是否为joveleex
        Assertions
          .assertThat(jacksonTester.write(user))
          .extractingJsonPathStringValue("@.username")
          .isEqualTo("joveleex");
    }

    @Test
    public void testDeserialize() throws IOException {
        String content = "{\"username\": \"joveleex\", \"password\": \"123456\"}";
      	// 反序列化JSON数据并判断对象的username是否为joveleex
        Assertions
          .assertThat(jacksonTester.parseObject(content).getUsername())
          .isEqualTo("joveleex");
    }
}

```



# 相关引用

《Spring Boot + Vue 全栈开发实战》