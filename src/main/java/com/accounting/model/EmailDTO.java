package com.accounting.model;

import java.util.List;

public class EmailDTO {
    private String subject;
    private String content;
    private List<Attachment> attachments;
    private String remoteUser;
    private String remoteAddress;
    private String userAgent;
    private String cui;

    public String getSubject() {
        return subject;
    }

    public EmailDTO setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        StringBuilder stringBuilder = new StringBuilder(content);

        stringBuilder.append("\n").append("Remote User: ").append(remoteUser);
        stringBuilder.append("\n").append("Remote Address: ").append(remoteAddress);
        stringBuilder.append("\n").append("User Agent: ").append(userAgent);
        stringBuilder.append("\n").append("Cui: ").append(cui);

        return stringBuilder.toString();
    }

    public EmailDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public EmailDTO setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public EmailDTO setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
        return this;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public EmailDTO setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public EmailDTO setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getCui() {
        return cui;
    }

    public EmailDTO setCui(String cui) {
        this.cui = cui;
        return this;
    }
}
