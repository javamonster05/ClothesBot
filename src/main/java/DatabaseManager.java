import java.sql.*;

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
            resultSet.next();
            int res = resultSet.getInt(1);
            if (res == 1) return true;
            else return false;
    }

    public static void main(String[] args) {
        try {
            System.out.println(new DatabaseManager().userExists(252101265));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
