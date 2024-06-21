package com.gudgo.jeju.global.util;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {
    public boolean validateStringValue (String value) {
        if (value != null || !value.isEmpty()) {
            return true;
        }

        return false;
    }

    public boolean validateLongValue (Long value) {
        if (value != null) {
            return true;
        }

        return false;
    }
}
