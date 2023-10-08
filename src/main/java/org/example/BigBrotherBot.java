package org.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class BigBrotherBot extends TelegramLongPollingBot {
    private Map<Long, Map<String, Integer>> userSelectedItems = new HashMap<>();
    private static List<String> selectedEmployee = new ArrayList<>();
    String building = "";

    public String getBotUsername() {
        return "ControlOfAttendance_Bot";
    }

    public String getBotToken() {
        return "6521254218:AAHENeMjcXr7R6jphZ2EoU0Oa6drKRK58u8";
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
        long chatID = update.getCallbackQuery().getMessage().getChatId();
        Map<String, Integer> selectedItems = userSelectedItems.getOrDefault(chatID, new HashMap<>());

        boolean clickBuildButton = false;

        for (String value : Keyboard.buildingMap.values()) {
            if (value.equals(data) && !data.equals("Готово")) {
                clickBuildButton = true;
                building = value;
                break;
            }
        }

        if (haveTwice(selectedEmployee)) {
            removeBothDuplicates(selectedEmployee);
        }

        if (clickBuildButton) {
            execute(EditMessageText.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text("Бригада: ")
                    .build());

            execute(AnswerCallbackQuery.builder()
                    .text("Выбран объект: " + building)
                    .callbackQueryId(update.getCallbackQuery().getId())
                    .build());

            execute(EditMessageReplyMarkup.builder()   // меняю клавиатуру
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.initEmployeeKeyboard()).build())
                    .build());
        }
        if (Keyboard.employee.containsValue(data)) {
            if (selectedItems.containsKey(data)) {
                selectedItems.remove(data);
                execute(EditMessageText.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .text("Бригада: " + "\n" + listToString(selectedEmployee))
                        .build());

                execute(EditMessageReplyMarkup.builder()   // меняю клавиатуру
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .chatId(update.getCallbackQuery().getMessage().getChatId())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.initEmployeeKeyboard()).build())
                        .build());
            } else {
                if (!data.equals("Готово")) {
                    selectedEmployee.add(data);

                    execute(EditMessageText.builder()
                            .chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                            .messageId(update.getCallbackQuery().getMessage().getMessageId())
                            .text("Бригада: " + "\n" + listToString(selectedEmployee))
                            .build());

                    execute(EditMessageReplyMarkup.builder()   // меняю клавиатуру
                            .messageId(update.getCallbackQuery().getMessage().getMessageId())
                            .chatId(update.getCallbackQuery().getMessage().getChatId())
                            .replyMarkup(InlineKeyboardMarkup.builder()
                                    .keyboard(Keyboard.initEmployeeKeyboard())
                                    .build())
                            .build());
                }
            }
        }


        if (data.equals("Готово")) {
            execute(EditMessageText.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
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
            execute(EditMessageText.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text("Объект: ")
                    .build());

            execute(EditMessageReplyMarkup.builder()   // меняю клавиатуру
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.initBuildingKeyboard()).build())
                    .build());
        }

        if (data.equals("Изменить состав бригады")) {
            selectedEmployee.clear();
            execute(EditMessageText.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text("Бригада: ")
                    .build());

            execute(EditMessageReplyMarkup.builder()   // меняю клавиатуру
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboard.initEmployeeKeyboard()).build())
                    .build());
        }

        if (data.equals("Отправить")) {
            String sender = "";

            if (update.getCallbackQuery().getFrom().getFirstName().isEmpty()) {
                sender = update.getCallbackQuery().getFrom().getUserName();
            } else {
                sender = update.getCallbackQuery().getFrom().getFirstName();
            }

            String finalPhrase = "Отправитель: https://t.me/" + sender + "\n" + "Объект: " + building + "\nБригада: \n" + listToString(selectedEmployee);

            execute(EditMessageText.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId()) // меняю сообщения
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text(finalPhrase)
                    .build());
        }
    }

    private static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            String entry = list.get(i);
            int number = i + 1;
            sb.append(number).append(") ").append(entry).append("\n");
        }

        return sb.toString();
    }

    public static boolean haveTwice(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    return true; // Найдено одинаковое значение
                }
            }
        }
        return false; // Одинаковых значений не найдено
    }

    public static void removeBothDuplicates(List<String> list) {
        List<String> tempList = new ArrayList<>(list);
        list.clear();
        for (String item : tempList) {
            if (tempList.indexOf(item) == tempList.lastIndexOf(item)) {
                list.add(item);
            }
        }
    }
}