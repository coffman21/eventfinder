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
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
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

        BotApiMethod response = null;

        saveMessage(update);

        //reply to inline query

        if (update.hasInlineQuery()) {
            InlineQuery inlineQuery = update.getInlineQuery();
            String inlineQueryId = inlineQuery.getId();
            if (inlineQuery.hasLocation()) {
                Location location = update.getInlineQuery().getLocation();
                logger.info("User with chatId={} requested inline query: {}, location is {}",
                        inlineQueryId, inlineQuery.getQuery(), location);
                response = BotMessageCreator.createInlineReply(inlineQueryId, location);

            } else {
                logger.info("User with chatId={} requested inline query: {} but sent no location",
                        inlineQueryId, inlineQuery.getQuery());
                response = BotMessageCreator.createInlineReply(inlineQueryId, null);
            }

        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            // user sends location by pressing "Get nearest" button
            if (message.hasLocation()) {
                Location location = message.getLocation();
                response = BotMessageCreator.nearestTusovka(chatId, location);
                logger.info("User with chatId={} sent a Location: {}", location);

            } else if (message.hasText()) {
                String[] text = message.getText().split(" ");
                switch (text[0]) {
                    case "/start":
                        String username = message.getFrom().getUserName();
                        response = BotMessageCreator.createStartMessage(chatId, username);
                        logger.info("User with chatId={} pressed [/start] button", chatId);
                        break;

                    case "/help":
                        response = BotMessageCreator.createHelpMessage(chatId);
                        logger.info("User with chatId={} pressed [/help] button", chatId);
                        break;

                    case "Get":
                        if (text[1].equals("nearest")) {
                            //response = BotMessageCreator.createLocation(chatId);
//                            response = BotMessageCreator.nearestTusovka(chatId);
                            //pass
                            logger.info("User with chatId={} pressed [Get nearest place] button " +
                                    "and was requested for Location", chatId);

                        } else if (text[1].equals("upcoming")) {
                            response = BotMessageCreator.createTusovkasMessage(chatId);
                            logger.info("User with chatId={} pressed [Get upcoming events] button", chatId);
                        }
                        break;

                        // here to get search query

                    default:
                        logger.info("Unhandled update: {}", update.toString());
                        return;
                }
            }
        }
        try {
            if (response != null) {
                execute(response);
            }
        } catch (TelegramApiException e) {
            logger.error("Failed to send message due to error: {}", e.getMessage());
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
