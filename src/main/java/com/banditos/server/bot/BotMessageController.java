package com.banditos.server.bot;

import com.banditos.server.orm.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;

import java.time.Instant;
import java.util.Date;

@Component
public class BotMessageController {
    private static MessageRepository messageRepository;

    @Autowired
    public BotMessageController (MessageRepository messageRepository) {
        BotMessageController.messageRepository = messageRepository;
    }

    public static void saveMessage(Update update) {

        com.banditos.server.model.Message message = null;
        if (update.hasMessage()) {
            Message msg = update.getMessage();

            Long chatId = msg.getChatId();
            String messageText = msg.getText();
            Date sentTime = Date.from(Instant.ofEpochSecond(msg.getDate()));

            message = new com.banditos.server.model.Message(chatId, messageText, sentTime);

        } else if (update.hasInlineQuery()) {
            InlineQuery inlineQuery = update.getInlineQuery();

            Long chatId             = Long.valueOf(inlineQuery.getFrom().getId());
            String messageText      = inlineQuery.getQuery();
            Date sentTime           = Date.from(Instant.now());
            Float lat               = inlineQuery.getLocation().getLatitude();
            Float lng               = inlineQuery.getLocation().getLongitude();
            message = new com.banditos.server.model.Message(
                    chatId, messageText, sentTime, lat, lng, true);
        }
        messageRepository.save(message);
    }
}