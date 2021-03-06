package com.banditos.server.parser.vk;

import com.banditos.server.model.Place;
import com.banditos.server.model.Tusovka;
import com.banditos.server.orm.TusovkaRepository;
import com.banditos.server.parser.Parser;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.responses.GetExtendedResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;

public abstract class AbstractVkParser implements Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractVkParser.class);

    private final TusovkaRepository tusovkaRepository;
    private final VkApiClient vk;
    private final ServiceActor actor;

    @Autowired
    AbstractVkParser(Environment env, TusovkaRepository tusovkaRepository) {
        TransportClient transportClient = new HttpTransportClient();
        this.vk = new VkApiClient(transportClient);

        Integer app_id = Integer.valueOf(env.getProperty("vk.appid"));
        String token = env.getProperty("vk.token");
        this.actor = new ServiceActor(app_id, token);

        this.tusovkaRepository = tusovkaRepository;
    }

    @Override
    public List<Tusovka> parseTusovkas(Place place) {
        GetExtendedResponse response;
        List<Tusovka> tusovkas = new ArrayList<>();

        try {
            response = vk.wall().getExtended(actor)
                    .domain("pwrhs").count(5).execute();
        } catch (ApiException | ClientException e) {
            LOGGER.error("Error when getting wall posts from vk: ", e);
            return tusovkas;
        }
        List<GroupFull> lgf = response.getGroups();
        int i = 0;
        for (WallPostFull wpf : response.getItems()) {
            GroupFull gf = lgf.get(i);

            URL url = null;
            try {
                url = new URL("https://xui.tebe");
            } catch (MalformedURLException e) {
                LOGGER.error("Error when parsing URL: ", e);
            }

            tusovkas.add(new Tusovka()
                    .setName(gf.getName())
                    .setBeginDate(LocalDateTime.from(Instant.ofEpochSecond(wpf.getDate())))
                    .setDescription(wpf.getText())
                    .setPlace(place)
                    .setLink(url));
            i++;
        }
        return tusovkas;
    }

    private LocalDateTime getLastTusovka(String place) {
        List<Tusovka> tusovka = tusovkaRepository
                .findByPlaceOrderByDateDesc(place, PageRequest.of(0, 1));
        return tusovka.get(0).getBeginDate();
    }
}
