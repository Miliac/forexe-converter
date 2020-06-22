package com.accounting.service;

import com.accounting.model.Attachment;
import com.accounting.model.EmailDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static com.accounting.config.Utils.COMMA;
import static com.accounting.config.Utils.TEXT_CONTENT_TYPE;

@Service
public class MailService {

    private static final Logger logger = LogManager.getLogger(MailService.class);
    private final Properties props;
    @Value("${email.from.username:verificare.balanta@gmail.com}")
    private String username;
    @Value("${email.from.password:Alex&Andrei}")
    private String password;
    @Value("${emails.to:andrey_valy93@yahoo.com,novacean.alex@gmail.com}")
    private String emailsTo;

    public MailService() {
        props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail,smtp.starttls.port", "587");
    }

    public void sendMail(EmailDTO emailDTO) {
        if(!StringUtils.isEmpty(username) || !StringUtils.isEmpty(password)) {
            for (String emailTo : emailsTo.split(COMMA)) {
                sendMail(emailTo, emailDTO.getSubject(), emailDTO.getContent(), emailDTO.getAttachments());
            }
        }
    }

    private void sendMail(String to, String subject, String content, List<Attachment> attachments) {
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);

            // Set the email message text.
            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setContent(content, TEXT_CONTENT_TYPE);

            // Create Multipart E-Mail.
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);

            for(Attachment attachment : attachments) {
                messagePart = new MimeBodyPart();
                attachFile(attachment.getFileName(), new String(attachment.getFileContent(), StandardCharsets.UTF_8), messagePart);
                multipart.addBodyPart(messagePart);
            }

            message.setContent(multipart);

            Transport.send(message);
            logger.info("Email sent to: {} ", to);
        } catch (MessagingException e) {
            logger.info("Error while sending email to: {} with error: {}", to, e.getMessage());
        }
    }

    private void attachFile(String fileName, String fileContent, MimeBodyPart messagePart) throws MessagingException {
        File attachFile = new File(fileName);
        try (FileWriter writer = new FileWriter(attachFile)) {
            writer.write(fileContent);
            messagePart.attachFile(attachFile);
        } catch (IOException e) {
            logger.info("Error while attaching file to email with error: {}", e.getMessage());
        }
    }

}
