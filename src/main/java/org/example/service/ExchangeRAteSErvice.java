package org.example.service;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.entity.ExchangeRateApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRAteSErvice {

    public SendMessage getExchangeRate(Long chatId){
      String rateInfo = "";
        String country_code[] = {"EUR","USD","RUB","KZT"};
        String country_flag[] = {"\uD83D\uDCB6","\uD83C\uDDFA\uD83C\uDDF8","\uD83C\uDDF7\uD83C\uDDFA","\uD83C\uDDF0\uD83C\uDDFF"};
        int count = 0;
        for (String code:country_code) {
            String urlstr = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/"+code+"/2019-01-01/";
            try {
                HttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(urlstr);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity entity = httpResponse.getEntity();

                String responseString = EntityUtils.toString(entity);
                String substring = responseString.substring(1, responseString.length() - 1);

                Gson gson = new Gson();
                ExchangeRateApi rateApi = gson.fromJson(substring, ExchangeRateApi.class);
                count++;
                rateInfo+=count+". "+country_flag[count-1]
                        +"  "+rateApi.getCcyNm_UZ()
                        +"  "+ rateApi.getNominal()+"  <b>"+code
                        +"</b>   -   <b>"+rateApi.getRate()+"</b> UZS  \uD83C\uDDFA\uD83C\uDDFF\n";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sendText = "         Bugungi valyuta kursi      \n"+rateInfo;
        sendMessage.setText(sendText);
        sendMessage.setParseMode("HTML");
        return sendMessage;
    }
}
