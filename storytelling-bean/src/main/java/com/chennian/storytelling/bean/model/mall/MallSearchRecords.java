package com.chennian.storytelling.bean.model.mall;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author by chennian
 * @date 2025/5/25.
 */
@Data
@TableName("mall_search_records")
public class MallSearchRecords {
    /**
     * 主键id
     */
    @TableId
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
     * 创建时间
     */
    private Date createTime;
}
