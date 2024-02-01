package com.community.manager.common.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

@Data
public class PageQuery {

    /**
     * 分页大小
     */
    private Integer size;

    /**
     * 当前页码
     */
    private Integer current;


    public <T> IPage<T> getPage(){
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(this.current, this.size);
    }
}
