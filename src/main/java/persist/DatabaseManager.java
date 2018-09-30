package persist;

import model.Product;

import java.sql.*;
import java.util.ArrayList;

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
            preparedStatement.close();
            resultSet.next();
            int res = resultSet.getInt(1);
            resultSet.close();
            if (res == 1) return true;
            else return false;
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

    public ArrayList<Product> getProducts(int supplierId) throws SQLException{
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
        return result;
    }

    public void addToBasket(int productId, int customerId, int quantity) throws SQLException{
        String query = "INSERT INTO telegramshop.orderproduct (Product_ID, Customer_ID, Quantity) VALUES (?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,productId);
        preparedStatement.setInt(2,customerId);
        preparedStatement.setInt(3,quantity);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static void main(String[] args)throws SQLException {
        new DatabaseManager().addToBasket(2,252101265,3);
    }
}
