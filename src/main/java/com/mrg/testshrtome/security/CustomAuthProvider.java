package com.mrg.testshrtome.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

//@Component
//@Primary
public class CustomAuthProvider implements AuthenticationProvider {
//    @Autowired
    UserDetailsService userDetailsService;
//    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String uname =authentication.getName();
        String pass= (String) authentication.getCredentials();

        UserDetails userDetails = userDetailsService.loadUserByUsername(uname);
        if (userDetails != null && userDetails.getUsername().equals(uname) && passwordEncoder.matches(pass,userDetails.getPassword())) {
            ArrayList<GrantedAuthority> authorities= (ArrayList<GrantedAuthority>) userDetails.getAuthorities();
            authorities.add(new SimpleGrantedAuthority("A"));
            return new UsernamePasswordAuthenticationToken(uname, pass,authorities);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
