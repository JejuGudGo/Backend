package com.example.jejugudgo.domain.user.checklist.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum DefaultCheckList {
    DEFAULT_CHECKLIST01("트래킹화 혹은 등산화"),
    DEFAULT_CHECKLIST02("모자"),
    DEFAULT_CHECKLIST03("우산 혹은 우의"),
    DEFAULT_CHECKLIST04("편한 옷"),
    DEFAULT_CHECKLIST05("넉넉한 배낭"),
    DEFAULT_CHECKLIST06("생수 1병 혹은 텀블러"),
    DEFAULT_CHECKLIST07("가벼운 구급약"),
    DEFAULT_CHECKLIST08("랜턴"),
    DEFAULT_CHECKLIST09("선글라스"),
    DEFAULT_CHECKLIST10("간식");

    private final String checkItem;

    DefaultCheckList(String checkItem) {
        this.checkItem = checkItem;
    }

    public static List<DefaultCheckList> getDefaultCheckList() {
        return Arrays.stream(DefaultCheckList.values())
                .collect(Collectors.toList());
    }
}
