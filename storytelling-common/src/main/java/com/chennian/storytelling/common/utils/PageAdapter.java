package com.chennian.storytelling.common.utils;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 分页处理器
 *
 * @author by chennian
 * @date 2023/3/14 13:48
 */
@Data
public class PageAdapter {

    private int begin;

    private int size;

    public PageAdapter(Page page) {
        int[] startEnd = PageUtil.transToStartEnd((int) page.getCurrent() - 1, (int) page.getSize());
        this.begin = startEnd[0];
        this.size = (int) page.getSize();
    }
}
