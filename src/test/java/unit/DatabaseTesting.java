package unit;

import org.junit.BeforeClass;
import org.junit.Test;
import persist.DatabaseManager;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseTesting {

    private static DatabaseManager databaseManager;

    @BeforeClass
    public static void initTesting(){
        databaseManager = new DatabaseManager();
    }

    @Test
    public void testSuppliers() throws SQLException {
        ArrayList<String> suppliers = databaseManager.getSuppliers();
        ArrayList<String> actual = new ArrayList<>();
        Collections.addAll(actual,"Adidas","Nike","Puma");
        assertEquals(suppliers,actual);
    }

}
