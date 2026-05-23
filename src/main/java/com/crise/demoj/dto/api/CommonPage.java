package com.crise.demoj.dto.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class CommonPage<T> {
    private Integer pageNum;

    private Integer pageSize;

    private Integer totalPage;

    private Long total;

    private List<T> data;

    public static <T> CommonPage<T> fromPage(IPage<T> page) {
        CommonPage<T> commonPage = new CommonPage<>();
        commonPage.setPageNum((int) page.getCurrent());
        commonPage.setPageSize((int) page.getSize());
        commonPage.setTotalPage((int) page.getPages());
        commonPage.setTotal(page.getTotal());
        commonPage.setData(page.getRecords());
        return commonPage;
    }
}
