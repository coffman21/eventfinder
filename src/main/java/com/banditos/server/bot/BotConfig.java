package com.banditos.server.bot;

import com.banditos.server.orm.TusovkaRepository;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
public class BotConfig {

    static {
        ApiContextInitializer.init();
    }

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BotConfig.class);

    @Bean
    @Autowired
    public Bot bot(Environment env, BotMessageCreator botMessageCreator,
            DefaultBotOptions botOptions) {
        String proxyUser = env.getProperty("telegram.proxy.username");
        String proxyPass = env.getProperty("telegram.proxy.password");
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(proxyUser,
                        proxyPass.toCharArray());
            }
        });

        // Create the TelegramBotsApi object to register your bots
        TelegramBotsApi botsApi = new TelegramBotsApi();
        // Register your newly created AbilityBot
        Bot bot = new Bot(env, botMessageCreator, botOptions);

        try {
            botsApi.registerBot(bot);
            return bot;
        } catch (TelegramApiRequestException e) {
            LOGGER.error("Error while registering bot: ", e);
        }
        return null;
    }

    @Bean
    @Autowired
    public BotMessageCreator botMessageCreator(
            TusovkaRepository tusovkaRepository) {
        return new BotMessageCreator(tusovkaRepository);
    }

    @Bean
    public DefaultBotOptions defaultBotOptions(Environment env) {
        // Set up Http proxy
        DefaultBotOptions botOptions = ApiContext
                .getInstance(DefaultBotOptions.class);

        botOptions.setProxyHost(env.getProperty("telegram.proxy.host"));
        botOptions.setProxyPort(env.getProperty("telegram.proxy.port", Integer.class));
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        return botOptions;
    }
}
