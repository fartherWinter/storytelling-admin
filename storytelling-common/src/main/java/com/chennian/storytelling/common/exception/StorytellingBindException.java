package com.chennian.storytelling.common.exception;


import com.chennian.storytelling.common.enums.ResultCode;
import com.chennian.storytelling.common.response.ResponseEnum;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import lombok.Getter;

/**
 * 异常处理工具
 *
 * @author by chennian
 * @date 2023/3/14 15:46
 */
@Getter
public class StorytellingBindException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -4137688758944857209L;

    /**
     * http状态码
     */
    private Integer code;

    private Object object;

    private ServerResponseEntity<?> serverResponseEntity;

    public StorytellingBindException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.code = ResultCode.ERROR.code();
    }

    /**
     * @param responseEnum
     */
    public StorytellingBindException(ResponseEnum responseEnum, String msg) {
        super(msg);
        this.code = ResultCode.ERROR.code();
    }

    public StorytellingBindException(ServerResponseEntity<?> serverResponseEntity) {
        this.serverResponseEntity = serverResponseEntity;
    }


    public StorytellingBindException(String msg) {
        super(msg);
        this.code = ResultCode.ERROR.code();
    }

    public StorytellingBindException(String msg, Object object) {
        super(msg);
        this.code = ResultCode.ERROR.code();
        this.object = object;
    }

    /**
     * 抛出异常信息
     */
    public StorytellingBindException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }
}
