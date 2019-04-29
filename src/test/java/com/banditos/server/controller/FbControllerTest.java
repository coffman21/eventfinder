package com.banditos.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class FbControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FbControllerTest.class);

    @Value(value = "${fb.pass:LOW2LOW2LOW}")
    private String pass;

    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        driver = new HtmlUnitDriver(true);
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void login() {
        driver.get("https://mobile.facebook.com/");
        driver.findElement(By.id("m_login_email")).click();
        driver.findElement(By.id("m_login_email"))
                .sendKeys("fire2low@rambler.ru");
        driver.findElement(By.name("pass")).click();
        driver.findElement(By.name("pass")).sendKeys(pass);
        driver.findElement(By.name("login")).click();
        try {
            driver.findElement(By.linkText("Not Now")).click();
        } catch (NoSuchElementException e) {
            // do nothing
        }

        driver.get("https://mobile.facebook.com/powerhousemoscow/events");
        List<String> eventHrefs = driver.findElements(By.xpath(
                "(//a[contains(text(),'View Event Details')])"))
                .stream()
                .map(e -> e.getAttribute("href"))
                .collect(Collectors.toList());

        for (int i = 0; i < 5; i++) {
            driver.get(eventHrefs.get(i));
            WebElement element;
            element = driver.findElement(By.xpath("//h3"));
            String title = element.getText();
            element = driver
                    .findElement(By.xpath("//div[@id='event_summary']/div/div/table/tbody/tr/td[2]/dt/div"));
            String time = element.getText();
            element = driver.findElement(By.xpath("//div[@id='event_tabs']/div[2]/div[2]/div[2]"));
            String descr = element.getText();

            LOGGER.info("page: {}", eventHrefs.get(i));
            LOGGER.info("title: {}", title);
            LOGGER.info("time: {}", time);
            LOGGER.info("descr: {}", descr);
        }
    }
}