package com.nexos.nexos_admin.po;

import java.util.Date;
import lombok.Data;

@Data
public class RolePermission {
    /**
     * ID
     */
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改时间
     */
    private Date edited;

    /**
     * 修改人
     */
    private String editor;

    /**
     * 逻辑删除:0=未删除,1=已删除
     */
    private Boolean deleted;
}

