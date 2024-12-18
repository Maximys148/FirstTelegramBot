package org.example.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.example.bot.config.BotConfig;
import org.example.bot.entity.User;
import org.example.bot.repository.UserRepository;
import org.glassfish.grizzly.http.util.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    @Autowired
    private final UserRepository repository;
    private static final String HELP_TEXT = "здесь я тебе расскажу что я умею \n\n" +
            "Команды \n" +
            "/start - перезапуск бота\n" +
            "/profile - ваш профиль\n" +
            "/delete_data - удалит ваш профиль\n" +
            "/help - покажет что умеет бот\n" +
            "/setting - изменяет настройки бота";

    Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    public TelegramBot(BotConfig botConfig, UserRepository userRepository, UserRepository repository) {
        this.botConfig = botConfig;
        this.repository = repository;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Поприветствовать бота"));
        listOfCommands.add(new BotCommand("/help", "Справка о всех командах бота"));
        listOfCommands.add(new BotCommand("/profile", "Ваш профиль"));
        listOfCommands.add(new BotCommand("/delete_data", "Удаляет информацию о вас у бота"));
        listOfCommands.add(new BotCommand("/setting", "Изменение настроек бота"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            logger.error("Ошибка с списке команд бота " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){  // Проверка на наличие сообщения и текста в нём
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            String firstName = update.getMessage().getChat().getFirstName();
            if(repository.findById(chatId).isPresent()) {
                User user = repository.findById(chatId).get();
                user.setCountMassage(user.getCountMassage() + 1);
                repository.save(user);
            }
            switch (messageText){
                case "/start":
                    registerUser(update.getMessage());
                    startCommand(chatId, firstName);
                    break;
                case "/help":
                    helpCommand(chatId);
                    break;
                case "/profile":
                    profileCommand(chatId);
                    break;
                case "/delete_data":
                    delete_dataCommand(chatId);
                    break;
                case "/setting":
                    settingCommand(chatId);
                    break;
                default:
                    sendMsg(chatId, "Извините, команда непонятна или функционал программы временно недоступен");
            }
        }
    }

    private void registerUser(Message message) {
        String chatId = message.getChatId().toString();
        if(repository.findById(chatId).isEmpty()){
            Chat chat = message.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setCountMassage(1);
            user.setRegisteredAtTime(new Date());
            repository.save(user);
            logger.info("User save: " + user);
        }
    }

    private void startCommand(String chatId, String firstName){
        String startCommandInfo = " Hello " + firstName + " " + HELP_TEXT;
        sendMsg(chatId, startCommandInfo);
    }
    private void helpCommand(String chatId){
        sendMsg(chatId, HELP_TEXT);
    }
    private void profileCommand(String chatId){
        User user = repository.findById(chatId).get();
        String lastName = user.getLastName();
        if(lastName == null){
            lastName = "Неизвестно";
        }
        String profileInfo = " Вот твой профиль \n\n" +
                "Имя: " + user.getFirstName() + "\n" +
                "Фамилия: " + lastName + "\n" +
                "Никнейм: " + user.getUserName() +"\n" +
                "Кол-во сообщений: " + user.getCountMassage() + "\n" +
                "Дата первого сообщений: " + user.getRegisteredAtTime() + "\n";
        sendMsg(chatId, profileInfo);
    }
    private void delete_dataCommand(String chatId){
        String startCommandInfo = " Delete ";
        sendMsg(chatId, startCommandInfo);
    }
    private void settingCommand(String chatId){
        String startCommandInfo = " Setting";
        sendMsg(chatId, startCommandInfo);
    }

    private void sendMsg(String chatId, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error occurred: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
