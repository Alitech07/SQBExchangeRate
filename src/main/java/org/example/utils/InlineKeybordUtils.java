package org.example.utils;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InlineKeybordUtils {
    /**
     * Button yaratish.
     * @param text
     * @param callbackData
     * @return
     */
    public InlineKeyboardButton createInlineButton(String text, String callbackData){
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    /**
     * Buttonlardan qator hosil qilish.
     * @param buttons
     * @return
     */
    public List<InlineKeyboardButton> row(InlineKeyboardButton... buttons){
        List<InlineKeyboardButton> row = new LinkedList<>();
        row.addAll(Arrays.asList(buttons));
        return row;
    }

    /**
     * Buttonlardan qatorlar hosil qilish.
     * @param rows
     * @return
     */
    public List<List<InlineKeyboardButton>> rowCollection(List<InlineKeyboardButton>... rows){
        List<List<InlineKeyboardButton>> rowCollection = new LinkedList<>();
        rowCollection.addAll(Arrays.asList(rows));
        return rowCollection;
    }

    /**
     * Markup button yaratish
     * @param rowCollection
     * @return
     */
    public InlineKeyboardMarkup markupButton(List<List<InlineKeyboardButton>> rowCollection){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(rowCollection);
        return keyboardMarkup;
    }
}
