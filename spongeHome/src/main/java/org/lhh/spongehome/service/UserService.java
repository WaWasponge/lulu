package org.lhh.spongehome.service;

import jakarta.servlet.http.HttpServletRequest;
import org.lhh.spongehome.model.DTO.user.AddUserRequest;
import org.lhh.spongehome.model.VO.UserVO;
import org.lhh.spongehome.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 19589
* @description 针对表【user】的数据库操作Service
* @createDate 2025-05-31 23:54:40
*/
public interface UserService extends IService<User> {

    /**
     *注册校验
     * @return
     */
    User userzhuce(AddUserRequest addUserRequest);

    /**
     *
     * 用户登录校验
     * @param nameAccount
     * @param password
     * @return
     */
    User login(String nameAccount, String password, HttpServletRequest request);

    UserVO getLoginUserVO(User user);

    User getLoginUser(HttpServletRequest request) ;

    /**
     * 管理员登录校验
     * @param nameAccount
     * @param password
     * @param request
     * @return
     */
    User getLoginAdmin(String nameAccount, String password,HttpServletRequest request);
}
