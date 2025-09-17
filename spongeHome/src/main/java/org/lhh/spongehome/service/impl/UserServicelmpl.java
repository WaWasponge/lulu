package org.lhh.spongehome.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.lhh.spongehome.common.ErrorCode;
import org.lhh.spongehome.exception.BusinessException;
import org.lhh.spongehome.exception.RateLimitException;
import org.lhh.spongehome.model.DTO.user.AddUserRequest;
import org.lhh.spongehome.model.VO.UserVO;
import org.lhh.spongehome.model.enums.UserRoleEnum;
import org.lhh.spongehome.utils.RedisRateLimiter;
import org.lhh.spongehome.model.entity.User;
import org.lhh.spongehome.mapper.UserMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.lhh.spongehome.constant.UserConstant.USER_LOGIN_STATE;


/**
* @author sponge
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-05-31 23:54:40
*/
@Slf4j
@Service
public class UserServicelmpl extends ServiceImpl<UserMapper, User> implements org.lhh.spongehome.service.UserService {

//    private static final Logger log= LoggerFactory.getLogger(UserServicelmpl.class);
    // 注入RedisTemplate
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisRateLimiter ipLoginRateLimiter;
    private final RedisRateLimiter accountLoginRateLimiter;

    public UserServicelmpl(
            RedisTemplate<String, String> redisTemplate,
            @Qualifier("ipLoginRateLimiter")   RedisRateLimiter ipLoginRateLimiter,
            @Qualifier("accountLoginRateLimiter")    RedisRateLimiter accountLoginRateLimiter) {
        this.redisTemplate = redisTemplate;
        this.ipLoginRateLimiter = ipLoginRateLimiter;
        this.accountLoginRateLimiter = accountLoginRateLimiter;
    }

    /**
     * 注册
      * 密码只使用了一次的BCryptPasswordEncoder
     *
     * @return
     */
    @Override
    public User userzhuce(AddUserRequest addUserRequest) {
        String nameAccount = addUserRequest.getNameAccount();
        String password = addUserRequest.getPassword();
        String checkPassword = addUserRequest.getCheckPassword();
      //校验
        System.out.println(nameAccount+password+checkPassword+"!!!");
       if(!StringUtils.isAnyBlank(nameAccount, password, checkPassword))
       {
//      账户不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.eq(User::getNameAccount, nameAccount);
        long count = this.count(queryWrapper);
        if(count > 0 ){
            log.error( "{}用户已存在",  nameAccount);
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"用户已存在");
        }
//       检查密码是否相同
        if( !password.equals(checkPassword))
        {
            log.error( "密码不一致");
//            return null;
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"密码不一致");
        }
//        加密
           PasswordEncoder  passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
           String encodedPassword = passwordEncoder.encode(password);
           User user = new User();
            user.setNameAccount(nameAccount);
             user.setPassWord(encodedPassword);
             user.setRole(UserRoleEnum.USER.getValue());
           user.setUserName(String.valueOf(Math.random()));
           user.setPhoto("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg");

             boolean result =this.save(user);
             if( result){
                 return user;
          }
        log.error( "{}用户保存失败",  nameAccount);
           throw new BusinessException(ErrorCode.OPERATION_ERROR);
       }
       log.error( "{}用户注册参数错误",  nameAccount);
        throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }

    /**
     * 密码只使用了一次的BCryptPasswordEncoder
     * 用户登录 有限流了
     * @param nameAccount
     * @param password
     * @return
     */
    @Override
    public User login(String nameAccount, String password, HttpServletRequest request) {
        // 调用 getCurrentRequestIp 方法获取客户端 IP
        String clientIp = getCurrentRequestIp(request);
        if (clientIp == null || clientIp.isEmpty()) {
            log.warn("无法获取客户端IP，跳过限流逻辑");
            return null;
        }
        if ( !StringUtils.isAnyBlank(nameAccount, password)) {
//            // 1. 限流逻辑（示例：每分钟最多5次请求）
            // ===== IP级别限流检查 =====
            // 尝试从IP限流桶中获取令牌
            if(!ipLoginRateLimiter.tryAcquire(clientIp))
            {
                log.warn("账号 :{} 从IP :{} 已超过限流阈值", nameAccount, clientIp);
                throw new RateLimitException("登录尝试过于频繁，请稍后再试",10);
            }
            // ===== 账号级别限流检查 =====
            // 尝试从账号限流桶中获取令牌
            if (!accountLoginRateLimiter.tryAcquire(nameAccount)) {
                // 记录警告日志：账号触发限流
                log.warn("账号登录限流触发: {}", nameAccount);
                // 抛出限流异常
                throw new RateLimitException("该账号登录尝试过多，已被暂时限制",150);
            }
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getNameAccount, nameAccount);
            User user = this.getOne(queryWrapper);
            if(user.getRole()!="user"){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"你是什么东西？");
            }
//            queryWrapper.clear();
            if (user != null) {
                //校验密码
                PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//                  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
                boolean matches = passwordEncoder.matches(
                        password, user.getPassWord()
                );
                if (matches) {
                    // 登录成功时清除限流记录
                    String ipRateLimitKey = ipLoginRateLimiter.getKeyPrefix() + clientIp;
                    String accountRateLimitKey = accountLoginRateLimiter.getKeyPrefix() + nameAccount;
                    redisTemplate.delete(ipRateLimitKey);
                    redisTemplate.delete(accountRateLimitKey);//
                    log.info("登录成功");
                    // 记录用户的登录态
                    request.getSession().setAttribute(USER_LOGIN_STATE,  user);
                    return user;
                }
                log.info("用户名{}密码错误",  nameAccount);
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"密码错误");
            }
            log.error("哈吉米，你的用户名错误");
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"请输入正确的哈吉名");
        }
        log.error("登录失败");
        return null;
    }
    @Override
    public User getLoginAdmin(String nameAccount, String password,HttpServletRequest httpServletRequest) {
        User user = new User();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNameAccount, nameAccount);
        user = this.getOne(queryWrapper);
        PasswordEncoder  passwordEncoder = new BCryptPasswordEncoder();
        if(user.getRole()!="admin"){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"请使用正确的管理员账号登录，小黑子！");
        }
        if(user == null ){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "黑子的用户名错误");
        }
        if(passwordEncoder.matches(password, user.getPassWord())){
            log.info("{}admin登录成功",  nameAccount);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return user;
        }
    throw  new BusinessException(ErrorCode.PARAMS_ERROR, "黑子的密码错误");
    }

    @Override
    public UserVO getLoginUserVO(User user){
        if(user== null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取当前登录用户
     * 该方法用于从HTTP请求中获取当前已登录的用户信息
     * 主要功能：
     * 1. 从session中获取用户登录状态
     * 2. 验证用户是否已登录
     * 3. 从数据库中获取最新的用户信息
     *
     * @param request HTTP请求对象，用于获取session中的用户信息
     * @return 返回当前登录的User对象
     * @throws BusinessException 当用户未登录或用户信息不存在时抛出异常
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 从session中获取用户登录状态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 验证用户是否已登录
        if (currentUser == null || currentUser.getUserId() == null) {
          return null;
        }
        // 从数据库查询最新的用户信息（可以注释掉以提高性能，直接使用session中的缓存数据）
        long userId = currentUser.getUserId();
        currentUser = this.getById(userId);
        // 验证用户信息是否存在
        if (currentUser == null) {
            return null;
        }
        return currentUser;
    }

    // 获取客户端IP的方法
    private String getCurrentRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
