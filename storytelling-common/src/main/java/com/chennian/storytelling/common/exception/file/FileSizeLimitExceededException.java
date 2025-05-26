package com.chennian.storytelling.common.exception.file;

/**
 * 文件名大小限制异常类
 *
 * @author by chennian
 * @date 2023/3/22 13:39
 */
public class FileSizeLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
