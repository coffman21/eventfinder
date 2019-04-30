package com.banditos.server.parser;

import com.banditos.server.parser.vk.VkParser;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
public class ParserScheduler {

    @Autowired
    private VkParser vkParser;

    private Logger logger = LoggerFactory.getLogger(ParserScheduler.class);

    //@Scheduled(cron = "* 20 16 ? * *")
    @Scheduled(cron = "*/5 * * ? * *")
    public void runParsers() throws ClientException, ApiException, MalformedURLException {
        logger.trace("Scheduled task started");
//        vkParser.getTusovkas();
    }
}
