package com.mk.tv.io.spring.sec;

import jPlus.lang.callback.Retrievable2;
import jPlus.util.io.ConsoleUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class CustomIPAuth implements AuthenticationProvider, Serializable {

    public static CustomIPAuth instance;

    private final Set<String> whitelist = new HashSet<>();
    private Retrievable2<Boolean, String, String> authenticator = null;

    public CustomIPAuth() {
        instance = this;
    }

    //***************************************************************//

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        String userIp = details.getRemoteAddress();
        System.out.println(details.getRemoteAddress() + " -- web login");
        if (!whitelist.contains(userIp)) {
            System.out.println(ConsoleUtils.encaseInBanner("UNKNOWN IP"));
            throw new BadCredentialsException("Invalid IP Address");
        }

        final String name = auth.getName();
        final String password = auth.getCredentials().toString();

        if (authenticator.retrieve(name, password)) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
            return new UsernamePasswordAuthenticationToken(name, password, authorities);
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    //***************************************************************//

    public Set<String> whiteList() {
        return whitelist;
    }

    public void setAuthenticator(Retrievable2<Boolean, String, String> authenticator) {
        this.authenticator = authenticator;
    }
}