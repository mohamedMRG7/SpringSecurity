package com.mrg.testshrtome.security;

import antlr.collections.List;
import com.mrg.testshrtome.exception.AuthEntryPoint;
import com.mrg.testshrtome.exception.customexceptions.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TestFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final AuthEntryPoint authEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String userName = "";
        String pass = "";
        String uri = request.getRequestURI();
        String header =request.getHeader(JWTConstants.AUTH_HEADER) ;

        try {
            if (header == null)
            {
                throw new RuntimeException("Missing Headers");
            }
            System.out.println("uri = " + uri);
            if (header.startsWith(JWTConstants.BASIC_TOKEN_PREFIX) && uri.endsWith(JWTConstants.SIGN_IN_URI_ENDING))
            {
                header = StringUtils.delete(header, JWTConstants.BASIC_TOKEN_PREFIX);
                userName=JWTUtils.decodedBase64(header)[0];
                pass =JWTUtils.decodedBase64(header)[1];

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,pass);
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                Authentication authenticate = authenticationManager.authenticate(authenticationToken);
                System.out.println("authenticate = " + authenticate.isAuthenticated());
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                filterChain.doFilter(request,response);
                return;
            }

            if (header.startsWith(JWTConstants.BEARER_TOKEN_PREFIX) && !uri.endsWith(JWTConstants.SIGN_IN_URI_ENDING))
            {
                String token = StringUtils.delete(header, JWTConstants.BEARER_TOKEN_PREFIX);
                if (!JWTUtils.isJWTTokenValid(token))
                {
                    filterChain.doFilter(request,response);
                    return;
                }
                userName = JWTUtils.getUsernameFromJWTUserToken(token);
                ArrayList<GrantedAuthority> roles = JWTUtils.getRoles(token);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,null,roles);
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request,response);
                return;
            }
            throw new AuthException("Authentication Failed");
        } catch (AuthenticationException e) {
            authEntryPoint.commence(request,response,e);
        }
    }
}
