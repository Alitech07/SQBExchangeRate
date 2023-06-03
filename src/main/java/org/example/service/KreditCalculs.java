package org.example.service;


import org.example.controller.BotController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.lang.Math.ulp;

public class KreditCalculs {
    public SendMessage getKreditCalclus(Double sum,Integer month,Long chatId){
        SendMessage sendMessage = new SendMessage();
        int protsent = month+2;
            Double total = sum+sum*protsent/100;
            String text = "----<b><i>Kredit malumotlari</i></b>----\n"+ "<b>Kredit summasi:</b> "+sum+" so'm \n"+"<b>Kredit Muddati:</b> "+month
                    +" oy\n<b>Kredit foizi miqdori:</b> "+protsent+" %"
                    +" \n<b>Jami summasi:</b> "+Math.floor(total)+" so'm \n"+"<b>1 oylik to'lov:</b> "+Math.floor(total/month)+" so'm";
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);
            sendMessage.setParseMode("HTML");
            return sendMessage;
    }
}
