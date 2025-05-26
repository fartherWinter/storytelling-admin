package com.chennian.storytelling.bean.bo;

import com.chennian.storytelling.bean.model.mall.MallSearchRecords;
import com.chennian.storytelling.common.utils.PageParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @author by chennian
 * @date 2025/5/25.
 */
@Data
public class MallSearchRecordsSearchBo implements Serializable {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 搜索内容
     */
    private String content;
    /**
     * 分页参数
     */
    PageParam<MallSearchRecords> pageParam;
}
