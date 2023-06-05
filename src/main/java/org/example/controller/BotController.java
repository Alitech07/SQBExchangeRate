package org.example.controller;

import org.example.service.ExchangeRAteSErvice;
import org.example.service.KreditCalculs;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;

public class BotController extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        ExchangeRAteSErvice rate = new ExchangeRAteSErvice();
        KreditCalculs kreditCalculs = new KreditCalculs();

        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Long chatId = callbackQuery.getMessage().getChatId();
            if (callbackQuery.getData().equals("kurs")){
                SendMessage exchangeRate = rate.getExchangeRate(chatId);
                try {
                    execute(exchangeRate);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }else if (callbackQuery.getData().equals("kredit")){
                SendMessage creditCommand = kreditCalculs.getCreditCommand(chatId);
                try {
                    execute(creditCommand);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (callbackQuery.getData().equals("calculs")) {
                sendMessage.setChatId(chatId);
                sendMessage.setText("Kredit summasi, muddatini va yillik foiz stavkasini(%) kiriting\nNamuna: 8000/5/30   so'm/oy/% kabi");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        if (message.getText().equals("/start")){
            sendMessage.setText(message.getChat().getFirstName()+" Botga hush kelibsiz \n Bu bot orqali <b><i>SQB</i></b> bankdan kreditlar olishingiz mumkin\n Shuningdek kunlik \uD83D\uDCB1 valyuta kurslarini korishingiz mumkin."
                    +"\nBank depoziti <b style=\"color:red; font-size:1rem;\">2 000 000 000</b> so'm ");
            sendMessage.setChatId(message.getChat().getId());
            sendMessage.setParseMode("HTML");

            InlineKeyboardButton menu =new InlineKeyboardButton();
            menu.setText("Valyuta kursi");
            menu.setCallbackData("kurs");

            InlineKeyboardButton kredit =new InlineKeyboardButton();
            kredit.setText("Kredit olish");
            kredit.setCallbackData("kredit");

            List<InlineKeyboardButton> row = new LinkedList<>();
            row.add(menu);
            row.add(kredit);

            List<List<InlineKeyboardButton>> rowCollection = new LinkedList<>();
            rowCollection.add(row);

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(rowCollection);

            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                String text = message.getText();
                String[] split = text.split("/");
                double credit = Double.parseDouble(split[0]);
                Integer month = Integer.valueOf(split[1]);
                Integer protsent = Integer.valueOf(split[2]);
                sendMessage = kreditCalculs.getKreditCalclus(credit, month, protsent, message.getChatId());
                execute(sendMessage);
            }catch (Exception e){
//                sendMessage.setChatId(message.getChatId());
//                sendMessage.setText("Xato matin kiritldi.");
//                try {
//                    execute(sendMessage);
//                } catch (TelegramApiException ex) {
//                    throw new RuntimeException(ex);
//                }
                e.printStackTrace();
            }

        }

    }

    @Override
    public String getBotUsername() {
        return "@Ra_qam_bot";
    }

    @Override
    public String getBotToken() {
        return "1326999462:AAFQ6yU1gfLg6_FaQz4twztTqcaQU2HDiNU";
    }
}
