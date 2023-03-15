package cn.tedu.csmall.product1.ex.hander;

import cn.tedu.csmall.product1.ex.ServiceException;
import cn.tedu.csmall.product1.web.JsonResult;
import cn.tedu.csmall.product1.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
        log.debug("创建全局异常处理器类对象：GlobalExceptionHandler");
    }
    @ExceptionHandler
    public JsonResult<Void> handleServiceException(ServiceException e){
        log.warn("异常信息:{}",e.getMessage());
        return JsonResult.fail(e);
    }
    @ExceptionHandler
    public JsonResult<Void> handleBindException(BindException e) {
        log.warn("程序运行过程中出现BindException，将统一处理！");
        log.warn("异常信息：{}", e.getMessage());
        String message = e.getFieldError().getDefaultMessage();
        return JsonResult.fail(ServiceCode.ERROR_BAD_REQUEST, message);
    }

    @ExceptionHandler
    public JsonResult<Void> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("程序运行过程中出现handleConstraintViolationException，将统一处理！");
        log.warn("异常信息：{}", e.getMessage());
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            stringBuilder.append(constraintViolation.getMessage());
        }
        String message = stringBuilder.toString();
        return JsonResult.fail(ServiceCode.ERROR_BAD_REQUEST, message);
    }

    @ExceptionHandler({
            InternalAuthenticationServiceException.class,
            BadCredentialsException.class
    })
    public JsonResult<Void> handleAuthenticationException(AuthenticationException e) {
        log.warn("程序运行过程中出现AuthenticationException，将统一处理！");
        log.warn("异常类型：{}", e.getClass().getName());
        log.warn("异常信息：{}", e.getMessage());
        String message = "登录失败，用户名或密码错误！";
        return JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED, message);
    }

    @ExceptionHandler
    public JsonResult<Void> handleDisabledException(DisabledException e) {
        log.warn("程序运行过程中出现DisabledException，将统一处理！");
        log.warn("异常信息：{}", e.getMessage());
        String message = "登录失败，账号已经被禁用！";
        return JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED_DISABLED, message);
    }

    @ExceptionHandler
    public JsonResult<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("程序运行过程中出现AccessDeniedException，将统一处理！");
        log.warn("异常信息：{}", e.getMessage());
        String message = "拒绝访问，您当前登录的账号无此操作权限！";
        return JsonResult.fail(ServiceCode.ERROR_FORBIDDEN, message);
    }
    @ExceptionHandler
    public JsonResult<Void> handleThrowable(Throwable e) {
        log.warn("程序运行过程中出现Throwable，将统一处理！");
        log.warn("异常类型：{}", e.getClass());
        log.warn("异常信息：{}", e.getMessage());
        String message = "服务器忙，请稍后再次尝试！（开发过程中，如果看到此提示，请检查控制台的信息，并补充处理异常的方法）";
        e.printStackTrace();
        return JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
    }
}
