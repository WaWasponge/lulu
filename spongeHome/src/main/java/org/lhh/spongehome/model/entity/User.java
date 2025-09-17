package org.lhh.spongehome.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */

@TableName(value ="user")
@Data
public class User {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 电话
     */
    private Long iphone;

    /**
     * 头像照片
     */
    private String photo;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 数据插入时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除 1启用 0弃用
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 角色
     */
    private String role;
    /**
     *  登陆账号
     */
    @TableField(value = "name_account")
    private String nameAccount;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", iphone=" + iphone +
                ", photo='" + photo + '\'' +
                ", passWord='" + passWord + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDelete=" + isDelete +
                ", role='" + role + '\'' +
                ", nameAccount='" + nameAccount + '\'' +
                '}';
    }
}