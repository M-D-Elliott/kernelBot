package com.mk.tv.io.spring.sec;

import jPlus.io.file.FileUtils;
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

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class CustomIPAuth implements AuthenticationProvider, Serializable {

    public static CustomIPAuth instance;

    private final Set<String> whitelist = new HashSet<>(Collections.singletonList("0:0:0:0:0:0:0:1"));
    private Retrievable2<Boolean, String, String> authenticator = null;

    public CustomIPAuth() {
        instance = this;
    }

    //***************************************************************//

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        final WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        final String userIp = details.getRemoteAddress();
        System.out.println(userIp + " -- web login");

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
        } else throw new BadCredentialsException("Invalid username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    //***************************************************************//

    public void setAuthenticator(Retrievable2<Boolean, String, String> authenticator) {
        this.authenticator = authenticator;
    }

    public void addScrubbedIPs(File hostsFile) {
        final String regexMatcher =
                "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}[/]?(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)?";

        final Pattern pattern = Pattern.compile(regexMatcher);
        final List<String> scrubbedWhiteList =
                FileUtils.read(hostsFile).stream()
                        .filter(string -> pattern.matcher(string).matches())
                        .collect(Collectors.toList());

        CustomIPAuth.instance.whitelist.addAll(scrubbedWhiteList);
    }
}