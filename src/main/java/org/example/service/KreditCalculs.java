package org.example.service;


import org.example.controller.BotController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.floor;
import static java.lang.Math.ulp;

public class KreditCalculs {
    public SendMessage getCreditCommand(Long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Bizda kreditlarni 0 dan 50 million so'mgacha\n 1 oydan 48 oygacha muddatda\n yillik foiz stavkasi 27.9% dan 29.9% gacha miqdorda olishingiz mumkin.");
        InlineKeyboardButton cal =new InlineKeyboardButton();
        cal.setText("Kredit Kalkulyatori");
        cal.setCallbackData("calculs");

        List<InlineKeyboardButton> row = new LinkedList<>();
        row.add(cal);

        List<List<InlineKeyboardButton>> rowCollection = new LinkedList<>();
        rowCollection.add(row);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowCollection);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    public SendMessage getKreditCalclus(Double sum,Integer month, Integer protsent, Long chatId){
        SendMessage sendMessage = new SendMessage();
            Double residual = sum;
            double principl_debt = floor(sum/month);
            String table = "";

            for (int i=1;i<=month;i++){
                double interest_payment = floor((residual-residual*protsent/100)/12);
                residual-=principl_debt;
                double monthly_payment = principl_debt+interest_payment;

                if (i==1){
                    table +=
                            " <b>"+i+"</b> "
                            +" <b>"+sum+"</b> "
                            +" <b>"+principl_debt+"</b> "
                            +" <b>"+interest_payment+"</b> "
                            +" <b>"+monthly_payment+"</b> "
                            +"\n";
                }else {
                    table +=
                            " <b>"+i+"</b> "
                            +" <b>"+residual+"</b> "
                            +" <b>"+principl_debt+"</b> "
                            +"<b> "+interest_payment+" </b>"
                            +"<b> "+monthly_payment+" </b>"
                            +"\n";
                }

            }

            String text = "      <b><i>Kredit malumotlari</i></b>     \n"
                            +"<b> Oy </b>"
                            + "<b> Qoldiq summasi </b> "
                            +"<b> Asosiy qarz </b>"
                            +"<b> Foiz to'lovi </b> "
                            +"<b> Oylik to'lov </b>"
                        +"\n"
                        +table;


            sendMessage.setChatId(chatId);
            sendMessage.setText(text);
            sendMessage.setParseMode("HTML");
            return sendMessage;
    }
}
