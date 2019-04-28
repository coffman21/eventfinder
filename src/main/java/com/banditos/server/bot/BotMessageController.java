package com.banditos.server.bot;

import com.banditos.server.model.Message;
import com.banditos.server.orm.MessageRepository;
import java.time.Instant;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotMessageController {
    private static MessageRepository messageRepository;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BotMessageController.class);

    @Autowired
    public BotMessageController (MessageRepository messageRepository) {
        BotMessageController.messageRepository = messageRepository;
    }

    public static void setMessage(org.telegram.telegrambots.meta.api.objects.Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        Date sentTime = Date.from(
                Instant.ofEpochSecond(message.getDate())
        );

        LOGGER.debug("incoming msg: "
                + messageText
                + " from: "
                + message.getChatId());

        Message message1 = new Message(chatId, messageText, sentTime);
        messageRepository.save(message1);
    }
}
