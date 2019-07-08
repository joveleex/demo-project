package joveleex.demo.springboot.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// 当前Demo项目采用数据库的方式认证，因此注释掉@Configuration
//@Configuration
public class WebSecurityConfigUseMemoryAuth extends WebSecurityConfigurerAdapter {

    /**
     * spring security 5.X 版本开始必须要为密码加密，需要实例化PasswordEncoder，否则会抛出异常
     * java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
     */
//    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 通过auth.inMemoryAuthentication方法在内存中创建了root、admin、joveleex三个认证用户
     * 分别为root、admin、joveleex三个账户设置密码并分配用户所属角色
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("root").password("123456").roles("ADMIN", "DBA")
                .and()
                .withUser("admin").password("123456").roles("ADMIN", "USER")
                .and()
                .withUser("joveleex").password("123456").roles("USER");
    }

    /**
     * 通过http.authorizeRequests开始配置请求的处理规则
     * .antMatchers("/admin/**").hasRole("ADMIN")   表示访问/admin/**格式的路径的资源需要用户有ADMIN角色
     * .antMatchers("/user/**").access("hasAnyRole('ADMIN', 'USER')")   表示访问/user/**格式的路径的资源需要用户有ADMIN或是USER角色
     * .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')") 表示访问/db/**格式的路径的资源需要用户有ADMIN和DBA两个角色
     * .anyRequest().authenticated()    表示任何请求都需要认证才能访问资源
     * .and().formLogin().loginPage("/login_page").loginProcessingUrl("/login")  开启表单登录，默认登录页为SpringSecurity提供的页面
     *      通过loginPage定义请求自定义登录页面的接口的路径，通过loginProcessingUrl定义执行登录操作的接口的路径（登录接口由框架实现）
     * .usernameParameter("name").passwordParameter("passwd") 定义登录的用户名、密码的请求参数，默认为username和password
     * .successHandler()    定义认证处理成功的响应，这里以响应JSON信息为例，第三个Authentication参数可以获取到用户信息
     * .failureHandler()    定义认证处理失败的响应，这里以响应JSON信息为例，第三个AuthenticationException参数可以获取异常信息
     * .permitAll() 表示和登录相关的接口都不需要认证
     * .and().logout()  开启注销登录
     * .logoutUrl("/logout").clearAuthentication(true).invalidateHttpSession(true)  注销登录请求的URL，以及是否清除认证信息、是否使Session失效
     * .addLogoutHandler()  定义注销处理时做的操作，例如可以清除Cookie
     * .logoutSuccessHandler()  定义注销后做的操作
     * .and().csrf().disable()  关闭csrf
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .antMatchers("/user/**")
                .access("hasAnyRole('ADMIN', 'USER')")
                .antMatchers("/db/**")
                .access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login_page")
                .loginProcessingUrl("/login")
                .usernameParameter("name")
                .passwordParameter("passwd")
                .successHandler((req, resp, auth) -> {
                    Object principal = auth.getPrincipal();
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    resp.setStatus(200);
                    Map<String, Object> map = new HashMap<>();
                    map.put("status", 200);
                    map.put("msg", principal);
                    ObjectMapper om = new ObjectMapper();
                    out.write(om.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .failureHandler((req, resp, auth) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    resp.setStatus(401);
                    Map<String, Object> map = new HashMap<>();
                    map.put("status", 401);
                    if (auth instanceof LockedException) {
                        map.put("msg", "账户被锁定，登录失败！");
                    } else if (auth instanceof BadCredentialsException) {
                        map.put("msg", "账户名或密码输入错误，登录失败！");
                    } else if (auth instanceof DisabledException) {
                        map.put("msg", "账户被禁用，登录失败！");
                    } else if (auth instanceof AccountExpiredException) {
                        map.put("msg", "账户已过期，登录失败！");
                    } else if (auth instanceof CredentialsExpiredException) {
                        map.put("msg", "密码已过期，登录失败！");
                    } else {
                        map.put("msg", "登录失败！");
                    }
                    ObjectMapper om = new ObjectMapper();
                    out.write(om.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .addLogoutHandler((req, resp, auth) -> {
                })
                .logoutSuccessHandler((req, resp, auth) -> {
                    resp.sendRedirect("/login_page");
                })
                .and()
                .csrf()
                .disable();
    }
}
