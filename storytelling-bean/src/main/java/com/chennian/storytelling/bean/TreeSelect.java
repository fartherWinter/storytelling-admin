package com.chennian.storytelling.bean;

import com.chennian.storytelling.bean.model.SysDept;
import com.chennian.storytelling.bean.model.SysMenu;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by chennian
 * @date 2025/5/6.
 */
@Data
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 父节点ID
     */
    private Long parentId;
    /**
     * 节点ID
     */
    private Long id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {

    }

    public TreeSelect(SysDept dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }


    public TreeSelect(SysMenu menu) {
        this.id = menu.getId();
        this.parentId= menu.getParentId();
        this.label = menu.getName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }




}
