package com.chennian.storytelling.common.exception.file;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 文件上传异常类
 *
 * @author by chennian
 * @date 2023/3/22 13:39
 */
public class FileUploadException extends Exception {
    private static final long serialVersionUID = 1L;

    private final Throwable cause;

    public FileUploadException() {
        this(null, null);
    }

    public FileUploadException(final String msg) {
        this(msg, null);
    }

    public FileUploadException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    @Override
    public void printStackTrace(PrintStream stream) {
        super.printStackTrace(stream);
        if (cause != null) {
            stream.println("Caused by:");
            cause.printStackTrace(stream);
        }
    }

    @Override
    public void printStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
        if (cause != null) {
            writer.println("Caused by:");
            cause.printStackTrace(writer);
        }
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
