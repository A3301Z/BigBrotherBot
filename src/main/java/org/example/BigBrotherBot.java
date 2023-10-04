package org.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BigBrotherBot extends TelegramLongPollingBot {

    String building = "";

    public String getBotUsername() {
        return "ControlOfAttendance_Bot";
    }

    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
            try {
                callbackHandle(update);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Ошибка обработки CallBack: ", e);
            }

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                handler(update);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Ошибка обработки Message.text: ", e);
            }
        }
    }

    public void handler(Update update) throws TelegramApiException {
        Message message = update.getMessage();

        if (message.getText().contains("/start")) {
            execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId()).text("Объект: ")
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.initBuildingKeyboard()).build())
                    .build());
        }
    }

    public void callbackHandle(Update update) throws TelegramApiException {
        String data = update.getCallbackQuery().getData();
        boolean clickBuildButton = false;

        for (String value : Keyboard.buildingMap.values()) {
            if (value.equals(data) && !data.equals("Готово")) {
                clickBuildButton = true;
                building = value;
                break;
            }
        }

        if (clickBuildButton) {
            execute(EditMessageText.builder().chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text("Бригада: ")
                    .build());

            execute(EditMessageReplyMarkup.builder()   // меняю клавиатуру
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.initEmployeeKeyboard()).build())
                    .build());
        }
        if (data.equals("Готово")) {
            execute(EditMessageText.builder().chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text("Выберите действие: ")
                    .build());

            execute(EditMessageReplyMarkup.builder()
                    .messageId(update.getCallbackQuery().getMessage().getMessageId()) // меняю клавиатуру
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.readyBackKeyboard()).build())
                    .build());
        }
        if (data.equals("Изменить объект")) {
            execute(EditMessageText.builder().chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text("Объект: ")
                    .build());

            execute(EditMessageReplyMarkup.builder()   // меняю клавиатуру
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.initBuildingKeyboard()).build())
                    .build());
        } 
    }
}

