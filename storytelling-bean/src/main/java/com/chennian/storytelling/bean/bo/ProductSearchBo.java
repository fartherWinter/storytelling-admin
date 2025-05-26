package com.chennian.storytelling.bean.bo;

import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.common.utils.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/22.
 */
@Data
public class ProductSearchBo {
    private String productName;
    private String productCode;
    private String specification;
    @Schema(description = "分页")
    private PageParam<Product> param;
}
