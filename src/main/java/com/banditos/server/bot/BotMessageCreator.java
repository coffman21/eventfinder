package com.banditos.server.bot;

import com.banditos.server.bot.geolocation.GoogleMapsApi;
import com.banditos.server.model.Place;
import com.banditos.server.model.Tusovka;
import com.banditos.server.orm.PlaceRepository;
import com.banditos.server.orm.TusovkaRepository;
import com.google.maps.model.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.objects.Location;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultLocation;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultVenue;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class BotMessageCreator {

    private static TusovkaRepository tusovkaRepository;

    private static PlaceRepository placeRepository;

    private static Logger logger = LoggerFactory.getLogger(BotMessageCreator.class);

    @Autowired
    public BotMessageCreator(TusovkaRepository tusovkaRepository, PlaceRepository placeRepository) {
        BotMessageCreator.tusovkaRepository = tusovkaRepository;
        BotMessageCreator.placeRepository = placeRepository;
    }

    public static SendMessage createTusovkasMessage(Long id) {
        Iterable<Tusovka> tusovkas = tusovkaRepository.findAllByDateAfter(
                Date.from(Instant.now()), new PageRequest(0, 5));
        SendMessage response = new SendMessage();
        response.setChatId(id);

        StringBuilder sb = new StringBuilder();
        for (Tusovka t : tusovkas) {
            sb.append(t.getName() + "\n");
            sb.append(t.getPrice() + "\n");
            sb.append(t.getLink() + "\n");
        }
        response.setText(sb.toString());
        return response;
    }

    private static SendVenue createVenue(String chatId) {
        SendVenue response = new SendVenue();

        Location location = new Location();
        Place place = calcNearest(location).get(0);

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

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setRequestLocation(true);
        keyboardButton.setText("Get nearest place");
        row.add(keyboardButton);

        row.add("Get upcoming events");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        response.setReplyMarkup(keyboardMarkup);

        return response;
    }

    private static InlineQueryResultLocation buildInlinePlace() {


        return null;
    }

    public static BotApiMethod<Message> createHelpMessage(Long chatId) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText("A help message.");
        return response;
    }

    public static BotApiMethod<Message> nearestTusovka(Long chatId, Location location) {

        Place place = calcNearest(location).get(0);

        SendVenue response = new SendVenue();
        response.setChatId(chatId);
        response.setAddress(place.getAddress());
        response.setLatitude(place.getLatitude());
        response.setLongitude(place.getLongitude());
        response.setTitle(place.getName());

        return response;
    }

    public static SendLocation createLocation(String chatId) {
        SendLocation sendLocation = new SendLocation();
        sendLocation.setChatId(chatId);

        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setRequestContact(true);
        keyboardButton.setText("asd");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row = new KeyboardRow();
        row.add(keyboardButton);
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendLocation.setReplyMarkup(replyKeyboardMarkup);

        return sendLocation;
    }

    public static AnswerInlineQuery createInlineReply(String inlineId, Location location) {
        List<InlineQueryResult> inlineQueryResults = new ArrayList<>();
        if (location != null) {
            // IDEA magic
            AtomicInteger i = new AtomicInteger(1);
            inlineQueryResults = calcNearest(location)
                    .stream()
                    .map(place -> {
                        InlineQueryResultVenue query = new InlineQueryResultVenue();
                        query.setLatitude(place.getLatitude());
                        query.setLongitude(place.getLongitude());
                        query.setTitle(place.getName());
                        query.setAddress(place.getAddress());
                        query.setId(String.valueOf(i.get()));
                        i.getAndIncrement();
                        return query;
                    })
                    .collect(Collectors.toList());
            if (inlineQueryResults.size() == 0) {
                InputTextMessageContent inputTextMessageContent = new InputTextMessageContent();
                inputTextMessageContent.setMessageText("MESSAGE TEXT");

                InlineQueryResultArticle emptyQuery = new InlineQueryResultArticle();
                emptyQuery.setId(inlineId);
                emptyQuery.setTitle("TITLE");
                emptyQuery.setInputMessageContent(inputTextMessageContent);
                inlineQueryResults.add(emptyQuery);
            }
        }
        AnswerInlineQuery response = new AnswerInlineQuery();
        response.setInlineQueryId(String.valueOf(inlineId));
        response.setResults(inlineQueryResults);

        return response;
    }

    private static List<Place> calcNearest(Location location) {

        Float lat = location.getLatitude();
        Float lng = location.getLongitude();

        logger.info("Got location : lat: {}, lng: {}", lat, lng);
        logger.info("What is located here: {}", GoogleMapsApi.getAddressByCoordinates(new LatLng(lat, lng)));

        Iterable<Place> places = placeRepository.findAll();
        Map<Place, Double> mapa = new HashMap<>();
        places.forEach(place -> {
            mapa.put(
                    place,
                    Math.sqrt(
                            Math.pow(place.getLatitude()-lat, 2)
                                    + Math.pow(place.getLongitude() - lng, 2)));
            logger.debug("current distance is {}, for place {}", mapa.get(place), place);
        });

        logger.info("distances: {}", mapa);

        List<Place> inlineQueryResults = mapa
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(node -> node.getKey())
                .collect(Collectors.toList());

        return inlineQueryResults;
    }
}
