import org.junit.Assert;
import org.junit.Test;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;

public class TopBanksTests {
    @Test
    public void topBanksSearchTest() throws IOException {
        String expected = "Курс фунта в Уфе\n" +
                "\n" +
                "Сбербанк\n" +
                "Покупка: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "Продажа: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "\n" +
                "Банк ВТБ\n" +
                "Покупка: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "Продажа: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "\n" +
                "Альфа-Банк\n" +
                "Покупка: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "Продажа: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "\n" +
                "Росбанк\n" +
                "Покупка: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "Продажа: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "\n" +
                "Райффайзенбанк\n" +
                "Покупка: [0-9]{1,3}.{0,1}[0-9]{0,2}\n" +
                "Продажа: [0-9]{1,3}.{0,1}[0-9]{0,2}";

        Update update = new Update();
        Message message = update.getMessage();
        TopBanksOfCitiesParser citiesParser = new TopBanksOfCitiesParser(message);
        String actual = citiesParser.getInfo();

        Assert.assertEquals(expected,actual);
    }
}
