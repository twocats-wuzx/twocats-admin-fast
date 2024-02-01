package com.community.manager.common.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class CommonPage<T> {

    private Long total;

    private List<T> records;

    public static <T, R> CommonPage<R> from(IPage<T> page, Function<T, R> function){
        CommonPage<R> commonPage = new CommonPage<>();
        commonPage.total = page.getTotal();
        if (CollectionUtils.isEmpty(page.getRecords())){
            commonPage.records = new ArrayList<>();
        }else {
            commonPage.records = page.getRecords().stream().map(function).collect(Collectors.toList());
        }
        return commonPage;
    }

}
