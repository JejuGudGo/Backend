package com.example.jejugudgo.global.exception.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum RetCode {
    RET_CODE00("00", "요청에 성공하였습니다."),
    RET_CODE01("01", "인증번호의 형식을 확인해주세요.(길이, 숫자외의 문자)"),
    RET_CODE02("02", "인증번호가 틀렸습니다."),
    RET_CODE03("03", "올바른 핸드폰번호 형식이 아닙니다. (0100000000, 하이픈 제외)"),
    RET_CODE04("04", "Cool SMS 요금을 충전해주세요."),
    RET_CODE05("05", "해당 휴대폰 번호 및 이름으로 등록된 유저가 없습니다."),
    RET_CODE06("06", "비밀번호 형식이 올바르지 않습니다."),
    RET_CODE07("07", "해당 이메일을 가진 유저가 존재합니다."),
    RET_CODE08("08", "존재하지 않는 이메일 입니다."),
    RET_CODE09("09", "알맞지 않은 비밀번호 입니다."),
    RET_CODE10("10", "존재하지 않는 북마크 유형입니다."),
    RET_CODE11("11", "이미지 파일값이 존재하지 않습니다."),
    RET_CODE12("12", "이미지 저장에 실패하였습니다."),
    RET_CODE13("13", "해당 이미지가 존재하지 않습니다."),
    RET_CODE14("14", "이미지 삭제에 실패하였습니다."),
    RET_CODE93("93", "알맞은 actionType 을 넣어주세요."),
    RET_CODE94("94", "알맞은 request param 을 넣어주세요."),
    RET_CODE95("95", "message consume 에 실패하였습니다."),
    RET_CODE96("96", "message publish 에 실패하였습니다."),
    RET_CODE97("97", "해당 결과가 존재하지 않습니다."),
    RET_CODE98("98", "토큰이 만료되었습니다."),
    RET_CODE99("99", "서버 오류로 인하여 요청에 실패하였습니다.");

    private final String retCode;
    private final String message;

    RetCode(String retCode, String message) {
        this.retCode = retCode;
        this.message = message;
    }

    public static RetCode fromCode(String code) {
        for (RetCode retCode : RetCode.values()) {
            if (retCode.getRetCode().equals(code)) {
                return retCode;
            }
        }
        return null;
    }

    public static List<RetCode> getAllCodes() {
        return List.of(RetCode.values());
    }
}
