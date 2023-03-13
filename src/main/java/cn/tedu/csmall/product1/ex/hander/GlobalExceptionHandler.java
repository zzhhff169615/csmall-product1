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
        // StringBuilder stringBuilder = new StringBuilder();
        // List<FieldError> fieldErrors = e.getFieldErrors();
        // for (FieldError fieldError : fieldErrors) {
        //     stringBuilder.append(fieldError.getDefaultMessage());
        // }
        // String message = stringBuilder.toString();
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

    // 注意：以下方法存在的意义主要在于：避免因为某个异常未被处理，导致服务器端响应500错误
    // 注意：e.printStackTrace()通常是禁止使用的，因为其输出方式是阻塞式的！
    //      以下方法中使用了此语句，是因为用于发现错误，并不断的补充处理对应的异常的方法
    //      随着开发进度的推进，执行到以下方法的概率会越来越低，
    //      出现由于此语句导致的问题的概率也会越来越低，
    //      甚至补充足够多的处理异常的方法后，根本就不会执行到以下方法了
    //      当项目上线后，可以将此语句删除
    @ExceptionHandler
    public JsonResult<Void> handleThrowable(Throwable e) {
        log.warn("程序运行过程中出现Throwable，将统一处理！");
        log.warn("异常类型：{}", e.getClass());
        log.warn("异常信息：{}", e.getMessage());
        String message = "服务器忙，请稍后再次尝试！（开发过程中，如果看到此提示，请检查控制台的信息，并补充处理异常的方法）";
        // String message = "服务器忙，请稍后再尝试！"; // 项目上线时应该使用此提示文本
        e.printStackTrace(); // 打印异常的跟踪信息，主要是为了在开发阶段更好的检查出现异常的原因
        return JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
    }
}
