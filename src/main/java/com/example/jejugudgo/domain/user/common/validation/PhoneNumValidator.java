package com.example.jejugudgo.domain.user.common.validation;

import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumValidator {
    private static final String PHONE_NUM_PATTERN = "^010[0-9]{8}$";

    public void isValidPhoneNum(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.matches(PHONE_NUM_PATTERN))
            throw new CustomException(RetCode.RET_CODE04);
    }
}
