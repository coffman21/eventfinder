package com.banditos.server.bot.geolocation;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoogleMapsApiTests {

    String cur_address = "Goncharnaya Ulitsa, 14, Moskva, Russia, 109240";
    Double cur_lat = 55.7451378;
    Double cur_lng = 37.6465727;

    private Logger logger = LoggerFactory.getLogger(GoogleMapsApiTests.class);

    @Test
    public void testAddress() {
        String address = GoogleMapsApi.getAddressByCoordinates(new LatLng(cur_lat, cur_lng));
        logger.info(address);
        Assert.assertSame(cur_address, address);
    }

    @Test
    public void testCoords() {
        LatLng latLng = GoogleMapsApi.getCoordinatesByAddress(cur_address);
        logger.info(latLng.toString());

        Assert.assertSame(cur_lat.intValue(), (int)latLng.lat);
        Assert.assertSame(cur_lng.intValue(), (int)latLng.lng
        );
    }
}
