package joveleex.demo.springboot.security.mapper;

import joveleex.demo.springboot.security.pojo.Role;
import joveleex.demo.springboot.security.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    User loadUserByUsername(String username);

    List<Role> getUserRolesByUid(Integer id);
}
