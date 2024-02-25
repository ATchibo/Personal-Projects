package com.example.assignment1_1.service;

import com.example.assignment1_1.domain.TextMessage;
import com.example.assignment1_1.dtos.TextMessageDTO;
import com.example.assignment1_1.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextMessageService {

    @Autowired
    private MessageRepo textMessageRepo;

    public void save(TextMessageDTO textMessageDTO) {
        TextMessage textMessage = new TextMessage();
        textMessage.setSenderName(textMessageDTO.getSenderName());
        textMessage.setMessage(textMessageDTO.getMessage());
        textMessage.setType(textMessageDTO.getType());
        textMessageRepo.save(textMessage);
    }
}
