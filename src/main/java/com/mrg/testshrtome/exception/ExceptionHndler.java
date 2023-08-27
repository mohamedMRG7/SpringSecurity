package com.mrg.testshrtome.exception;

import com.mrg.testshrtome.dtos.BaseResponse;
import com.mrg.testshrtome.enums.StatusEnum;
import com.mrg.testshrtome.exception.customexceptions.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ExceptionHndler {

    @ExceptionHandler({AuthException.class})
    public ResponseEntity <BaseResponse> exceptionResponse(Exception exception, HttpServletRequest request)
    {
        BaseResponse res =BaseResponse.builder().error(exception.getMessage()).status(StatusEnum.FAILED).requestId("id").build();
        log.error("AuthException"+exception.getMessage());
        return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity <BaseResponse> handlCommonException(RuntimeException exception,HttpServletRequest request)
    {
        BaseResponse res =BaseResponse.builder().error(exception.getMessage()).status(StatusEnum.FAILED).requestId("id").build();
        log.error("Runtime",exception.getMessage());
        return new ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
