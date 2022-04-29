package com.mk.tv.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mk.tv.auth.botusers.UserConfig;
import com.mk.tv.kernel.generic.Config;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthConfig extends Config {

    public UserConfig user = new UserConfig();

    public AuthConfig() {
    }

    public static AuthConfig newInstance() {
        final AuthConfig config = new AuthConfig();
        config.system.ioConfigs = defaultIOConfigs();
        return config;
    }
}
