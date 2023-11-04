package org.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.security.Key;

public class BigBrotherBot extends TelegramLongPollingBot {

    public String getBotUsername() {
        return "ControlOfAttendance_Bot";
    }

    public String getBotToken() {
        return "6521254218:AAHENeMjcXr7R6jphZ2EoU0Oa6drKRK58u8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            messageHandler(update.getMessage());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void messageHandler(Message message) throws TelegramApiException {
        String text = message.getText();
        long chatID = message.getChatId();

        if (text.trim().equalsIgnoreCase("/start")) {
            execute(SendMessage.builder()
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.initBuildingKeyboard()).build())
                    .chatId(chatID)
                    .text("Выберите объект:")
                    .build());
        }
    }
}