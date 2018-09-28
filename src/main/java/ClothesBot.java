import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import persist.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClothesBot extends TelegramLongPollingBot {
    DatabaseManager db =  new DatabaseManager();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            if(update.getMessage().getText().equals("/start")){
                int user_id = update.getMessage().getFrom().getId();
                String fname = update.getMessage().getFrom().getFirstName();
                String lname = update.getMessage().getFrom().getLastName();
                String uname = update.getMessage().getFrom().getUserName();
                db.addClientToDatabase(user_id,fname,lname,uname);
                sendMessage("This bot appears to be the clothes <i>shop</i>", update.getMessage().getChatId());
            }

            if(update.getMessage().getText().equals("/categories")){
                sendKeyboardCategories(update.getMessage().getChatId());
            }

            if (update.getMessage().getText().equals("Adidas")){

            }

            if (update.getMessage().getText().equals("Nike")){

            }

            if (update.getMessage().getText().equals("Puma")){

            }
        }
    }

    private void sendMessage(String msg, long chat_id){
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText(msg);
        try{
            execute(message);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void sendKeyboardCategories(long chatId) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setParseMode("HTML");
            message.setText("Here are all accessible <i>manufacturers</i>");
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            ArrayList<String> categories = db.getSuppliers();
            for (String s : categories)
                row.add(s);
            keyboard.add(row);
            keyboardMarkup.setKeyboard(keyboard);
            message.setReplyMarkup(keyboardMarkup);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
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