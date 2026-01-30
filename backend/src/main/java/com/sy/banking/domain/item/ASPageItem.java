package com.sy.banking.domain.item;

import com.sy.banking.enumbox.OrderByType;
import com.sy.banking.enumbox.TransferType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Account statement page item
public class ASPageItem {
    /*
    @Schema(description = "페이지 번호 1부터 시작")
    @Min(1)
    private int page;
    @Schema(description = "페이지 번호 최대 10")
    @Max(10)
    private int size;

    @Schema(description = "검색어", example = "samsung")
    private String keyword; // 검색어
    @Schema(description = "찾을 가격")
    private int searchPrice;//
    @Schema(description = "MORE <=, LESS >=, EQUAL = ")  
    private PriceConditionEnum priceConditionEnum; // >=, <=, =
    @Schema(description = "원산지", example = "korea")
    private String productOrigin; 

    @Schema(description = "정렬방식 ASC, DESC")
    private OrderByType orderByType;

    public int getOffset() {
        return (page - 1) * size;
    }
    */
    @Min(1)
    private int page;
    @Max(10)
    private int size;
   
    @Schema(description = "DEPOSIT, WITHDRAWAL, TRANS_IN, TRANS_OUT, INTEREST /// IF NULL = ALL")
    private TransferType transferType;

    @Schema(description = "DESC, ASC // IF NULL DESC")
    private OrderByType orderByType;

    public int getOffset() {
        return (page - 1) * size; 
    }

    public ASPageItem (int page, int size) {
        this.page = page;
        this.size = size;
    }
}
