package joveleex.demo.springboot.security.pojo;

import lombok.Data;

/**
 * 角色实体类
 */
@Data
public class Role {

    /**
     * ID
     */
    private Integer id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色名（中文）
     */
    private String nameZh;
}
