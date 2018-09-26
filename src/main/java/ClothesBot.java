import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ClothesBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {


        }
    }

    @Override
    public String getBotUsername() {
        return "CurrencBot";
    }

    @Override
    public String getBotToken() {
        return "600404491:AAGrfXTfizjw43IcBNJlAId4xj8v6VMWDeQ";
    }
}