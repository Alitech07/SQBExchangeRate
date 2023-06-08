package org.example.controller;

import org.example.utils.InlineKeybordUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class GeneralController {
    SendMessage sendMessage = new SendMessage();
    public SendMessage commandStart(Message message){
        sendMessage.setText(message.getChat().getFirstName()+" Botga hush kelibsiz \nBu bot orqali <b><i>SQB</i></b> bankdan kreditlar olishingiz mumkin\nShuningdek kunlik \uD83D\uDCB1 valyuta kurslarini korishingiz mumkin."
                +"\nBank depoziti <b style=\"color:red; font-size:1rem;\">2 000 000 000</b> so'm ");
        sendMessage.setChatId(message.getChat().getId());
        sendMessage.setParseMode("HTML");

        InlineKeybordUtils inlineKeybordUtils = new InlineKeybordUtils();

        InlineKeyboardButton kurs = inlineKeybordUtils.createInlineButton("Valyuta kursi", "kurs");
        InlineKeyboardButton kredit = inlineKeybordUtils.createInlineButton("Mikroqarz olish", "kredit");

        List<InlineKeyboardButton> row = inlineKeybordUtils.row(kurs, kredit);
        List<List<InlineKeyboardButton>> rowCollection = inlineKeybordUtils.rowCollection(row);
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeybordUtils.markupButton(rowCollection);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    public SendMessage commandHelp(Message message){
        sendMessage.setText("Sizga qanday yordam kerak.");
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }
    public SendMessage commandSettings(Message message){
        sendMessage.setText("Sozlamalar bo'limiga hush kelibsiz");
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }
}
