package org.lhh.spongehome.model.DTO.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加用户请求
 */
@Data
public class AddUserRequest implements Serializable {
   private String nameAccount;
   private String password;
   private String checkPassword;
   private String role;


}
