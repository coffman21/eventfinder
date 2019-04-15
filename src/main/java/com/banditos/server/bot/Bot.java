package com.banditos.server.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private String telegramToken;
    private BotMessageCreator botMessageCreator;

    @Autowired
    public Bot(Environment env, BotMessageCreator botMessageCreator) {
        this.telegramToken = env.getProperty("token.telegram");
        this.botMessageCreator = botMessageCreator;
    }

    @Override
    public String getBotToken() {
            return telegramToken;
    }

    public void onClosing() {
        logger.info("closing");
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
