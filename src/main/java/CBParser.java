import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;

public class CBParser {
    private static final String url = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    private final String[] validCurrencyArr = new String[]{"USD", "EUR", "GBP", "CNY", "JPY"};
    private String date;

    public CBParser(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getCurrencyValue() throws IOException {
        String result = "";
        Document xmlData = Jsoup.connect(url + getDate()).get();

        Elements currencies = xmlData.select("Valute");
        for (Element el: currencies) {
            String currencyCode = el.select("CharCode").text();
            if (Arrays.asList(validCurrencyArr).contains(currencyCode)) {
                double nominal = Double.parseDouble(el.select("Nominal").text());
                double value = Double.parseDouble(el.select("Value").text().replace(',','.'));

                result += "1.0 " + currencyCode + " = " + value/nominal + " RUB\n";
            }
        }

        return "Курс по ЦБ за " + getDate() + ":\n" + result;
    }
}
