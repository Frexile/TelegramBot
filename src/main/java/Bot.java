import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {

    private static final String botToken = "1074111622:AAGvryL9DZpIpOoi58eKMiBoSdVuUORGdg0";

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        //sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start" :
                    //TODO мб стоит добавить возможность вывода курса после старта
                    sendMsg(message, "Привет, чем я могу помочь?\nP.S.Для получения справки касательно функционала введите /help");
                    break;
                case "/help" :
                    sendMsg(message, "Основные команды для работы со мной:\n1./start: Приветствие(начало работы)\n2./info: Возможности бота\n3./help: Краткая справка относительно команд\n4./valutes: Список валют");
                    break;
                case "/info" :
                    sendMsg(message, "Я могу:\n1.Вывести курсы валют согласно ЦБ\n2.Показать список банков в определенном регионе с самыми выгодными условиями для обмена определенной валюты");
                    break;
                case "/valutes" :
                    sendMsg(message, "Список фигурирующих валют:\n1.Американский доллар(USD)\n2.Евро(EUR)\n3.Фунт стерлингов Соединенного королевства(GBR)\n4.Китайский юань(CNY)\n5.Японская иена(JPY)");
                    break;
                case "/centralBank" :
                    sendMsg(message, "");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "LebowskiBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
