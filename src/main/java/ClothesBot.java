import model.Product;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import persist.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClothesBot extends TelegramLongPollingBot {
    DatabaseManager db =  new DatabaseManager();

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {



            if(update.hasCallbackQuery()){

            }

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
                try {
                    sendInlineKeyboadProducts(update.getMessage().getChatId(),db.getProducts(1));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (update.getMessage().getText().equals("Nike")){
                try {
                    sendInlineKeyboadProducts(update.getMessage().getChatId(),db.getProducts(2));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (update.getMessage().getText().equals("Puma")){
                try {
                    sendInlineKeyboadProducts(update.getMessage().getChatId(),db.getProducts(3));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(update.hasMessage() && update.getMessage().hasPhoto()) {
            sendPhotoId(update.getMessage().getChatId(), update.getMessage().getPhoto());
        }
    }

    private void sendMessage(String msg, long chatId){
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg);
        try{
            execute(message);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendKeyboardCategories(long chatId) {
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

    private void sendInlineKeyboadProducts(long chatId, ArrayList<Product> products){
        Product first = products.get(1);
        SendMessage message = new SendMessage();
        message.setParseMode("HTML");
        message.setText("<b>here are all products in this category</b>");
        message.setChatId(chatId);
        SendPhoto message1 = new SendPhoto();
        message1.setChatId(chatId);
        message1.setParseMode("HTML");
        String prodInfo = String.format("<i> Name: </i>"+ first.getName() +"\n"
        + "<i> Description: </i>" + first.getDescription() + "\n"
        + "<b> Price: </b>" + first.getPrice());
        message1.setCaption(prodInfo);
        System.out.println(first.getImageLink());
        message1.setPhoto("AgADAgADv6kxG3bggUmujxXLgpg_UrXBtw4ABEKjPVPxDzQuriYEAAEC");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton().setText("<--").setCallbackData("update_back"));
        row.add(new InlineKeyboardButton().setText("-->").setCallbackData("update_forward"));
        rows.add(row);
        markup.setKeyboard(rows);
        message1.setReplyMarkup(markup);
        try {
            execute(message);
            execute(message1);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendPhotoId(long chatId, List<PhotoSize> photos){
        String f_id = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getFileId();
        // Width photo
        int f_width = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getWidth();
        // Heigth
        int f_height = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getHeight();
        String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
        SendPhoto msg = new SendPhoto()
                .setChatId(chatId)
                .setPhoto(f_id)
                .setCaption(caption);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
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