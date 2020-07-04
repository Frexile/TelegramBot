import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.telegram.telegrambots.api.objects.Message;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class MessageParser {
    private static String[] potentialDate;
    private static final String[] cbPatterns = new String[]{"цб[а-я]{0,2}", "центробанк[а-я]?", "центральный[а-я,\\s]*", "курс[а-я]?"};
    private static final String dateRegex = "[0-9]{2}[\\/.][0-9]{2}[\\/.][0-9]{4}";


    public static String[] getWordsList(Message message){
        potentialDate = message.getText().split(" ");
        return potentialDate;
    }

    public String getCityFromMessage(Message message) throws FileNotFoundException {
        String[] potentialCities = getWordsList(message);
        String cityForURLFormat = "";
        Gson gson = new Gson();
        HashMap<String,String> cities = gson.fromJson(new JsonReader(new FileReader("./src/main/resources/Cities.json")), new TypeToken<HashMap<String,String>>(){}.getType());

        for (int i = 0; i < potentialCities.length; i++) {
            for (Map.Entry<String,String> el : cities.entrySet()) {
                if (potentialCities[i].matches(el.getKey())){
                    cityForURLFormat = el.getValue();
                }
            }
        }

        return cityForURLFormat.isEmpty() ? "getCityError" : cityForURLFormat;
    }

    public String getCurrencyCodeFromMessage(Message message) throws FileNotFoundException {
        String[] potentialCurrencies = getWordsList(message);
        String currencyForURL = "";
        Gson gson = new Gson();
        HashMap<String,String> currencies = gson.fromJson(new JsonReader(new FileReader("./src/main/resources/CurrencyName.json")), new TypeToken<HashMap<String,String>>(){}.getType());

        for (int i = 0; i < potentialCurrencies.length; i++) {
            for (Map.Entry<String,String> el : currencies.entrySet()) {
                if (potentialCurrencies[i].matches(el.getKey())){
                    currencyForURL =  el.getValue();
                }
            }
        }

        return currencyForURL.isEmpty() ? "no valute" : currencyForURL;
    }

    public String parseMessage(Message message) throws IOException {
        CBParser cbParser = new CBParser(getDate(message));

        if (message.hasText()){
            for (String s : cbPatterns) {
                if (Pattern.matches(s,message.getText())){
                    return cbParser.getCurrencyValue();
                }
                if (Pattern.matches(dateRegex, message.getText())){
                    cbParser.setDate(message.getText());
                    return cbParser.getCurrencyValue();
                }
            }
            //TODO пропиши для городов цикл поиска в сообщении по словам
            switch (message.getText()) {
                case "/start" :
                    return "Привет, чем я могу помочь?\nP.S.Для получения справки касательно функционала введите /help";
                case "/help" :
                    return  "Основные команды для работы со мной:\n1./start: Приветствие(начало работы)\n2./info: Возможности бота\n3./help: Краткая справка относительно команд\n4./valutes: Список валют\n5./centralBank : Курс по ЦБ";
                case "/info" :
                    return "Я могу:\n1.Вывести курсы валют согласно ЦБ\n2.Показать список банков в определенном регионе с самыми выгодными условиями для обмена определенной валюты";
                case "/valutes" :
                    return "Список фигурирующих валют:\n1.Американский доллар(USD)\n2.Евро(EUR)\n3.Фунт стерлингов Соединенного королевства(GBR)\n4.Китайский юань(CNY)\n5.Японская иена(JPY)";
                case "/centralBank" :
                    try {
                        return cbParser.getCurrencyValue() + "\nДля получения курса валют за определенную дату, введите ее в следующем формате: dd/mm/yyyy";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
        return "";
    }

    public String getDate(Message message){
        String[] potentialDate = getWordsList(message);
        String result = null;
        ;

        if (message.hasText()){
            for (int i = 0; i < potentialDate.length; i++) {
                if (Pattern.matches(dateRegex,potentialDate[i])){
                    result = potentialDate[i];
                }
            }
        }
        if (result == null){
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            result = simpleDateFormat.format(date);
        }

        return result;
    }
}
