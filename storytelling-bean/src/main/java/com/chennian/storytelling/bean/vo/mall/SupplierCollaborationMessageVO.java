package com.chennian.storytelling.bean.vo.mall;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

public class SupplierCollaborationMessageVO implements Serializable {
    private Long supplierId;
    private String supplierName;
    private Integer totalMessages;
    private Long unreadMessages;
    private List<Message> messages;

    public static class Message implements Serializable {
        private Long messageId;
        private Long supplierId;
        private String sender;
        private String content;
        private Boolean isRead;
        private Date createTime;

        public Long getMessageId() { return messageId; }
        public void setMessageId(Long messageId) { this.messageId = messageId; }
        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Boolean getIsRead() { return isRead; }
        public void setIsRead(Boolean isRead) { this.isRead = isRead; }
        public Date getCreateTime() { return createTime; }
        public void setCreateTime(Date createTime) { this.createTime = createTime; }
    }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public Integer getTotalMessages() { return totalMessages; }
    public void setTotalMessages(Integer totalMessages) { this.totalMessages = totalMessages; }
    public Long getUnreadMessages() { return unreadMessages; }
    public void setUnreadMessages(Long unreadMessages) { this.unreadMessages = unreadMessages; }
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
}