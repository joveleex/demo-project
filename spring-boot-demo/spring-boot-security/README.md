# Spring Security的基本使用

通过Spring Security来保护项目的资源，即当用户发起请求时，对用户进行认证，使用户只能访问其有权限访问的资源

## 添加依赖

创建SpringBoot项目，并添加以下依赖
只要项目中添加了spring-boot-starter-security依赖，项目中所有资源都会被保护起来
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## 添加hello接口

添加hello接口并启动服务，请求localhost:8080/hello访问项目内的资源
由于项目中的资源已经被Spring Security保护起来，需要对用户先认证，会自动跳转到Spring Security提供的登录页
```
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
```

## 通过认证

Spring Security提供了默认的用户名user，项目启动时日志中打印了密码，输入账密通过认证后自动请求hello接口，获取到响应的资源
也可以使用自定义的账密，在application.yml中配置自定义的账密，默认的账密将失效，且日志不再打印密码
```
spring:
  security:
    user:
      name: test
      password: 123456
      roles: admin
```

# 基于内存的认证

继承WebSecurityConfigurerAdapter重写方法来自定义具体的认证规则
以下代码在内存中创建了root、admin、joveleex三个用户和相应的密码，并分配了不通的角色
具体配置信息以及资源访问控制详见WebSecurityConfigUseMemoryAuth
```
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
            .withUser("root").password("123456").roles("ADMIN", "DBA")
            .and()
            .withUser("admin").password("123456").roles("ADMIN", "USER")
            .and()
            .withUser("joveleex").password("123456").roles("USER");
}
```

# 基于数据库的认证

基于内存的认证，用户数据都被事先创建在内存中了，而基于数据库的认证，用户信息都存在于数据库中，需要查询数据库并告知Spring Security
Spring Security提供了UserDetailsService接口来获取查询数据库得到的用户信息
但Spring Security不知道查询出的用户信息是什么类型的，所以Spring Security提供了UserDetails接口来规范用户信息的类型
因此UserDetailsService.loadUserByUsername返回值为UserDetails

所以需要编写一个类实现UserDetailsService接口，编写另一个类实现UserDetails接口
并在重写的loadUserByUsername方法中查询用户信息并将信息封装到UserDetails的实现类中并返回

由于本例中使用User实现UserDetails，因此查询出用户信息后直接返回

具体代码见UserService和User
具体配置信息以及资源访问控制详见WebSecurityConfigUseDbAuth

# 基于注解方式的安全配置

通过在WebSecurityConfig类上添加@EnableGlobalMethodSecurity开启注解方式的认证配置

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
prePostEnabled 开启@PreAuthorize @PostAuthorize注解的使用
@PreAuthorize 方法执行前验证
@PostAuthorize 方法执行后验证
securedEnabled 开启@Secured注解的使用

具体使用如下
```
@Service
public class MethodService {
    
    @Secured("ROLE_ADMIN")
    public String admin() {
        return "hello admin";
    }
    
    @PreAuthorize("hasRole('ADMIN') and hasRole('DBA')")
    public String dba() {
        return "hello dba";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'DBA', 'USER')")
    public String user() {
        return "hello user";
    }
}
```