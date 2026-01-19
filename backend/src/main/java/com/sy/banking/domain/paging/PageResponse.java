package com.sy.banking.domain.paging;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
    private int page;
    private int size;
    private int totalPage;
    private long totalCount;
    private String status;
    private String accountNumber;
    private List<T> list;

    public static <T> PageResponse<T> page(int page, int size, long totalCount, String status, String accountNumber, List<T> list){
        int totalPage = (int) Math.ceil((double) totalCount / size);
        return new PageResponse<>(
            page, 
            size, 
            totalPage, 
            totalCount, 
            status, 
            accountNumber,
            list
        );
    }
}
