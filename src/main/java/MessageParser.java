import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.inject.internal.cglib.proxy.$Factory;
import org.telegram.telegrambots.api.objects.Message;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MessageParser {
    private static final Logger LOGGER = Logger.getGlobal();

    private static String[] words;
    private static final String[] cbPatterns = new String[]{"цб[а-я]{0,2}", "центробанк[а-я]?", "центральный[а-я,\\s]*"};
    private static final String dateRegex = "[0-9]{2}[\\/.][0-9]{2}[\\/.][0-9]{4}";
    private static HashMap<String,String> cities;
    private static HashMap<String,String> currencies;

    public static String[] getWordsList(Message message){
        words = message.getText().split(" ");
        return words;
    }

    public static void loadResources() throws FileNotFoundException {
        Gson gson = new Gson();
        cities = gson.fromJson(new JsonReader(new FileReader("./src/main/resources/Cities.json")), new TypeToken<HashMap<String,String>>(){}.getType());
        currencies = gson.fromJson(new JsonReader(new FileReader("./src/main/resources/CurrencyName.json")), new TypeToken<HashMap<String,String>>(){}.getType());
    }

    public String getCityFromMessage(Message message) throws FileNotFoundException {
        String[] potentialCities = getWordsList(message);
        String cityForURLFormat = "";
        Gson gson = new Gson();
        HashMap<String,String> cities = gson.fromJson(new JsonReader(new FileReader("./src/main/resources/Cities.json")), new TypeToken<HashMap<String,String>>(){}.getType());

        for (int i = 0; i < potentialCities.length; i++) {
            for (Map.Entry<String,String> el : cities.entrySet()) {
                if (potentialCities[i].toLowerCase().matches(el.getKey())){
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
                if (potentialCurrencies[i].toLowerCase().matches(el.getKey())){
                    currencyForURL =  el.getValue();
                }
            }
        }

        return currencyForURL.isEmpty() ? "no valute" : currencyForURL;
    }

    public String parseMessage(Message message) throws IOException {
        CBParser cbParser = new CBParser(getDate(message));
        TopBanksOfCitiesParser toBanks = new TopBanksOfCitiesParser(message);

        String[] wordsList = getWordsList(message);
        boolean isCB = false;
        boolean isCity = false;
        boolean isCurrency = false;
        boolean isDate = false;

        if (message.hasText()){
        for (String word : wordsList) {
            for (String s : cbPatterns){
                if (Pattern.matches(s,word.toLowerCase()))
                    isCB = true;
            }
            for (Map.Entry<String,String> el : currencies.entrySet()) {
                if (Pattern.matches(el.getKey(),word.toLowerCase()) || word.toLowerCase().contains("валют"))
                    isCurrency = true;
            }
            for (Map.Entry<String,String> el : cities.entrySet()) {
                if (Pattern.matches(el.getKey(),word.toLowerCase()))
                    isCity = true;
            }
            if (Pattern.matches(dateRegex,word))
                isDate = true;
        }

        if (isCB){
            if (isDate)
                cbParser.setDate(getDate(message));
            return cbParser.getCurrencyValue();
        }
        if (isCity){
            return toBanks.getInfo();
        }
            switch (message.getText()) {
                case "/start" :
                    return "Привет, чем я могу помочь?\nP.S.Для получения справки касательно функционала введите /help";
                case "/help" :
                    return  "Основные команды для работы со мной:\n1./start: Приветствие(начало работы)\n2./info: Возможности бота\n3./help: Краткая справка относительно команд\n4./valutes: Список валют\n5./centralBank : Курс по ЦБ";
                case "/info" :
                    return "Я могу:\n1.Вывести курсы валют согласно ЦБ. Если необходимо вывести курс за какую-либо определенную дату, введите её в следующем формате: dd/mm/yyyy\n\n2.Показать список банков(Топ 5) в определенном регионе с самыми выгодными условиями для обмена определенной валюты";
                case "/valutes" :
                    return "Список фигурирующих валют:\n1.Американский доллар(USD)\n2.Евро(EUR)\n3.Фунт стерлингов Соединенного королевства(GBR)\n4.Китайский юань(CNY)\n5.Японская иена(JPY)";
                case "/centralBank" :
                    try {
                        return cbParser.getCurrencyValue() + "\nДля получения курса валют за определенную дату, введите ее в следующем формате: dd/mm/yyyy";
                    } catch (IOException e) {
                         LOGGER.warning("Cannot reach XML data");
                    }
            }
        }
        return "";
    }

    public String getDate(Message message){
        String[] potentialDate = getWordsList(message);
        String result = null;

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
