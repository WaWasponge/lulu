package org.lhh.spongehome.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {

    ADMIN("管理员", "admin"),
    USER("普通用户", "user");
    private String value;
    private String text;

    UserRoleEnum(String text, String value) {
        this.value = value;
        this.text = text;
    }
    public static UserRoleEnum getEnum(String value) {
        if(ObjUtil.isEmpty( value)){
            return null;
        }
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.getValue().equals(value)) {
                return userRoleEnum;
            }
        }
        return null;
    }
}
