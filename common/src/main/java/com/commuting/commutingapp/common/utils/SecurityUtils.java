package com.commuting.commutingapp.common.utils;

import com.commuting.commutingapp.common.dto.JwtData;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public final class SecurityUtils {

    public static String getUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((JwtData) JsonConverterUtils.convertFromJson(user.getUsername(), JwtData.class)).getId();
    }
}
