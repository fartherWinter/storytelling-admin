package com.chennian.storytelling.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author by chennian
 * @date 2023/3/14 13:48
 */
@Schema(description = "分页参数")
public class PageParam<T> extends Page<T> {
    /**
     * 每页显示条数，默认 10
     */
    @Schema(description = "每页大小，默认10")
    private long size = 10;

    /**
     * 当前页
     */
    @Schema(description = "当前页，默认1")
    private long current = 1;

    /**
     * 查询数据列表
     */
    @Hidden
    private List<T> records;
    /**
     * 总数
     */
    @Hidden
    private long total = 0;


    /**
     * 是否进行 count 查询
     */
    @JsonIgnore
    private boolean isSearchCount = true;

    @JsonIgnore
    private String countId;
    @JsonIgnore
    private Long maxLimit;
    @JsonIgnore
    private boolean optimizeCountSql;

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public Page<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public Page<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @JsonIgnore
    public boolean getSearchCount() {
        if (total < 0) {
            return false;
        }
        return isSearchCount;
    }

    @Override
    public boolean searchCount() {
        if (total < 0) {
            return false;
        }
        return isSearchCount;
    }

    @Override
    public Page<T> setSearchCount(boolean isSearchCount) {
        this.isSearchCount = isSearchCount;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public Page<T> setSize(long size) {
        int maxSize = 100;
        if (size > maxSize) {
            this.size = maxSize;
        } else {
            this.size = size;
        }
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public Page<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getCountId() {
        return this.countId;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Long getMaxLimit() {
        return this.maxLimit;
    }


    /**
     * @deprecated
     */
    @Deprecated
    public boolean isOptimizeCountSql() {
        return this.optimizeCountSql;
    }

}
