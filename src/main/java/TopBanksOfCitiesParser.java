import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TopBanksOfCitiesParser {
    private static final String url = "https://ru.myfin.by/currency/";
    private final String[] validCurrencyArr = new String[]{"USD", "EUR", "GBP", "CNY", "JPY"};
    private String city;
    private String currency;

    public TopBanksOfCitiesParser(String city, String currency) {
        this.city = city;
        this.currency = currency;
    }

    public String getCity() {
        return city;
    }

    public String getCurrency() {
        return currency;
    }

    public String getBanksAndCurrency() throws IOException {
        byte counter = 0;
        String result = "";
        Document xmlData = Jsoup.connect(url + getCurrency() + "/" + getCity()).get();

        Elements elements = xmlData.select("#g_bank_rates > table > tbody > tr");
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
        return "Курс " + getCurrency() + " в городе " + getCity() + "\n\n" + result;
    }

    public static void main(String[] args) throws IOException {
        TopBanksOfCitiesParser parser = new TopBanksOfCitiesParser("moskva","eur");
        System.out.println(parser.getBanksAndCurrency());
    }
}
