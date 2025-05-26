package com.chennian.storytelling.common.exception.file;

import com.chennian.storytelling.common.exception.base.BaseException;

/**
 * @author by chennian
 * @date 2023/3/22 13:34
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }
}
