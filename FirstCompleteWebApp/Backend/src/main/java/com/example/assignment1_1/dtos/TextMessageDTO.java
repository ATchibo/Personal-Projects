package com.example.assignment1_1.dtos;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TextMessageDTO {
    private String senderName;
    private String message;
    public enum MessageType {LEAVE, CHAT, JOIN}

    private MessageType type;
}