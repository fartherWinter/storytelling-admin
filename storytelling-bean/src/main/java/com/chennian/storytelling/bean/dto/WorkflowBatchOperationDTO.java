package com.chennian.storytelling.bean.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 工作流批量操作数据传输对象
 * 
 * @author chennian
 */
public class WorkflowBatchOperationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作类型
     */
    public enum OperationType {
        APPROVE,    // 批准
        REJECT,     // 拒绝
        COMPLETE,   // 完成
        TERMINATE,  // 终止
        ASSIGN,     // 分配
        CLAIM       // 认领
    }

    /**
     * 操作类型
     */
    private OperationType operationType;

    /**
     * 任务ID列表（用于批量任务操作）
     */
    private List<String> taskIds;

    /**
     * 流程实例ID列表（用于批量流程操作）
     */
    private List<String> processInstanceIds;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 批量操作意见
     */
    private String comment;

    /**
     * 分配给的用户（用于分配操作）
     */
    private String assignee;

    /**
     * 流程变量（用于完成任务时设置）
     */
    private Map<String, Object> variables;

    /**
     * 操作结果
     */
    private BatchOperationResult result;

    /**
     * 批量操作结果
     */
    public static class BatchOperationResult implements Serializable {
        
        private static final long serialVersionUID = 1L;

        /**
         * 总操作数
         */
        private int totalCount;

        /**
         * 成功操作数
         */
        private int successCount;

        /**
         * 失败操作数
         */
        private int failureCount;

        /**
         * 失败详情
         * Key: 任务ID或流程实例ID, Value: 失败原因
         */
        private Map<String, String> failureDetails;

        /**
         * 操作是否全部成功
         */
        private boolean allSuccess;

        // 构造函数
        public BatchOperationResult() {}

        public BatchOperationResult(int totalCount) {
            this.totalCount = totalCount;
            this.successCount = 0;
            this.failureCount = 0;
            this.allSuccess = false;
        }

        // Getter和Setter方法
        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
            this.allSuccess = (this.successCount == this.totalCount);
        }

        public int getFailureCount() {
            return failureCount;
        }

        public void setFailureCount(int failureCount) {
            this.failureCount = failureCount;
            this.allSuccess = (this.failureCount == 0);
        }

        public Map<String, String> getFailureDetails() {
            return failureDetails;
        }

        public void setFailureDetails(Map<String, String> failureDetails) {
            this.failureDetails = failureDetails;
        }

        public boolean isAllSuccess() {
            return allSuccess;
        }

        public void setAllSuccess(boolean allSuccess) {
            this.allSuccess = allSuccess;
        }

        /**
         * 增加成功计数
         */
        public void incrementSuccess() {
            this.successCount++;
            this.allSuccess = (this.successCount == this.totalCount);
        }

        /**
         * 增加失败计数
         */
        public void incrementFailure() {
            this.failureCount++;
            this.allSuccess = false;
        }
    }

    // 构造函数
    public WorkflowBatchOperationDTO() {}

    public WorkflowBatchOperationDTO(OperationType operationType, String operator) {
        this.operationType = operationType;
        this.operator = operator;
    }

    // Getter和Setter方法
    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public List<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<String> taskIds) {
        this.taskIds = taskIds;
    }

    public List<String> getProcessInstanceIds() {
        return processInstanceIds;
    }

    public void setProcessInstanceIds(List<String> processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public BatchOperationResult getResult() {
        return result;
    }

    public void setResult(BatchOperationResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "WorkflowBatchOperationDTO{" +
                "operationType=" + operationType +
                ", taskIds=" + taskIds +
                ", processInstanceIds=" + processInstanceIds +
                ", operator='" + operator + '\'' +
                ", comment='" + comment + '\'' +
                ", assignee='" + assignee + '\'' +
                ", variables=" + variables +
                ", result=" + result +
                '}';
    }
}