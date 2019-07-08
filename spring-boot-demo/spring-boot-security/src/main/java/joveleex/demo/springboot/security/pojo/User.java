package joveleex.demo.springboot.security.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体类
 */
@Data
public class User implements UserDetails {

    /**
     * ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否不可用
     */
    private Boolean disabled;

    /**
     * 是否被锁定
     */
    private Boolean locked;

    /**
     * 角色信息
     */
    private List<Role> roles;

    /**
     * 获取当前用户对象所具有的角色信息
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    /**
     * 当前账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        // 当前演示项目并没有能标识一个用户是否过期的业务逻辑，一直返回true
        return true;
    }

    /**
     * 当前账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        // 当前演示项目通过数据库locked字段标识用户是否锁定，返回!locked
        return !locked;
    }

    /**
     * 当前账户密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // 当前演示项目并没有能标识一个用户账密是否过期的业务逻辑，一直返回true
        return true;
    }

    /**
     * 当前账户是否可用
     */
    @Override
    public boolean isEnabled() {
        // 当前演示项目通过数据库disabled字段标识用户是否可用，返回!disabled
        return !disabled;
    }
}
