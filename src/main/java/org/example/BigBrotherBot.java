package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BigBrotherBot extends TelegramLongPollingBot {
    private String selectedObject = "";
    private final List<String> selectedEmployee = new ArrayList<>();

    public String getBotUsername() {
        return "ControlOfAttendance_Bot";
    }

    public String getBotToken() {
        return "6521254218:AAHENeMjcXr7R6jphZ2EoU0Oa6drKRK58u8";
    }

    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery() && !update.getCallbackQuery().getData().equals("\"✅Отправить✅\"")) {
            try {
                handleEntity(update);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Ошибка метода handleEntity: ", e);
            }
        } else {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException("Ошибка метода handleMessage: ", e);
            }
        }

        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("✅Отправить✅")) {
            try {
                endOf(update);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleMessage(Message message) throws TelegramApiException {
        if (message.hasText() && message.getText() != null) {

            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

            for (ConstructionSite value : ConstructionSite.values()) {
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(value.getBuildName())
                        .callbackData(value.getBuildName())
                        .build();
                buttons.add(Collections.singletonList(button));
            }

            String text = message.getText();

            if (text.contains("/start") || text.equals("Старт")) {
                execute(SendMessage.builder()
                        .chatId(message.getChatId()).text("*" + "Выберите объект: " + "*").parseMode("Markdown")
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }
        }
    }

    public void handleEntity(Update update) throws TelegramApiException {
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            for (ConstructionSite value : ConstructionSite.values()) {
                if (data.equals(value.getBuildName())) {
                    selectedObject = value.getBuildName();
                    List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

                    for (Employee people : Employee.values()) {
                        InlineKeyboardButton button = InlineKeyboardButton.builder()
                                .text(people.getName())
                                .callbackData(people.getName())
                                .build();
                        buttons.add(Collections.singletonList(button));
                    }

                    execute(SendMessage.builder()
                            .chatId(update.getCallbackQuery().getMessage().getChatId())
                            .text("*" + "Выберите сотрудника:  " + "*").parseMode("Markdown")
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
                }
            }
            if (!data.equals("✅Отправить✅")) {
                selectedEmployee.add(data);
            }
        }
    }

    public void endOf(Update update) throws TelegramApiException {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("✅Отправить✅")) {

            String emp = listToString(selectedEmployee);
            StringBuilder finalPhraseBuild = new StringBuilder();
            String chatID = update.getCallbackQuery().getMessage().getChatId().toString();

            finalPhraseBuild.append("*Объект: *").append(selectedObject).append("\n").append("*Бригада: \n*");
            finalPhraseBuild.append(emp);
            execute(SendMessage.builder().chatId(chatID).text(String.format(finalPhraseBuild.toString())).parseMode("Markdown").build());
            selectedEmployee.clear();
        }
    }

    public String listToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        int index = 0;

        for (String item : list) {
            boolean found = false;
            for (Employee employee : Employee.values()) {
                if (employee.getName().equals(item)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                result.append("\t\t\t\t").append(++index).append(") ").append(item).append("\n");
            }
        }
        return result.toString();
    }
}