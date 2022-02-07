package com.mk.tv.auth.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mk.tv.kernel.system.Config;
import jPlus.io.security.Access;
import jPlus.util.io.TimeUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthConfig extends Config {

    public Access securityLevel = Access.PRIVATE;
    public String userSessionDuration = "00:06:00:00";

    public String rejectUserMessage = "You aren't Chris...";
    public String onlySecureChannelWarning = "I won't do that for just anybody...";;
    public String publicChannelWarning = "This is a public channel... ";

    public AuthConfig() {
    }

    public AuthConfig(String token) {
        super(token);
    }

    public static AuthConfig newInstance() {
        return new AuthConfig();
    }

    public long sessionDurationS() {
        return TimeUtils.parseSecondsFrom(userSessionDuration);
    }
}
