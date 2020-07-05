import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingBot {

    private static final String botToken = "1074111622:AAGvryL9DZpIpOoi58eKMiBoSdVuUORGdg0";
    private static final Logger LOGGER = Logger.getGlobal();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            LOGGER.warning("Bot has not been registered.");
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
            LOGGER.warning("Sending message error.");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        MessageParser messageParser = new MessageParser();
        if (message != null && message.hasText()) {
            try {
                MessageParser.loadResources();
            } catch (FileNotFoundException e) {
                LOGGER.warning("Losing data error.");
            }
            try {
                sendMsg(message,messageParser.parseMessage(message));
            } catch (IOException e) {
                LOGGER.warning("Bot does not understand the message");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Lebowski Bot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
