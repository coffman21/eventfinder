package com.banditos.server.bot;

import com.banditos.server.model.Message;
import com.banditos.server.orm.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;

import java.time.Instant;
import java.util.Date;

@Component
public class BotMessageController {
    private static MessageRepository messageRepository;

    private BotApiMethod<org.telegram.telegrambots.api.objects.Message> botApiMethod;

    @Autowired
    public BotMessageController (MessageRepository messageRepository) {
        BotMessageController.messageRepository = messageRepository;
    }

    public void setBotApiMethod(
            BotApiMethod<org.telegram.telegrambots.api.objects.Message> botApiMethod) {
        this.botApiMethod = botApiMethod;
    }

    public static void saveMessage(org.telegram.telegrambots.api.objects.Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        Date sentTime = Date.from(
                Instant.ofEpochSecond(message.getDate())
        );

        Message message1 = new Message(chatId, messageText, sentTime);
        messageRepository.save(message1);
    }
}