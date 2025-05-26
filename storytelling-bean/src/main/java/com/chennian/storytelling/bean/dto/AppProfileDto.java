package com.chennian.storytelling.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@Data
public class AppProfileDto {
    private String userName;
    private String nickName;
    private String email;
    private String phoneNumber;
    private String deptName;
    private String password;
    private Integer deptId;
    private Integer userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
