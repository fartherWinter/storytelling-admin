package com.storytelling.common.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 * 
 * @author storytelling
 */
public class TableDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<?> rows;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo() {
    }

    /**
     * 分页
     * 
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 返回成功的分页数据
     *
     * @param list 列表数据
     * @param total 总记录数
     * @return 分页数据对象
     */
    public static TableDataInfo success(List<?> list, long total) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * 返回成功的分页数据
     *
     * @param list 列表数据
     * @param total 总记录数
     * @param msg 消息内容
     * @return 分页数据对象
     */
    public static TableDataInfo success(List<?> list, long total, String msg) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg(msg);
        rspData.setRows(list);
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * 返回失败的分页数据
     *
     * @param msg 错误消息
     * @return 分页数据对象
     */
    public static TableDataInfo error(String msg) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(500);
        rspData.setMsg(msg);
        rspData.setRows(null);
        rspData.setTotal(0);
        return rspData;
    }

    /**
     * 返回空的分页数据
     *
     * @return 分页数据对象
     */
    public static TableDataInfo empty() {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(null);
        rspData.setTotal(0);
        return rspData;
    }

    /**
     * 是否为成功状态
     *
     * @return true成功 false失败
     */
    public boolean isSuccess() {
        return this.code == 200;
    }

    /**
     * 是否为失败状态
     *
     * @return true失败 false成功
     */
    public boolean isError() {
        return this.code != 200;
    }
}