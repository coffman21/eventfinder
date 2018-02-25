package com.banditos.server.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="message_log")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    private String messageText;

    private Date sendTime;

    private Boolean isInline;

    private Float lat;

    private Float lng;


// private Object state;

    public Message(Long chatId, String messageText, Date sendTime) {
        this.chatId = chatId;
        this.messageText = messageText;
        this.sendTime = sendTime;
    }

    public Message(Long chatId, String messageText, Date sendTime, Float lat, Float lng, boolean isInline) {
        this.chatId = chatId;
        this.messageText = messageText;
        this.sendTime = sendTime;
        this.lat = lat;
        this.lng = lng;
        this.isInline = true;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Boolean getInline() {
        return isInline;
    }

    public void setInline(Boolean inline) {
        isInline = inline;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }
}
