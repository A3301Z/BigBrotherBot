package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class BigBrotherBot extends TelegramLongPollingBot {

    private static boolean theObjectIsSelected = false;

    public String getBotUsername() {
        return "ControlOfAttendance_Bot";

    }

    public String getBotToken() {
        return "6521254218:AAHENeMjcXr7R6jphZ2EoU0Oa6drKRK58u8";
    }

    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callbackHandler(update);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                commandHandler(update);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Сообщение не было отправлено: ", e);
            }
        }
    }

    public void commandHandler(Update update) throws TelegramApiException {
        Message message = update.getMessage();

        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity = message
                    .getEntities()
                    .stream().filter(e -> "bot_command".equals(e.getType()))
                    .findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                List<List<InlineKeyboardButton>> buttons2 = new ArrayList<>();

                for (ConstructionSite building : ConstructionSite.values()) {          // Кнопки объектов стройки
                    InlineKeyboardButton button = InlineKeyboardButton.builder()
                            .text(building.getBuildName())
                            .callbackData("Объект: " + building.getBuildName())
                            .build();
                    buttons.add(Collections.singletonList(button));
                }

                for (Employee value : Employee.values()) {          // Кнопки работников
                    InlineKeyboardButton button = InlineKeyboardButton.builder()
                            .text(value.getName())
                            .callbackData(value.getName())
                            .build();
                    buttons2.add(Collections.singletonList(button));
                }

                if (command.equals("/start")) {
                    execute(SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text("Выберите объект: ")
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());

                }
                for (ConstructionSite value : ConstructionSite.values()) {
                    if (callbackHandler(update).equals(value.getBuildName())) {
                        execute(SendMessage.builder()
                                .chatId(message.getChatId())
                                .text("Выберите работников из списка: ")
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons2).build())
                                .build());
                    }
                }
            }
        }
    }

    public String callbackHandler(Update update) {
        String result = "";
        if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();

            for (Employee value : Employee.values()) {
                if (data.equals(value.getName())) {
                    SendMessage responseMessage = new SendMessage(callbackQuery.getMessage().getChatId().toString(),
                            data + " задействован на объекте.");
                    try {
                        result = data + " задействован на объекте.";
                        execute(responseMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();

            if (data.startsWith("Объект: ")) {
                String selectedObject = data.replace("Объект: ", "");
                SendMessage responseMessage = new SendMessage(callbackQuery.getMessage().getChatId().toString(),
                        "Вы выбрали объект: " + selectedObject);
                try {
                    result = "Вы выбрали объект: " + selectedObject;
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
