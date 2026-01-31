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

    public ASPageItem (int page, int size, TransferType t) {
        this.page = page;
        this.size = size;
        this.transferType = t;
    }
}
