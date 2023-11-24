package tech.twocats.admin.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class CommonPage<T> {

    private Long total;

    private List<T> records;

    public CommonPage<T> from(IPage<T> page){
        this.total = page.getTotal();
        this.records = page.getRecords();
        return this;
    }

}
