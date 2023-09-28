package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BigBrotherBot extends TelegramLongPollingBot {

    int messageId = 0;

    List<String> selectedWorkers = new ArrayList<>();
    private Map<String, Boolean> buttonStates = new HashMap<>();

    public String getBotUsername() {
        return "ControlOfAttendance_Bot";
    }

    public String getBotToken() {
        return "6521254218:AAHENeMjcXr7R6jphZ2EoU0Oa6drKRK58u8";
    }

    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().getText().startsWith("/start")) {
            try {
                execute(SendMessage.builder().text("Бригада:")
                        .chatId(update.getMessage().getChatId())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttonsInit()).build())
                        .build());

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.hasCallbackQuery()) {
            messageId = update.getCallbackQuery().getMessage().getMessageId();
            try {
                callbackHandle(update);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            for (String selectedWorker : selectedWorkers) {
                try {
                    execute(SendMessage.builder().chatId(update.getMessage().getChatId()).text(selectedWorker).build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

/*    public List<List<InlineKeyboardButton>> buttonsInit() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        // Добавляем кнопки для работников из Employee
        for (Employee value : Employee.values()) {
            InlineKeyboardButton button = InlineKeyboardButton
                    .builder()
                    .callbackData(value.getName())
                    .text(value.getName())
                    .build();
            buttons.add(button);
            buttonStates.put(value.getName(), false);
        }

        // Разбиваем кнопки на ряды по две кнопки в каждом ряду
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i += 2) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(buttons.get(i));
            if (i + 1 < buttons.size()) {
                row.add(buttons.get(i + 1));
            }
            rows.add(row);
        }

        return rows;
    }  Рабочий метод ButtonsInit*/
/*    public List<List<InlineKeyboardButton>> buttonsInit() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        // Добавляем кнопки для работников из Employee
        for (Employee value : Employee.values()) {
            boolean isSelected = buttonStates.getOrDefault(value.getName(), false);
            String buttonText = isSelected ? " ✅" + value.getName() : "" + value.getName();

            InlineKeyboardButton button = InlineKeyboardButton
                    .builder()
                    .callbackData(value.getName())
                    .text(buttonText)
                    .build();
            buttons.add(button);
        }

        // Разбиваем кнопки на ряды по две кнопки в каждом ряду
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i += 2) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(buttons.get(i));
            if (i + 1 < buttons.size()) {
                row.add(buttons.get(i + 1));
            }
            rows.add(row);
        }

        return rows;
    }  ЛУШИЙ BUTTONINIT с обновлением состояния кнопок сотрудников*/
    public List<List<InlineKeyboardButton>> buttonsInit() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        // Добавляем кнопки для работников из Employee
        for (Employee value : Employee.values()) {
            boolean isSelected = buttonStates.getOrDefault(value.getName(), false);
            String buttonText = isSelected ? " ✅" + value.getName() : "" + value.getName();

            InlineKeyboardButton button = InlineKeyboardButton
                    .builder()
                    .callbackData(value.getName())
                    .text(buttonText)
                    .build();
            buttons.add(button);
        }

        // Разбиваем кнопки на ряды по две кнопки в каждом ряду
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i += 2) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(buttons.get(i));
            if (i + 1 < buttons.size()) {
                row.add(buttons.get(i + 1));
            }
            rows.add(row);
        }

        return rows;
    }

    private void callbackHandle(Update update) throws TelegramApiException {
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            String buttonText = "";
            boolean isSelected = false;

            if (buttonStates.containsKey(callbackData)) {
                isSelected = buttonStates.get(callbackData);
            }

            // Инвертируем состояние и обновляем текст кнопки
            isSelected = !isSelected;
            buttonStates.put(callbackData, isSelected);

            // Получаем текущий текст кнопки
            buttonText = isSelected ? " ✅" + callbackData : "" + callbackData;

            // Создаем объект EditMessageText только для обновления текста кнопки
            EditMessageText editMessageText = EditMessageText
                    .builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .text("Бригада:") // Не изменяем это сообщение
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboard(buttonsInit()) // Здесь вы можете использовать вашу текущую клавиатуру
                            .build())
                    .build();

            // Отправляем запрос на обновление текста кнопки
            execute(editMessageText);

            // Теперь обновляем текст кнопки
            EditMessageText editButton = EditMessageText
                    .builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .text("Бригада: ")
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboard(buttonsInit()) // Здесь вы можете использовать вашу текущую клавиатуру
                            .build())
                    .build();

            // Отправляем запрос на обновление текста кнопки
            execute(editButton);
        }
    }

