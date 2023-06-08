package org.example.service;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.entity.ExchangeRateApi;
import org.example.utils.TableBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;

public class ExchangeRAteSErvice {

    /**
     * Valyuta kurslarini {USD,EURO,RUB,KZT} larni sumga nisbtini oladi.
     * @param chatId
     * @return SendMessage tipida malumot qaytardi.
     */
    public SendMessage getExchangeRate(Long chatId){
        LocalDate localDate = LocalDate.now();
        String country_code[] = {"EUR","USD","RUB","KZT"};
        String country_flag[] = {"\uD83D\uDCB6","\uD83C\uDDFA\uD83C\uDDF8","\uD83C\uDDF7\uD83C\uDDFA","\uD83C\uDDF0\uD83C\uDDFF"};
        int count = 0;
        String table = "";
        for (String code:country_code) {
            String urlstr = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/"+code+"/"+localDate+"/";
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
                  table +=count+". "
                          +country_flag[count-1]+" "
                          +rateApi.getCcyNm_UZ()+" <b>"
                          +rateApi.getNominal()+"</b> "
                          +code+" - <b>"
                          +rateApi.getRate()+"</b> UZS \n";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sendText = "\t\tBugungi <b>"+localDate+"</b> valyuta kursi   \n\n"+table;
        sendMessage.setText(sendText);
        sendMessage.setParseMode("HTML");
        return sendMessage;
    }

    public SendMessage getExchangeRate(String ccy,Long chatId){
        LocalDate localDate = LocalDate.now();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
            String urlstr = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/"+ccy+"/"+localDate+"/";
            try {
                HttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(urlstr);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity entity = httpResponse.getEntity();

                String responseString = EntityUtils.toString(entity);
                String substring = responseString.substring(1, responseString.length() - 1);

                Gson gson = new Gson();
                ExchangeRateApi rateApi = gson.fromJson(substring, ExchangeRateApi.class);
                String text=
                        rateApi.getCcyNm_UZ()
                        +"  "+ rateApi.getNominal()+"  <b>"
                        +"</b>   -   <b>"+rateApi.getRate()+"</b> UZS\n";
                String sendText = "\t\tBugungi valyuta kursi      \n\n";
                sendMessage.setText(sendText);
                sendMessage.setParseMode("HTML");
                return sendMessage;
            } catch (Exception e) {
                sendMessage.setText("Kiritilgan valyuta kodi xato!!!");
                return sendMessage;
            }
        }

}
