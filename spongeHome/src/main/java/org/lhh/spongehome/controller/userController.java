package org.lhh.spongehome.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lhh.spongehome.common.BaseResponse;
import org.lhh.spongehome.common.ErrorCode;
import org.lhh.spongehome.common.ResultUtils;
import org.lhh.spongehome.exception.BusinessException;
import org.lhh.spongehome.model.DTO.user.AddUserRequest;
import org.lhh.spongehome.model.DTO.user.LoginUserRequest;
import org.lhh.spongehome.model.VO.UserVO;
import org.lhh.spongehome.utils.JwtTokenprovider;
import org.lhh.spongehome.model.entity.User;
import org.lhh.spongehome.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)控制用户的操作受登录限制
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户服务")
public class userController {

    @Resource
    private UserService userServiceImpl;

    @Resource
    private JwtTokenprovider jwtTokenprovider;

    /**
     * 用户登录
     * @param user
     * @param HttpServletRequest
     * @return
     */
    @Operation(summary = "LoginUserRequest登录请求")
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody LoginUserRequest user, HttpServletRequest HttpServletRequest) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接收的数据为空");
        }
        String nameAccount = user.getNameAccount();
        String password =  user.getPassword();
        User userCheckAfter=null;
        if(StringUtils.isAnyBlank(nameAccount, password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码有为空的");
        }
        if(user.getRole()=="admin"){
            userCheckAfter = userServiceImpl.getLoginAdmin(nameAccount, password, HttpServletRequest);
        }
        if(user.getRole()=="user"){
            userCheckAfter = userServiceImpl.login(nameAccount, password, HttpServletRequest);
        }

        String token    =jwtTokenprovider.generateToken(userCheckAfter);
        if(token==null){
            log.warn("token获取失败");
        }
        UserVO uservo = new UserVO();
        BeanUtils.copyProperties(userCheckAfter, uservo);
        uservo.setToken(token);

        return ResultUtils.success(uservo);
    }


    /**
     * 用户注册
     * @param addUserRequest
     * @return
     */
    @Operation(summary = "AddUserRequest注册请求")
    @PostMapping("/zhuce")
    public BaseResponse<User> zhuce(@RequestBody AddUserRequest addUserRequest) {
        System.out.println(addUserRequest);
        User zhuceAfter = userServiceImpl.userzhuce(addUserRequest);
        if(zhuceAfter==null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(zhuceAfter);
    }
    @GetMapping("/get/loginUser")
    public BaseResponse<UserVO> getLoginUser( HttpServletRequest request) {

        User user = userServiceImpl.getLoginUser(request);
        if(user==null){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        return ResultUtils.success(userServiceImpl.getLoginUserVO(user));
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @GetMapping("/deleteByUserId")
    public BaseResponse<Boolean> deleteUserId(@RequestParam("userId") Integer userId) {
        boolean delete = userServiceImpl.removeById(userId);
        return ResultUtils.success(delete);
    }
}
