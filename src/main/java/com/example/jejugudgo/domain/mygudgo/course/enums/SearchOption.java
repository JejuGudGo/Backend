package com.example.jejugudgo.domain.mygudgo.course.enums;

import lombok.Getter;

@Getter
public enum SearchOption {
    SEARCH_OPTION01("0", "추천(기본값)"),
    SEARCH_OPTION02("4", "추천+대로우선"),
    SEARCH_OPTION03("10", "최단"),
    SEARCH_OPTION04("30", "최단거리+계단제외");

    private final String searchOptionId;
    private final String searchOptionName;

    SearchOption(String searchOptionId, String searchOptionName) {
        this.searchOptionId = searchOptionId;
        this.searchOptionName = searchOptionName;
    }

    public static SearchOption fromSearchOptionId(String searchOptionId) {
        for (SearchOption searchOption : SearchOption.values()) {
            if (searchOption.searchOptionId.equals(searchOptionId)) {
                return searchOption;
            }
        }
        return null;
    }

    public static SearchOption fromSearchOptionName(String searchOptionName) {
        for (SearchOption searchOption : SearchOption.values()) {
            if (searchOption.searchOptionName.equals(searchOptionName)) {
                return searchOption;
            }
        }
        return null;
    }

}
