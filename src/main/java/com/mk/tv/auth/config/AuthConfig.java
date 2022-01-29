package com.mk.tv.auth.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mk.tv.kernel.system.Config;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthConfig extends Config {

    public AuthConfig() {
    }

    public AuthConfig(String token) {
        super(token);
    }

    public static AuthConfig newInstance() {
        return new AuthConfig();
    }
}
