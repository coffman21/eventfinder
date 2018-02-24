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
        SendMessage response = new SendMessage();
        response.setChatId(id);

        StringBuilder sb = new StringBuilder();
        for (Tusovka t : tusovkas) {
            sb.append(t.getName() + "\n");
            sb.append(t.getPrice() + "\n");
            sb.append(t.getLink() + "\n");
        }
        response.setText(sb.toString());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("next");
        row.add("previous");

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        response.setReplyMarkup(keyboardMarkup);
        return response;
    }

    public static SendVenue createVenue(Long chatId, Long placeId) {

        // here should be enum or something like findOneByName

        Place place = placeRepository.findOne(placeId);

        SendVenue response = new SendVenue();

        response.setChatId(chatId);
        response.setAddress(place.getAddress());
        response.setLatitude(place.getLatitude());
        response.setLongitude(place.getLongitude());
        response.setTitle(place.getName());

        return response;
    }

    public static SendMessage createStartMessage(Long chatId, String username) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText("Hi everybody, ima testing /start command here. What is "
                + username +" ?");


        return response;
    }

    public static BotApiMethod<Message> createHelpMessage(Long chatId) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText("A help message.");

        return response;
    }

    public static BotApiMethod<Message> nearestTusovka(Long chatId) {
        return null;
    }
}
