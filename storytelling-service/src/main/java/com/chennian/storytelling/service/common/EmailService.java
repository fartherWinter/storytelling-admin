package com.chennian.storytelling.service.common;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



/**
 * 邮件服务类
 * 负责发送邮件
 * @author chen
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param text    邮件内容
     */
    public void sendSimpleMail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            // true表示HTML格式
            // helper.setFrom("your-email@example.com"); // 设置发件人，通常在配置文件中统一配置

            mailSender.send(message);
            logger.info("邮件发送成功: 收件人: {}, 主题: {}", to, subject);
        } catch (MessagingException e) {
            logger.error("发送邮件失败: 收件人: {}, 主题: {}, 错误: {}", to, subject, e.getMessage(), e);
            // 根据实际需求，这里可以抛出自定义异常或进行其他错误处理
        }
    }

    // 可以根据需要添加更多发送邮件的方法，例如发送带附件的邮件等
}