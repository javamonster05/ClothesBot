import model.Product;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import persist.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClothesBot extends TelegramLongPollingBot {

    private Object lock = new Object();
    private DatabaseManager db =  new DatabaseManager();
    private ArrayList<Product> products;
    private static int counterForward = 0;

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasCallbackQuery()){
            String callData = update.getCallbackQuery().getData();
            if(callData.equals("update_forward")){
                try {
                    if (counterForward >= products.size() - 1){
                        counterForward = -1;
                    }
                    counterForward++;
                    System.out.println(counterForward);
                    long chatId = update.getCallbackQuery().getFrom().getId();
                    Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                    EditMessageMedia editMessageMedia = new EditMessageMedia();
                    editMessageMedia.setChatId(chatId);
                    editMessageMedia.setMessageId(messageId);
                    editMessageMedia.setReplyMarkup(sendInlineKeyboard());
                    InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
                    inputMediaPhoto.setParseMode("HTML");
                    inputMediaPhoto.setMedia(products.get(counterForward).getImageLink());
                    String prodInfo = String.format("<i>  Name:  </i>"+ products.get(counterForward).getName() +"\n"
                            + "<i> Description:  </i>" + products.get(counterForward).getDescription() + "\n"
                            + "<b> Price:  </b>" + products.get(counterForward).getPrice());
                    inputMediaPhoto.setCaption(prodInfo);
                    editMessageMedia.setMedia(inputMediaPhoto);
                    execute(editMessageMedia);
                    if(counterForward >= products.size()-1) counterForward = -1;
                    System.out.println(counterForward);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    counterForward =0;
                }catch (NullPointerException e){
                    sendMessage("Select category!",update.getCallbackQuery().getFrom().getId());
                }
            }
            if(callData.equals("update_back")){
                try {
                    if(counterForward == -1){
                        counterForward = 2;
                    }
                if (counterForward == 0){
                    counterForward = products.size();

                }
                    counterForward--;
                    System.out.println(counterForward);
                    long chatId = update.getCallbackQuery().getFrom().getId();
                    Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                    EditMessageMedia editMessageMedia = new EditMessageMedia();
                    editMessageMedia.setChatId(chatId);
                    editMessageMedia.setMessageId(messageId);
                    editMessageMedia.setReplyMarkup(sendInlineKeyboard());
                    InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
                    inputMediaPhoto.setParseMode("HTML");
                    inputMediaPhoto.setMedia(products.get(counterForward).getImageLink());
                    String prodInfo = "<i>  Name:  </i>"+ products.get(counterForward).getName() +"\n"
                        + "<i> Description:  </i>" + products.get(counterForward).getDescription() + "\n"
                        + "<b> Price:  </b>" + products.get(counterForward).getPrice();
                    inputMediaPhoto.setCaption(prodInfo);
                    editMessageMedia.setMedia(inputMediaPhoto);
                    execute(editMessageMedia);
                    System.out.println(counterForward);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(callData.equals("add_to_basket")){
                long chatId = update.getCallbackQuery().getFrom().getId();
                sendMessage("How many do you want? Send number from <b> 1 </b> to <b> 9 </b>",chatId);
            }

            if(callData.equals("back_to_categories")){
                sendKeyboardCategories(update.getCallbackQuery().getFrom().getId());
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {

            if(update.getMessage().getText().equals("/start")){
                int user_id = update.getMessage().getFrom().getId();
                String fname = update.getMessage().getFrom().getFirstName();
                String lname = update.getMessage().getFrom().getLastName();
                String uname = update.getMessage().getFrom().getUserName();
                db.addClientToDatabase(user_id,fname,lname,uname);
                sendMessage("This bot appears to be the clothes <i>shop</i>", update.getMessage().getChatId());
            }

            if(update.getMessage().getText().matches(".*[123456789].*")){
                synchronized (lock) {
                    try {
                        String input = update.getMessage().getText();
                        int prodId;
                        if (counterForward == -1) {
                            prodId = products.get(products.size() - 1).getId();
                        } else {
                            prodId = products.get(counterForward).getId();
                        }
                        int custId = update.getMessage().getFrom().getId();
                        int quantity = Integer.parseInt(input);
                        db.addToBasket(prodId, custId, quantity);
                        sendMessage("<i> Added to basket </i>", update.getMessage().getChat().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(update.getMessage().getText().equals("/categories")){
                sendKeyboardCategories(update.getMessage().getChatId());
            }

            if(update.getMessage().getText().equals("/cabinet")){
                sendKeyboardCabinet(update.getMessage().getChatId());
            }

            if (update.getMessage().getText().equals("Adidas")){
                try {
                    counterForward = 0;
                    sendInlineKeyboadProducts(update.getMessage().getChatId(),db.getProductsBySupplierId(1));
                    products = db.getProductsBySupplierId(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (update.getMessage().getText().equals("Nike")){
                try {
                    counterForward =0;
                    sendInlineKeyboadProducts(update.getMessage().getChatId(),db.getProductsBySupplierId(2));
                    products = db.getProductsBySupplierId(2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (update.getMessage().getText().equals("Puma")){
                try {
                    counterForward=0;
                    sendInlineKeyboadProducts(update.getMessage().getChatId(),db.getProductsBySupplierId(3));
                    products = db.getProductsBySupplierId(3);
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
        SendMessage message = new SendMessage();
                message.setParseMode("HTML");
                message.setChatId(chatId);
                message.setText(msg);
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
            keyboardMarkup.setResizeKeyboard(true);
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            ArrayList<String> categories = db.getSuppliers();
            for (String s : categories)
                row.add(s); // TODO add distribution of rows of products
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
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove();
        Product first = products.get(counterForward);
        SendMessage message = new SendMessage();
        message.setParseMode("HTML");
        message.setReplyMarkup(remove);
        message.setText("<b>here are all products in this category</b>");
        message.setChatId(chatId);
        SendPhoto message1 = new SendPhoto();
        message1.setChatId(chatId);
        message1.setParseMode("HTML");
        String prodInfo = "<i> Name: </i>"+ first.getName() +"\n"
        + "<i> Description: </i>" + first.getDescription() + "\n"
        + "<b> Price: </b>" + first.getPrice();
        message1.setCaption(prodInfo);
        message1.setPhoto(first.getImageLink());
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(new InlineKeyboardButton().setText("<--").setCallbackData("update_back"));
        firstRow.add(new InlineKeyboardButton().setText("add to basket").setCallbackData("add_to_basket"));
        firstRow.add(new InlineKeyboardButton().setText("-->").setCallbackData("update_forward"));
        keyboard.add(firstRow);
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        secondRow.add(new InlineKeyboardButton().setText("Back to categories").setCallbackData("back_to_categories"));
        keyboard.add(secondRow);
        markup.setKeyboard(keyboard);
        message1.setReplyMarkup(markup);
        try {
            execute(message);
            execute(message1);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendInlineKeyboardBasket(long chatId, ArrayList<Product> products){
         // TODO make the inline keyboard in order to show full basket for user
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

    private InlineKeyboardMarkup sendInlineKeyboard(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton().setText("<--").setCallbackData("update_back"));
        row.add(new InlineKeyboardButton().setText("add to basket").setCallbackData("add_to_basket"));
        row.add(new InlineKeyboardButton().setText("-->").setCallbackData("update_forward"));
        rows.add(row);
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(new InlineKeyboardButton().setText("Back to categories").setCallbackData("back_to_categories"));
        rows.add(row1);
        markup.setKeyboard(rows);
        return markup;
    }

    private void sendKeyboardCabinet(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setParseMode("HTML");
        message.setText("Select whether to see the <i> basket </i> or <i> order history </i>");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Basket");
        row.add("Order history");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
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