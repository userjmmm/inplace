package team7.inplace.admin.user.application.dto;

import user.AdminUser;
import user.Role;

public record AdminUserInfo(
    Long id,
    String username,
    String password,
    Role role
) {

    public static AdminUserInfo of(AdminUser adminUser) {
        return new AdminUserInfo(
            adminUser.getId(),
            adminUser.getUsername(),
            adminUser.getPassword(),
            adminUser.getRole()
        );
    }
}
