package com.example.jejugudgo.domain.auth.validation;

import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidation {
    public void validatePhoneNumber(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber)) throw new CustomException(RetCode.RET_CODE03);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
       return phoneNumber != null && phoneNumber.matches("^010[0-9]{8}$");
    }
}
