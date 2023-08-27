package com.mrg.testshrtome.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrg.testshrtome.dtos.BaseResponse;
import com.mrg.testshrtome.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Qualifier("handlerExceptionResolver")
    @Autowired
    private HandlerExceptionResolver resolver;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
          resolver.resolveException(request, response, null, authException);
//        System.out.println("request.getMethod() = " + request.getMethod());
//        BaseResponse res =BaseResponse.builder().error(authException.getMessage()).status(StatusEnum.FAILED).requestId("id").build();
//        ResponseEntity<BaseResponse> re = new ResponseEntity(res,HttpStatus.UNAUTHORIZED);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        OutputStream responseStream = response.getOutputStream();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(responseStream, re);
//        responseStream.flush();

    }
}
