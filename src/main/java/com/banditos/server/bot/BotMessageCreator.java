package com.banditos.server.bot;

import com.banditos.server.model.Tusovka;
import com.banditos.server.orm.TusovkaRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class BotMessageCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotMessageCreator.class);

    private TusovkaRepository tusovkaRepository;

    @Autowired
    public BotMessageCreator(TusovkaRepository tusovkaRepository) {
        this.tusovkaRepository = tusovkaRepository;
    }

    public SendMessage createTusovkasMessage(Long id) {
        Iterable<Tusovka> tusovkas = tusovkaRepository.findAllByDateGreaterThanEqual(
                Date.from(Instant.now().minus(Duration.ofDays(1))), PageRequest.of(0, 10));
        SendMessage response = new SendMessage();
        response.setChatId(id);

        StringBuilder sb = new StringBuilder();
        for (Tusovka t : tusovkas) {
            sb.append(t.getName()).append("\n");
            sb.append(t.getPrice()).append("\n");
            sb.append(t.getLink()).append("\n");
        }

        response.setText(sb.toString());
        response.setReplyMarkup(createKeyboard());
        LOGGER.debug(sb.toString());
        return response;
    }

    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add("next");
        row.add("previous");

        List<KeyboardRow> keyboard = Collections.singletonList(row);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
