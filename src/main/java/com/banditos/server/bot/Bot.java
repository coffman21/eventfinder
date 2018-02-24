package com.banditos.server.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

import static com.banditos.server.bot.BotMessageController.saveMessage;

@Component
public class Bot extends TelegramLongPollingBot {

    private static String telegramToken;

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    @Autowired
    public Bot(Environment env) {
        Bot.telegramToken = env.getProperty("token.telegram");
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
            Long chatId = message.getChatId();
            saveMessage(message);

            BotApiMethod<Message> response;


            String[] text = message.getText().split(" ");
            switch (text[0]) {
                case "/start":
                    // ???
                    String username = message.getAuthorSignature();
                    response = BotMessageCreator.createStartMessage(username);
                    break;
                case "/help":
                    response = BotMessageCreator.createHelpMessage();
                    break;
                case "nearest":
                    response = BotMessageCreator.nearestTusovka();
                    break;
                default:
                    //handle
                    response = null;
                    break;
            }

            try {
                if (response != null) {
                    execute(response);
                }
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
