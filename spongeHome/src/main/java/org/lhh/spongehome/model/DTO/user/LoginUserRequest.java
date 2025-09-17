package org.lhh.spongehome.model.DTO.user;

import lombok.Data;

/**
 * 登录用户请求
 */
@Data
public class LoginUserRequest {
    private String nameAccount;
    private String password;
    private String role;
}
