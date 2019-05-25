package com.banditos.server.parser.facebook;

import com.banditos.server.model.Place;
import com.banditos.server.model.Tusovka;
import com.banditos.server.parser.Parser;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FacebookParser implements Parser, DisposableBean {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FacebookParser.class);
    private static final String FB_PAGE_LINK = "https://mobile.facebook.com/";
    private static final String FB_LOG_IN_TITLE = "Log in to Facebook";
    private static final String XPATH_FOR_VIEW_EVENTS_BUTTON = "(//a[contains(text(),'View Event Details')])";
    private static final String XPATH_FOR_EVENT_TITLE = "//h3";
    private static final String XPATH_FOR_EVENT_DATE = "//div[@id='event_summary']/div/div/table/tbody/tr/td[2]/dt/div";
    private static final String XPATH_FOR_EVENT_DESCRIPTION = "//div[@id='event_tabs']/div[2]/div[2]/div[2]";
    private static final String XPATH_FOR_ADDRESS = "/html/body/div/div/div[2]/div/div[1]/div[1]/div[2]/div/div[3]/div/div[1]/a/table/tbody/tr/td[2]/div/div[1]";

    private static final String XPATH_FOR_SEE_MORE_BTN = "//span[text()[contains(.,'See More Events')]]";
    private static final String XPATH_FOR_SEE_MORE_BTN_2 = "//div[@id='m_more_friends_who_like_this']/a/span";

    private final WebDriver driver;
    private final Environment env;

    @Autowired
    public FacebookParser(Environment env) {
        this.env = env;
        this.driver = new HtmlUnitDriver(true);
    }

    @Override
    public void destroy() {
        driver.quit();
    }

    @Override
    public List<Tusovka> parseTusovkas(Place place) {
        String domain = place.getFacebookDomain();
        if (domain == null) {
            throw new IllegalStateException("domain field not set");
        }

        List<Tusovka> tusovkas = new ArrayList<>();

        if (isNotLoggedIn()) {
            login();
        }

        try {
            String fbGroupUrl = FB_PAGE_LINK + domain + "/events";
            driver.get(fbGroupUrl);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Opening url: {} Got page: {}", fbGroupUrl,
                        driver.getPageSource());
            }

            List<String> eventHrefs = driver.findElements(By.xpath(
                    XPATH_FOR_VIEW_EVENTS_BUTTON))
                    .stream()
                    .map(e -> e.getAttribute("href"))
                    .collect(Collectors.toList());

            for (int i = 0; i < 5; i++) {
                String eventUrl = eventHrefs.get(i);
                driver.get(eventUrl);
                WebElement element;

                element = driver.findElement(By
                        .xpath(XPATH_FOR_EVENT_TITLE));
                String eventTitle = element.getText();

                element = driver.findElement(By
                        .xpath(XPATH_FOR_EVENT_DATE));
                String eventDate = element.getText();

                element = driver.findElement(By
                        .xpath(XPATH_FOR_EVENT_DESCRIPTION));
                String eventDescr = element.getText();
                LOGGER.trace("page: {}, title: {}, time: {}, description: {}",
                        eventUrl, eventTitle, eventDate, eventDescr);

                URL url;
                try {
                    url = new URL(eventUrl);
                } catch (MalformedURLException e) {
                    LOGGER.error("Event URL: {} is malformed", eventUrl);
                    url = null;
                }
                Tusovka t = new Tusovka()
                        .setName(eventTitle)
                        .setDateStr(eventDate)
                        .setDate(FacebookParserUtils.parseEventDate(eventDate))
                        .setDescription(eventDescr)
                        .setPlace(place)
                        .setLink(url);
                tusovkas.add(t);
            }
        } catch (WebDriverException e) {
            LOGGER.error("Error when parsing page: {}", driver.getCurrentUrl(), e);
        }
        return tusovkas;
    }

    @Override
    public Place resolvePlace(String placeDomain) {
        if (isNotLoggedIn()) login();

        try {
            driver.get(FB_PAGE_LINK + placeDomain);
            String placeName = driver.getTitle();

            WebElement addrElement = driver.findElement(By
                    .xpath(XPATH_FOR_ADDRESS));
            String address = addrElement.getText();

            return new Place()
                    .setName(placeName)
                    .setFacebookDomain(placeDomain)
                    .setAddress(address)
                    .setTusovkas(new ArrayList<>());
        } catch (WebDriverException e) {
            LOGGER.error("Error when parsing page: {}", driver.getCurrentUrl(), e);
        }
        return null;

    }

    private void login() {
        String email = env.getProperty("fb.login");
        String pass = env.getProperty("fb.pass");

        driver.get(FB_PAGE_LINK);
        driver.findElement(By.id("m_login_email")).click();
        driver.findElement(By.id("m_login_email"))
                .sendKeys(email);
        driver.findElement(By.name("pass")).click();
        driver.findElement(By.name("pass")).sendKeys(pass);
        driver.findElement(By.name("login")).click();
        try {
            driver.findElement(By.linkText("Not Now")).click();
        } catch (NoSuchElementException e) {
            // do nothing
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Trying to log in, got: {}", driver
                    .getPageSource());
        }
        if (isNotLoggedIn()) {
            LOGGER.warn("Did not login, current page: {} {}"
                    , driver.getCurrentUrl()
                    , driver.getTitle());
        }
    }

    private boolean isNotLoggedIn() {
        LOGGER.info("Not logged in: logging in to Facebook");
        driver.get(FB_PAGE_LINK);
        return driver.getTitle().contains(FB_LOG_IN_TITLE);
    }
}
