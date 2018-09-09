package com.practice.listner;

import lombok.Data;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import javax.mail.internet.MimeMessage;

/**
 * Created by psingh15 on 5/20/17.
 */
@Component
@Slf4j
@Data
public class EmailNotification {
    private static final String MAIL_HOST = "mail.jcpenney.com";


    @Autowired
    JavaMailSenderImpl sender;
    @Synchronized
    public void sendMail() {
        try {

            sender.setHost(MAIL_HOST);
            MimeMessage message = sender.createMimeMessage();
            // use the true flag to indicate you need a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo("psingh15@jcp.com");
            helper.setFrom("psingh15@jcp.com");
            helper.setSubject("try fail msg");
            helper.setText("MSG_BODY");
            FileSystemResource file = new FileSystemResource("/Users/psingh15/DelphiCode/PracticeFiles/user.csv");
            helper.addAttachment("PracticeFile", file);
            sender.send(message);
        } catch (MessagingException ex) {
            log.error("Exception occurred during mail sent --[{}] - errorMessage - [{}]",
                    ex.getCause(), ex);
        } catch (javax.mail.MessagingException ex) {
            log.error("Exception occurred during mail sent--cause --[{}]---exception[{}]",
                    ex.getCause(), ex);
        } catch (Exception exp) {
            log.error("Exception occurred during mail sent--cause --[{}]---exception[{}]",
                    exp.getCause(), exp);
        }
    }
}

