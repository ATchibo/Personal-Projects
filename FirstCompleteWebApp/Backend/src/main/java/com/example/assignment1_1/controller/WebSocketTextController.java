package com.example.assignment1_1.controller;

import com.example.assignment1_1.dtos.TextMessageDTO;
import com.example.assignment1_1.service.TextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketTextController {

    @Autowired
    private TextMessageService textMessageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public TextMessageDTO sendMessage(@Payload TextMessageDTO chatMessage) {
        textMessageService.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public TextMessageDTO addUser(@Payload TextMessageDTO chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderName());
        return chatMessage;
    }
}