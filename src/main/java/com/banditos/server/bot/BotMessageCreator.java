package com.banditos.server.bot;

import com.banditos.server.model.Place;
import com.banditos.server.model.Tusovka;
import com.banditos.server.orm.PlaceRepository;
import com.banditos.server.orm.TusovkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.ResponseParameters;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotMessageCreator {

    private static TusovkaRepository tusovkaRepository;

    private static PlaceRepository placeRepository;

    @Autowired
    public BotMessageCreator(TusovkaRepository tusovkaRepository, PlaceRepository placeRepository) {
        BotMessageCreator.tusovkaRepository = tusovkaRepository;
        BotMessageCreator.placeRepository = placeRepository;
    }

    public static SendMessage createTusovkasMessage(Long id) {
        Iterable<Tusovka> tusovkas = tusovkaRepository.findAll();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);

        StringBuilder sb = new StringBuilder();
        for (Tusovka t : tusovkas) {
            sb.append(t.getName() + "\n");
            sb.append(t.getPrice() + "\n");
            sb.append(t.getLink() + "\n");
        }
        sendMessage.setText(sb.toString());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("next");
        row.add("previous");

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public static SendVenue createVenue(Long chatId, Long placeId) {
        Place place = placeRepository.findOne(placeId
        );

        SendVenue sendVenue = new SendVenue();

        sendVenue.setChatId(chatId);
        sendVenue.setAddress(place.getAddress());
        sendVenue.setLatitude(place.getLatitude());
        sendVenue.setLongitude(place.getLongitude());
        sendVenue.setTitle(place.getName());

        return sendVenue;
    }

    public static SendMessage createStartMessage(String username) {
        SendMessage sendMessage = new SendMessage();



        return null;
    }

    public static BotApiMethod<Message> createHelpMessage() {
        return null;
    }

    public static BotApiMethod<Message> nearestTusovka() {
        return null;
    }
}
