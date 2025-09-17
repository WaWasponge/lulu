package org.lhh.spongehome.AOP;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lhh.spongehome.annotation.AuthCheck;
import org.lhh.spongehome.common.ErrorCode;
import org.lhh.spongehome.exception.BusinessException;
import org.lhh.spongehome.model.entity.User;
import org.lhh.spongehome.model.enums.UserRoleEnum;
import org.lhh.spongehome.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class AuthInterceptor  {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint , AuthCheck authCheck) throws Throwable {

        String mustRule= authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        //当前登录的用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRole = UserRoleEnum.getEnum(mustRule);
        if(mustRole==null){
           return joinPoint.proceed();
        }

        UserRoleEnum loginUserRole = UserRoleEnum.getEnum(loginUser.getRole());
        //没权限，拒绝
        if(loginUserRole==null){
             throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //要求必须是管理员
        if(UserRoleEnum.ADMIN.equals(mustRole)&&  !UserRoleEnum.ADMIN.equals(loginUserRole))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return joinPoint.proceed();
    }
}
