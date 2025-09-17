package org.lhh.spongehome.model.VO;

import lombok.Data;

/**
 * 用户返回视图
 */
@Data
public class UserVO {

    /**
     * 用户 id
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户账号
     */
    private String nameAccount;
    /**
     * 用户头像
     */
    private String photo;
    /**
     * 用户电话
     */
    private Long iphone;
    /**
     * 用户token
     */
    private String token;

    /**
     * 用户角色
     */
    private String role;


    @Override
    public String toString() {
        return "UserVO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", nameAccount='" + nameAccount + '\'' +
                ", photo='" + photo + '\'' +
                ", iphone=" + iphone +
                ", token='" + token + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
