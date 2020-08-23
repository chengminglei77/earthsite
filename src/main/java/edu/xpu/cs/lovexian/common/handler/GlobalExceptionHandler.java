package edu.xpu.cs.lovexian.common.handler;

import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.exception.FileDownloadException;
import edu.xpu.cs.lovexian.common.exception.LimitAccessException;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.common.response.RespStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author huchengpeng
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /**
     * 使用@RequestBody时此处生效
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public EarthSiteResponse validExceptionHandler(MethodArgumentNotValidException e){
        FieldError fieldError = e.getBindingResult().getFieldError();
        StringBuilder sb = new StringBuilder();
        assert fieldError != null;
        log.error(fieldError.getField() + ":" + fieldError.getDefaultMessage());
        sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append("#");
        return EarthSiteResponse.FAIL().message(sb.toString());
    }

    @ExceptionHandler(value = Exception.class)
    public EarthSiteResponse handleException(Exception e) {
        log.error("系统内部异常，异常信息", e);
        return EarthSiteResponse.FAIL().message("系统内部异常");
    }

    @ExceptionHandler(value = EarthSiteException.class)
    public EarthSiteResponse handleLoveXianException(EarthSiteException e) {
        log.error("系统错误", e);
        return EarthSiteResponse.FAIL().message(e.getMessage());
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return EarthSiteResponse
     */
    @ExceptionHandler(BindException.class)
    public EarthSiteResponse validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return EarthSiteResponse.FAIL().status(RespStatus.paramsError.getCode()).message(message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return EarthSiteResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public EarthSiteResponse handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return EarthSiteResponse.FAIL().status(RespStatus.paramsError.getCode()).message(message.toString());
    }

    @ExceptionHandler(value = {LimitAccessException.class, UndeclaredThrowableException.class})
    public Map handleLimitAccessException(LimitAccessException e) {
        log.debug("LimitAccessException", e);
        Map<String,Object> res = new HashMap<>();
        res.put("message","限流");
        res.put("code",RespStatus.limitError.getCode());
        return res;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public EarthSiteResponse handleUnauthorizedException(UnauthorizedException e) {
        log.debug("UnauthorizedException", e);
        return EarthSiteResponse.FAIL().message(e.getMessage());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public EarthSiteResponse handleAuthenticationException(AuthenticationException e) {
        log.debug("AuthenticationException", e);
        return EarthSiteResponse.FAIL().message(e.getMessage());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public EarthSiteResponse handleAuthorizationException(AuthorizationException e){
        log.debug("AuthorizationException", e);
        return EarthSiteResponse.FAIL().message(e.getMessage());
    }


    @ExceptionHandler(value = ExpiredSessionException.class)
    public EarthSiteResponse handleExpiredSessionException(ExpiredSessionException e) {
        log.debug("ExpiredSessionException", e);
        return EarthSiteResponse.FAIL().message(e.getMessage());
    }

    @ExceptionHandler(value = FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleFileDownloadException(FileDownloadException e) {
        log.error("FileDownloadException", e);
    }
}