//        if (update.hasCallbackQuery()) {
//            for (Employee value : Employee.values()) {
//                if (update.getCallbackQuery().getData().equals(value.getName())) {
//                    // Добавляем или удаляем работника в зависимости от текущего состояния
//                    boolean isWorkerSelected = selectedWorkers.contains(value.getName());
//                    if (isWorkerSelected) {
//                        selectedWorkers.remove(value.getName());
//                    } else {
//                        selectedWorkers.add(value.getName());
//                    }
//                    String buttonText = isWorkerSelected ? " ❌" + listToString(selectedWorkers) : " ✅" + value.getName();
//
//
//                    // Проверяем, должен ли текст кнопки измениться
//                    if (!buttonText.equals(update.getCallbackQuery().getMessage().getText())) {
//                        // Создаем объект EditMessageText для обновления сообщения
//                        EditMessageText editMessageText = EditMessageText
//                                .builder()
//                                .chatId(update.getCallbackQuery().getMessage().getChatId())
//                                .text(buttonText)
//                                .messageId(update.getCallbackQuery().getMessage().getMessageId())
//                                .replyMarkup(InlineKeyboardMarkup.builder()
//                                        .keyboard(buttonsInit()) // Здесь вы можете использовать вашу текущую клавиатуру
//                                        .build())
//                                .build();
//
//                        // Отправляем запрос на обновление сообщения
//                        execute(editMessageText);
//                    }
//                }
//            }
//        }
//    } ХЭНДЛ КОЛБЭК КОТОРЫЙ РАБОТАЕТ, НО ОБНОВЛЯЕТ ВЫБРАННЫХ СОТРУДНИКОВ, А НЕ ДОБАВЛЯЕТ ДРУГ К ДРУГУ
//private void callbackHandle(Update update) throws TelegramApiException {
//            if (update.hasCallbackQuery()) {
//                String callbackData = update.getCallbackQuery().getData();
//                String buttonText = "";
//                boolean isSelected = false;
//
//                if (buttonStates.containsKey(callbackData)) {
//                    isSelected = buttonStates.get(callbackData);
//                }
//
//                // Инвертируем состояние и обновляем текст кнопки
//                isSelected = !isSelected;
//                buttonStates.put(callbackData, isSelected);
//
//                // Обновляем текст кнопки в сообщении
//                buttonText = isSelected ? " ✅" + callbackData : "" + callbackData;
//
//                // Создаем объект EditMessageText для обновления сообщения
//                EditMessageText editMessageText = EditMessageText
//                        .builder()
//                        .chatId(update.getCallbackQuery().getMessage().getChatId())
//                        .text(buttonText)
//                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
//                        .replyMarkup(InlineKeyboardMarkup.builder()
//                                .keyboard(buttonsInit()) // Здесь вы можете использовать вашу текущую клавиатуру
//                                .build())
//                        .build();
//
//                // Отправляем запрос на обновление сообщения
//                execute(editMessageText);
//            }
//        } ЛУЧШИЙ НА ДАННЫЙ МОМЕНТ ХЭНДЛ КОЛБЭЕ, ОБНОВЛЯЕТ КНОПКИ, НО И ТЕКС ОБНОВЛЯЕТ (А ЭТОГО НЕ НАДО)
}