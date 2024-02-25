package com.example.assignment1_1.domain;

import com.example.assignment1_1.dtos.TextMessageDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class TextMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String senderName;
    private String message;
    public enum MessageType {LEAVE, CHAT, JOIN}

    private TextMessageDTO.MessageType type;
}
