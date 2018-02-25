package com.banditos.server.bot;

import com.google.maps.model.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Location;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.awt.print.Pageable;
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

        BotApiMethod<Message> response = null;
        Long chatId = null;

        saveMessage(update);

        if (update.hasInlineQuery() && update.getInlineQuery().hasLocation()) {

            chatId = Long.valueOf(update.getInlineQuery().getFrom().getId());
            Location location = update.getInlineQuery().getLocation();
            response = BotMessageCreator.nearestTusovka(chatId, location);

        } else if (update.hasMessage()) {

            // here we are getting inline query which contains location
            // and handle somehow


            Message message = update.getMessage();
            chatId = message.getChatId();



            String[] text = message.getText().split(" ");
            switch (text[0]) {
                case "/start":
                    // ???
                    String username = message.getFrom().getUserName();
                    response = BotMessageCreator.createStartMessage(chatId, username);
                    break;
                case "/help":
                    response = BotMessageCreator.createHelpMessage(chatId);
                    break;
                case "Get":
                    if (text[1].equals("nearest")) {
                        response = BotMessageCreator.createLocation(chatId);
                        //response = BotMessageCreator.nearestTusovka(chatId);
                        //pass
                    } else if (text[1].equals("sorted")) {
                        response = BotMessageCreator.createTusovkasMessage(chatId);
                    }
                    break;
                case "Send":
                    if (text[1].equals("location")) {
                        response = BotMessageCreator.createLocation(chatId);
                    }
                    break;
                default:
                    logger.info("Unhandled update: {}", update.toString());
                    return;
            }
        }
        try {
            if (response != null) {
                execute(response);
            }
            if (chatId != null) {
                logger.info("Sent message to {}", chatId.toString());
            }
        } catch (TelegramApiException e) {
            logger.error("Failed to send message to {} due to error: {}", chatId, e.getMessage());
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
