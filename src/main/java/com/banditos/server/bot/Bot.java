package com.banditos.server.bot;

import com.banditos.server.bot.config.BotConfigurationProperties;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends AbilityBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private BotMessageCreator botMessageCreator;

    public Bot(BotConfigurationProperties properties,
            BotMessageCreator botMessageCreator,
            DefaultBotOptions botOptions) {
        super(properties.getToken(), properties.getBotname(), botOptions);
        this.botMessageCreator = botMessageCreator;
    }

    public void onClosing() {
        logger.info("closing");
    }

    @Override
    public int creatorId() {
        return 0;
    }

    public void onUpdateReceived(Update update) {
        logger.info("update received: " + update.toString());

        if (update.hasMessage()) {
            Message message = update.getMessage();
            BotMessageController.setMessage(message);
            Long chatId = message.getChatId();
            SendMessage response = botMessageCreator.createTusovkasMessage(chatId);

            try {
                execute(response);
                logger.info("Sent message to {}", chatId.toString());
            } catch (TelegramApiException e) {
                logger.error("Failed to send message to {} due to error: {}", chatId, e.getMessage());
            }
        }
        logger.info(update.toString());
    }

    public void onUpdatesReceived(List<Update> updates) {
        logger.info("updates received:");
        for (Update update : updates) {
            onUpdateReceived(update);
        }
    }

    public String getBotUsername() {
        // note that its unique username is eventSfinderbot
        return "eventfinder";
    }
}
