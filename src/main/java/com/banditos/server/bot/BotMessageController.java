package com.banditos.server.bot;

import com.banditos.server.orm.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.time.Instant;
import java.util.Date;

@Component
public class BotMessageController {
    private static MessageRepository messageRepository;

    @Autowired
    public BotMessageController (MessageRepository messageRepository) {
        BotMessageController.messageRepository = messageRepository;
    }

    public static void saveMessage(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        Date sentTime = Date.from(
                Instant.ofEpochSecond(message.getDate())
        );

        com.banditos.server.model.Message message1
                = new com.banditos.server.model.Message (chatId, messageText, sentTime);
        messageRepository.save(message1);
    }
}