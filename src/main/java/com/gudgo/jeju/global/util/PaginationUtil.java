package com.gudgo.jeju.global.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
public class PaginationUtil {

    public static <T> Page<T> listToPage(List<T> list, Pageable pageable) {
        int total = list.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);

        List<T> subList = list.subList(start, end);

        return new PageImpl<>(subList, pageable, total);
    }
}
