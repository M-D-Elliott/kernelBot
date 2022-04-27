package com.mk.tv.auth.botusers;

import jPlus.io.Access;
import jPlus.util.io.TimeUtils;

public class UserConfig {
    public Access securityLevel = Access.PRIVATE;
    public String userSessionDuration = "00:06:00:00";

    public String rejectUserMessage = "You aren't Chris...";
    public String onlySecureChannelWarning = "I won't do that for just anybody...";;
    public String publicChannelWarning = "This is a public channel... ";

    public long sessionDurationS() {
        return TimeUtils.parseSecondsFrom(userSessionDuration);
    }
}
