package com.banditos.server.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix= "telegram")
public class BotConfigurationProperties {
    private String token;
    private String botname;

    public String getToken() {
        return token;
    }

    public String getBotname() {
        return botname;
    }

    public final Proxy proxy = new Proxy();

    public BotConfigurationProperties setBotname(String botname) {
        this.botname = botname;
        return this;
    }

    public BotConfigurationProperties setToken(String token) {
        this.token = token;
        return this;
    }

    public static class Proxy {
        private String host;
        private Integer port;
        private String username;
        private String password;

        public String getHost() {
            return host;
        }

        public Proxy setHost(String host) {
            this.host = host;
            return this;
        }

        public Integer getPort() {
            return port;
        }

        public Proxy setPort(Integer port) {
            this.port = port;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public Proxy setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Proxy setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
