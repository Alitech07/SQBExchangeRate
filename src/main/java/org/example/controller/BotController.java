package org.example.controller;

import org.example.service.ExchangeRAteSErvice;
import org.example.service.KreditCalculs;
import org.example.utils.InlineKeybordUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        }else if(update.hasMessage() & message.hasText()){
            if (message.getText().equals("/start")){
                GeneralController generalController = new GeneralController();
                SendMessage sendStartMessage = generalController.commandStart(message);
                try {
                    execute(sendStartMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }else if (message.getText().equals("/help")){
                GeneralController generalController = new GeneralController();
                SendMessage sendHelpMessage = generalController.commandHelp(message);

                try {
                    execute(sendHelpMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }else if (message.getText().equals("/settings")){
                GeneralController generalController = new GeneralController();
                SendMessage sendSettingMessage = generalController.commandSettings(message);
                try {
                    execute(sendSettingMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }



            try {
                String text = message.getText();
                String[] split = text.split("/");
                int credit = Integer.valueOf(split[0]);
                int month = Integer.valueOf(split[1]);
                double protsent = Double.parseDouble(split[2]);
                SendDocument sendDocument = kreditCalculs.getKreditCalclus(credit, month, protsent, message.getChatId());
                execute(sendDocument);
            }catch (Exception e){
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("Xato matin kiritldi.");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException ex) {
                    throw new RuntimeException(ex);
                }
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
