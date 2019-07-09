# 基本用法

添加devtools的依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
```

当classpath下的文件（不包括静态文件）发生变化时，SpringBoot会自动热部署

Eclipse修改保存文件后会自动编译文件，从而使classpath下的文件发生变化触发热部署

Intellij IDEA默认修改保存文件并不会自动编译文件，需要手动Build项目或Recompile文件



# Intellij IDEA 自动构建项目

Intellij IDEA也可以设置自动构建项目

- 菜单栏 File - Settings 打开设置窗口
- 进入到 Build,Execution,Deployment - Compiler 页面，勾选 Build project automatically
- Ctrl + Shift + Alt + / 打开Maintenance - Registry，勾选compiler.automake.allow.when.app.running



# 静态文件的热部署

默认情况下，/META-INF/maven、/META-INF/resources、/resources、/static、/public、/templates

位置下的资源发生变化不会触发热部署

如果希望热部署某一目录下的资源，可以在配置文件中配置

```properties
# 不触发热部署的目录中去除static目录，即classpath:static目录下的资源发生变化将触发热部署
spring.devtools.restart.exclude=static/**
```

或者也可以这样配置

```properties
# 触发热部署的目录中添加额外的static目录
spring.devtools.restart.additional-paths=src/main/resources/static
```



# LiveReload热部署静态文件

devtools默认嵌入了LiveReload服务器，可以在静态资源发生变化时自动触发浏览器更新

需要安装相应浏览器的LiveReload插件，安装完插件开启LiveReload连接即可

当不想使用LiveReload热部署静态文件时，可以在配置文件中配置

```properties
# 不启用LiveReload热部署
spring.devtools.livereload.enabled=false
```



# 触发文件方式的热部署

每次修改文件就会自动触发热部署，这样热部署可能会非常频繁

如果希望手动触发热部署，可以在配置文件中配置

```properties
# 项目resources目录下创建.trigger-file文件，当希望触发热部署时，修改该文件即可
spring.devtools.restart.trigger-file=.trigger-file
```



# 禁用热部署

添加了spring-boot-devtools但并不想使用热部署，可以在配置文件中配置
```properties
# 不启用热部署重启
spring.devtools.restart.enabled=false
```

也可以在项目启动前设置系统属性
```java
@SpringBootApplication
public class SpringBootDevtoolsApplication {
  
  public static void main(String[] args) {
    System.setProperty("spring.devtools.restart.enabled", "false");
    SpringApplication.run(SpringBootDevtoolsApplication.class, args);
  }
}
```



# Devtools的全局配置

在当前系统登录用户的home目录下创建.spring-boot-devtools.properties文件配置全局配置

该配置文件会应用于所有依赖了spring-boot-devtools构件的项目



# 相关引用

《Spring Boot + Vue 全栈开发实战》