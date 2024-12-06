package com.example.jejugudgo.domain.user.athentication.signIn.dto.request;

import com.example.jejugudgo.domain.user.athentication.signIn.enums.TTL;

public record CurrentSignInStatus(
        TTL type,
        String remainingTime
) {
}
