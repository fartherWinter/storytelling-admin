package com.chennian.storytelling.common.exception.weichat;

import com.chennian.storytelling.common.exception.base.BaseException;

/**
 * @author by chennian
 * @date 2023/3/30 14:27
 */
public class WeiChatException extends BaseException {
    private static final long serialVersionUID = 1L;

    public WeiChatException(String code, Object[] args) {
        super("weichat", code, args, null);
    }
}
