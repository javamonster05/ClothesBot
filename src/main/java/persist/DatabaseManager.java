package persist;

import model.Basket;
import model.Order;
import model.Product;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;


public class DatabaseManager {

    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/telegramshop?verifyServerCertificate=false&useSSL=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","Dima","ilonalove123");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // for each object there is a connection
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addClientToDatabase(int id, String fname, String lname, String usname)  {
        try {
            if (userExists(id)) return;
            String query = "INSERT INTO telegramshop.customer (Customer_ID, First_Name, Last_Name, User_Name) VALUES " +
                    "(?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, fname);
            preparedStatement.setString(3, lname);
            preparedStatement.setString(4, usname);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean userExists(int id) throws SQLException{
            String query = "SELECT count(*) FROM telegramshop.customer where customer_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
//            preparedStatement.close();
            resultSet.next();
            int res = resultSet.getInt(1);
            resultSet.close();
            System.out.println(res);
            if (res > 0){
                System.out.println(res);
                return true;
            }
            return false;
    }

    public  ArrayList<String> getSuppliers() throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        String query = "SELECT Supplier_Name FROM telegramshop.supplier";
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            result.add(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
        return result;
    }

    public ArrayList<Product> getProductsBySupplierId(int supplierId) throws SQLException{
        ArrayList<Product> result = new ArrayList<>();
        String query ="SELECT Product_ID,Product_Name,Product_Price,Product_Description,Product_ImageId FROM telegramshop.product WHERE Supplier_ID = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,supplierId);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int price = resultSet.getInt(3);
            String description = resultSet.getString(4);
            String link = resultSet.getString(5);
            result.add(new Product(id, name, price, description, link));
        }
        preparedStatement.close();
        resultSet.close();
        return result;
    }

    public void addToBasket(int productId, long customerId, int quantity) throws SQLException{
        String query = "INSERT INTO telegramshop.orderproduct (Product_ID, Customer_ID, Quantity) VALUES (?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,productId);
        preparedStatement.setLong(2,customerId);
        preparedStatement.setInt(3,quantity);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public Basket getBasketByUserId(long userId) throws SQLException {
        Basket basket = new Basket();
        String query = "SELECT Product_ID , Quantity FROM telegramshop.orderproduct WHERE customer_id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1,userId);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            basket.getBasket().put(
                    resultSet.getInt(1),
                    resultSet.getInt(2));
        }
        resultSet.close();
        preparedStatement.close();
        return basket;
    }

    public ArrayList<Product> getProductsInBasket(Basket basket) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        Set<Integer> keySet = basket.getBasket().keySet();
        for(int i : keySet){
            products.add(getProductById(i));
        }
        return products;
    }

    public Product getProductById(int prodId) throws SQLException{
            Product product;
            String query = "SELECT Product_ID,Product_Name,Product_Price,Product_Description,Product_ImageId FROM telegramshop.product WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,prodId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int price = resultSet.getInt(3);
            String description = resultSet.getString(4);
            String link = resultSet.getString(5);
            product = new Product(id, name, price, description, link);
            resultSet.close();
            preparedStatement.close();
            return product;
    }

    public void removeFromBasket(long userId, int prodId) throws SQLException{
        String query = "DELETE FROM telegramshop.orderproduct WHERE product_id = ? AND customer_id = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,prodId);
        preparedStatement.setLong(2,userId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void changeQuantity(long userId, int prodId, int quantity) throws SQLException{
        String query = "UPDATE telegramshop.orderproduct SET Quantity = ? WHERE Customer_ID = ? AND Product_ID = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,quantity);
        preparedStatement.setLong(2,userId);
        preparedStatement.setInt(3,prodId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void makeOrder(long userId, String orderDate) throws SQLException {
        String query = "INSERT INTO telegramshop.order (Customer_ID, OrderDate) VALUES (?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, userId);
        preparedStatement.setString(2, orderDate);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public ArrayList<Order> getOrderHistory(long userId) throws SQLException{
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM telegramshop.order WHERE Customer_ID = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, userId);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int orderId = resultSet.getInt(1);
            long customerId = resultSet.getLong(2);
            String orderDate =  resultSet.getString(3);
            orders.add(new Order(orderId,customerId,orderDate));
        }
        return orders;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(new DatabaseManager().userExists(252101265));

    }
}
