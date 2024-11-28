package org.example.bot.config;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.yml")
public class BotConfig {
    @Value("${bot.nickName}")
    private String botNickName;
    @Value("${bot.token}")
    private String botToken;
    public String getBotUsername() {
        return botNickName;
    }
    public String getBotToken() {
        return botToken;
    }
}
