import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.api.objects.Message;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class TopBanksOfCitiesParser {
    private static final Logger LOGGER = Logger.getGlobal();
    private static final String url = "https://ru.myfin.by/currency/";
    private final String[] validCurrencyArr = new String[]{"usd", "eur", "gbp", "cny", "jpy"};
    //private static final String sortingPatternUrl = "?sort=buy_course_36";
    private Message message;

    public TopBanksOfCitiesParser(Message message) {
        this.message = message;
    }

    public String getBanksAndCurrency(Document document) throws IOException {

        byte counter = 0;
        String result = "";

        String header = document.select("body > div.container.page_cont > div.row > div.col-md-9.main-container.pos-r.ovf-hidden > div:nth-child(1) > div.content_i-head.content_i-head--datepicker-fix > div.wrapper-flex > div.wrapper-flex__title > h1").text();

        Elements elements = document.select("#g_bank_rates > table > tbody > tr");
        for (Element el : elements) {
            if (counter == 5)
                break;

            if (el.hasAttr("data-key")){
                counter++;

                String bankName = el.select("td.bank_name > a").text();
                double buyingCurrency = Double.parseDouble(el.select("td:nth-child(2)").text().replace(',','.'));
                double sellingCurrency = Double.parseDouble(el.select("td:nth-child(3)").text().replace(',','.'));

                result += bankName + "\n" + "Покупка: " + buyingCurrency + "\n" + "Продажа: " + sellingCurrency + "\n\n";
            }
        }
        if (result.equals("")){
            LOGGER.warning("No sell/buy current currency in current city");
            return "В данном городе нет банков продающих/покупающих данную валюту";
        }
        return header + "\n\n" + result + "\n\n";
    }

    public String getInfo() throws IOException {
        MessageParser parser = new MessageParser();
        Document xmlData = null;

        String currencyCode = parser.getCurrencyCodeFromMessage(message);
        String city = parser.getCityFromMessage(message);

        if (city.equals("getCityError")) {
            LOGGER.warning("No city error");
            return "Нет названия города";
        }
        if (!currencyCode.equals("no valute")){
            xmlData = Jsoup.connect(url + currencyCode + "/" + city).get();
            return getBanksAndCurrency(xmlData);
        }
        else {
            String result = "";
            for (int i = 0; i < validCurrencyArr.length; i++) {
               currencyCode = validCurrencyArr[i];
               xmlData = Jsoup.connect(url + currencyCode + "/" + city).get();
               result += getBanksAndCurrency(xmlData);
            }
            return result;
        }
    }
}
