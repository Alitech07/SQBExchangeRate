package org.example.controller;

import org.example.entity.User;
import org.example.entity.enums.Step;
import org.example.service.ExchangeRAteSErvice;
import org.example.service.KreditCalculs;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;

public class BotController extends TelegramLongPollingBot {

List<User> users = new LinkedList<>();
boolean isName = false;
boolean isLastName = false;
boolean isPasportSeria = false;
boolean isPhoneNumber = false;
boolean isPhoto = false;
boolean isSalary = false;

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        ExchangeRAteSErvice rate = new ExchangeRAteSErvice();
        KreditCalculs kreditCalculs = new KreditCalculs();

        if(update.hasMessage() && update.getMessage().hasText()){
            String id = message.getFrom().getId().toString();
            String userName = message.getFrom().getUserName();
            User user = chackUser(id, userName);
            System.out.println(user);
            if (message.getText().equals("/start")){
                GeneralController generalController = new GeneralController();
                SendMessage sendStartMessage = generalController.commandStart(message);
                soutMessage(sendStartMessage);
            }else if (message.getText().equals("/help")){
                GeneralController generalController = new GeneralController();
                SendMessage sendHelpMessage = generalController.commandHelp(message);

                soutMessage(sendHelpMessage);
            }else if (message.getText().equals("/settings")){
                GeneralController generalController = new GeneralController();
                SendMessage sendSettingMessage = generalController.commandSettings(message);
                soutMessage(sendSettingMessage);

            }
            if (user.getStep().equals(Step.EXCHANGE_RATE_CCY.name())){
                ExchangeRAteSErvice exchangeRAteSErvice = new ExchangeRAteSErvice();
                String ccy = message.getText();
                Long chatId = message.getChatId();
                SendMessage exchangeRate = exchangeRAteSErvice.getExchangeRate(ccy, chatId);
                System.out.println("EXCHANGE_RATE_CCY ISHLADI.");
                soutMessage(exchangeRate);
            } else if (user.getStep().equals(Step.MIKRO_DEBT.name())) {
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("tepadagi tugmalardan birini tanlang!");
                soutMessage(sendMessage);

            } else if (user.getStep().equals(Step.CALCULATOR.name())){
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
                    soutMessage(sendMessage);
                }
            } else if (user.getStep().equals(Step.GET_MIKRO_DEBT.name())) {

                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("Kredit summasi oyi va foizini kiriting ");

            }else if (user.getStep().equals(Step.REGISTRATION.name())){
                if (!isName){
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Iltimos ismingizni kiriting!");
                    soutMessage(sendMessage);
                   user.setFirstName(message.getText());
                    isName = true;
                }else if (!isLastName){
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Iltimos familyangizni kiriting!");
                    soutMessage(sendMessage);
                    user.setLastName(message.getText());
                    isLastName = true;
                } else if (!isPasportSeria) {
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Iltimos pasport seriasini kiriting!");
                    soutMessage(sendMessage);
                    user.setPasportSeria(message.getText());
                    isPasportSeria = true;
                }else if (!isPhoneNumber){
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Iltimos telefon raqamingizni kiriting!");
                    soutMessage(sendMessage);
                    user.setPhoneNumber(message.getText());
                    isPhoneNumber = true;
                } else if (!isPhoto) {
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Iltimos foto suratingizni yuboring!");
                    soutMessage(sendMessage);
                    isPhoto = true;
                }else if (!isSalary){
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Iltimos oylik maoshingizni kiriting!");
                    soutMessage(sendMessage);
                    user.setSalary(Double.parseDouble(message.getText()));
                    isSalary = true;
                }else {
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Rasmiylashtirish jarayoni tugadi.");
                    soutMessage(sendMessage);
                }
            }


        }else if (update.hasCallbackQuery()){
            String id = update.getCallbackQuery().getFrom().getId().toString();
            String userName = update.getCallbackQuery().getFrom().getUserName();
            User user = chackUser(id, userName);
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Long chatId = callbackQuery.getMessage().getChatId();

            if (callbackQuery.getData().equals("kurs")){

                SendMessage exchangeRate = rate.getExchangeRate(chatId);
                soutMessage(exchangeRate);

            } else if (callbackQuery.getData().equals("ccy_rate")) {

                sendMessage.setChatId(chatId);
                sendMessage.setText("Davlat valyuta kodini kiriting\n<b>Namuna</b>: USD ");
                sendMessage.setParseMode("HTML");
                user.setStep(Step.EXCHANGE_RATE_CCY.name());
                System.out.println(user.getStep());
                soutMessage(sendMessage);

            } else if (callbackQuery.getData().equals("kredit")){

                SendMessage creditCommand = kreditCalculs.getCreditCommand(chatId);
                user.setStep(Step.MIKRO_DEBT.name());
                System.out.println(user.getStep());
                soutMessage(creditCommand);

            } else if (callbackQuery.getData().equals("microDebt")) {


                user.setStep(Step.REGISTRATION.name());



            } else if (callbackQuery.getData().equals("calculs")) {

                sendMessage.setChatId(chatId);
                sendMessage.setText("Kredit summasi, muddatini va yillik foiz stavkasini(%) kiriting\nNamuna: 8000/5/30   so'm/oy/% kabi");
                user.setStep(Step.CALCULATOR.name());
                System.out.println(user.getStep());
                soutMessage(sendMessage);

            } else if (callbackQuery.getData().equals("registration")) {
                user.setStep(Step.REGISTRATION.name());
                System.out.println(user.getStep());
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "@Ra_qam_bot";
    }

    @Override
    public String getBotToken() {
        return "326999462:AAFQ6yU1gfLg6_FaQz4twztTqcaQU2HDiU";
    }

    /**
     * Foydalanuvchilarni tekshirish va listda bolmasa qo'shish.
     * @param id
     * @param username
     * @return
     */
    public User chackUser(String id,String username){
        if (!users.isEmpty()){
            for (User user:users){
                if (user.getId().equals(id)){
                    return user;
                }
                System.out.println(user.getId());
                System.out.println(id);
            }
        }
        User saveUser = new User();
            saveUser.setId(id);
            saveUser.setUsername(username);
            saveUser.setStep("START");
            users.add(saveUser);
        return saveUser;
    }

    /**
     * Botda xabarlarni ko'rish.
     * @param sendMessage
     */
    public void soutMessage(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
