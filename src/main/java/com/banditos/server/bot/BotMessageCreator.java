package com.banditos.server.bot;

import com.banditos.server.model.Tusovka;
import com.banditos.server.orm.TusovkaRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class BotMessageCreator {

    private TusovkaRepository tusovkaRepository;

    @Autowired
    public BotMessageCreator(TusovkaRepository tusovkaRepository) {
        this.tusovkaRepository = tusovkaRepository;
    }

    public SendMessage createTusovkasMessage(Long id) {
        Iterable<Tusovka> tusovkas = tusovkaRepository.findAllByDateGreaterThanEqual(
                Date.from(Instant.now()), new PageRequest(0, 10));
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
