package com.banditos.server.bot.config;

import com.banditos.server.bot.Bot;
import com.banditos.server.bot.BotMessageCreator;
import com.banditos.server.orm.TusovkaRepository;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
public class BotConfig {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BotConfig.class);

    /**
     * Initializing ApiContextInitializer. Used for creating BotSession, which
     * used when Bot inits.
     */
    @PostConstruct
    public void init() {
        ApiContextInitializer.init();
    }

    @Bean
    @Autowired
    public Bot bot(BotConfigurationProperties properties,
            BotMessageCreator botMessageCreator,
            DefaultBotOptions botOptions) {
        String proxyUser = properties.getProxy().getUsername();
        String proxyPass = properties.getProxy().getPassword();
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
        Bot bot = new Bot(properties, botMessageCreator, botOptions);

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

    /**
     * Set up Http proxy
     *
     * @return bot options for Bot constructor
     */
    @Bean
    @Autowired
    public DefaultBotOptions defaultBotOptions(BotConfigurationProperties properties) {
        DefaultBotOptions botOptions = ApiContext
                .getInstance(DefaultBotOptions.class);
        botOptions.setProxyHost(properties.getProxy().getHost());
        botOptions.setProxyPort(properties.getProxy().getPort());
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        return botOptions;
    }
}
