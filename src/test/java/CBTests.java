import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CBTests {
    @Test
    public void wrongCBDateTest() throws IOException {
        String expected = "Неверно указана дата";
        CBParser cbParser = new CBParser("13/08/1981");
        String actual =  cbParser.getCurrencyValue();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void CBDateTest() throws IOException {
        String expected = "Курс по ЦБ за 13/08/2017:\n" +
                "1.0 GBP = 78.0689 RUB\n" +
                "1.0 USD = 60.1873 RUB\n" +
                "1.0 EUR = 70.7502 RUB\n" +
                "1.0 CNY = 9.02751 RUB\n" +
                "1.0 JPY = 0.552101 RUB";
        CBParser cbParser = new CBParser("13/08/2017");
        String actual = cbParser.getCurrencyValue();

        Assert.assertEquals(expected,actual);
    }

}
