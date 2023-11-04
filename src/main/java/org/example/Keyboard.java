package org.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class Keyboard {
    protected static Map<Integer, String> employee = new HashMap<>();
    protected static Map<Integer, String> buildingMap = new HashMap<>();
    protected static Map<Integer, String> readyBackMap = new HashMap<>();

    public static List<List<InlineKeyboardButton>> initBuildingKeyboard() {
        List<List<InlineKeyboardButton>> buildingKeyboard = new ArrayList<>();

        buildingMap.put(1, "ЖК Суворов");
        buildingMap.put(2, "ТЦ Доваторцев 65");
        buildingMap.put(3, "Автомойка Доваторцев 65");
        buildingMap.put(4, "Герцена 147");
        buildingMap.put(5, "Южный Обход 1/2");
        buildingMap.put(6, "Роддом");

        for (String value : buildingMap.values()) {
            buildingKeyboard.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .callbackData(value)
                    .text(value)
                    .build()));
        }
        return buildingKeyboard;
    }

    public static List<List<InlineKeyboardButton>> initEmployeeKeyboard() {
        List<List<InlineKeyboardButton>> employeeKeyboard = new ArrayList<>();

        employee.put(1, "Акинин О.");
        employee.put(2, "Гостищев М.");
        employee.put(3, "Гусаров А.");
        employee.put(4, "Ермаков Д.");
        employee.put(5, "Новоселов А.");
        employee.put(6, "Скиба А.");
        employee.put(7, "Толстенев А.");
        employee.put(8, "Шушпанников А.");
        employee.put(9, "Жидков М.");
        employee.put(10, "Ивакин Ю.");
        employee.put(11, "Коваленко В.");
        employee.put(12, "Мартынов К.");
        employee.put(13, "Моисеенко А.");
        employee.put(14, "Панасейко Ю.");
        employee.put(15, "Подольский В.");
        employee.put(16, "Труфанов В.");
        employee.put(17, "Готово");
        employee.put(18, "Назад");

        int columnCount = 2; // Количество столбцов
        int rowCount = (int) Math.ceil(employee.size() / (double) columnCount);

        for (int i = 0; i < rowCount; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < columnCount; j++) {
                int index = i * columnCount + j + 1;

                if (index > employee.size()) {
                    break;
                }

                String value = employee.get(index);
                row.add(InlineKeyboardButton.builder()
                        .callbackData(value)
                        .text(value)
                        .build());
            }
            employeeKeyboard.add(row);
        }

        return employeeKeyboard;
    }

    public static List<List<InlineKeyboardButton>> readyBackKeyboard() {
        List<List<InlineKeyboardButton>> readyBackKeyboard = new ArrayList<>();

        readyBackMap.put(1, "Изменить объект");
        readyBackMap.put(2, "Изменить состав бригады");
        readyBackMap.put(3, "Отправить");

        for (String value : readyBackMap.values()) {
            readyBackKeyboard.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .callbackData(value)
                    .text(value)
                    .build()));
        }
        return readyBackKeyboard;
    }
}





