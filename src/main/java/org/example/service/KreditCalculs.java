package org.example.service;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;

public class KreditCalculs {
    public SendMessage getCreditCommand(Long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Bizda mikroqarzlarni 0 dan 50 million so'mgacha\n1 oydan 48 oygacha muddatda\nyillik foiz stavkasi 27.9% dan 29.9% gacha miqdorda olishingiz mumkin.");
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

    /**
     * Kreditni hisoblash funksiyasi.
     * @param sum
     * @param month
     * @param protsent
     * @param chatId
     * @return
     * @throws IOException
     */
    public SendDocument getKreditCalclus(double sum, int month, double protsent, Long chatId) throws IOException {

        SendDocument sendDocument = new SendDocument();

            //qoldiq summa
            double residual = sum;

            //Bir oylik asosiy qarz
            double principl_debt = sum/month;

        String path = "KreditHisoblari.pdf";
        String imgPath = "src/main/resources/images/sqb.png";
        String imgPath1 = "src/main/resources/images/logo600.png";

        ImageData imageData = ImageDataFactory.create(imgPath1);
        Image image = new Image(imageData);
        image.setHeight(5);


        PdfWriter pdfWriter = new PdfWriter(path);


        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();

        String headText = "Siz olmoqchi bo'lgan "+sum+" so'm "+month+" oyga yillik foiz stavkasi "
                +protsent+"% bo'lgan kreditingiz hisoboti.";
        Paragraph paragraph =new Paragraph(headText);
        paragraph.setFontColor(Color.BLACK);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setFontSize(14);
        paragraph.setMarginBottom(4);


        float colomunWidth[] = {100,100,100,100,100};
        String headerRow[] = {"OY","Qoldiq summasi","Asosiy qarz","Foiz to'lovi","Oylik to'lov"};
        Table table = new Table(colomunWidth);
        table.addCell(new Cell().add(headerRow[0]).setBackgroundColor(Color.GREEN));
        table.addCell(new Cell().add(headerRow[1]).setBackgroundColor(Color.GREEN));
        table.addCell(new Cell().add(headerRow[2]).setBackgroundColor(Color.GREEN));
        table.addCell(new Cell().add(headerRow[3]).setBackgroundColor(Color.GREEN));
        table.addCell(new Cell().add(headerRow[4]).setBackgroundColor(Color.GREEN));


            for (int i=1;i<=month;i++){
                //Yillik foiz summaning 1 oylik miqdori
                Double interest_payment = (residual*protsent/100)/12;
                //Bir oyda to'lanadigan summa
                Double monthly_payment = principl_debt+interest_payment;

                table.addCell(new Cell().add(String.valueOf(i)));
                table.addCell(new Cell().add(String.format("%.2f",residual)));
                table.addCell(new Cell().add(String.format("%.2f",principl_debt)));
                table.addCell(new Cell().add(String.format("%.2f",interest_payment)));
                table.addCell(new Cell().add(String.format("%.2f",monthly_payment)));

                //Har oylik qoldiq summani hisoblab borish.
                residual=residual-principl_debt;

            }

            Document document = new Document(pdfDocument);
            document.add(image);
            document.add(paragraph);
            document.add(table);
            document.close();

            sendDocument.setChatId(chatId);
            sendDocument.setDocument(new File("/media/coder/8AB228D9B228CB8F/java_project/telegram bot/telegramBotkeybord/"+path));
            return sendDocument;
    }
}
