package unit;

import bot.ShopBot;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import persist.DatabaseManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseTesting {

    @Mock
    private DatabaseManager databaseManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

//    @BeforeClass
//    public void initTesting(){
//        databaseManager = new DatabaseManager();
//    }

//    @Test
//    public void testSuppliers() throws SQLException {
//        ArrayList<String> suppliers = databaseManager.getSuppliers();
//        ArrayList<String> actual = new ArrayList<>();
//        Collections.addAll(actual,"Adidas","Nike","Puma");
//        assertEquals(suppliers,actual);
//    }

    @Test
    public void testVerify() throws SQLException {
        System.out.println(databaseManager.userExists(252101265));
        when(databaseManager.userExists(252101265)).thenReturn(true);
        databaseManager.removeFromBasket(204890796,7);
        assertTrue(databaseManager.userExists(252101265));
        DatabaseManager db = new DatabaseManager();
        DatabaseManager spy = spy(db);
        System.out.println(spy.userExists(252101265));
//        verify(test).userExists(ArgumentMatchers.eq(204890796));
//        verify(test,times(2)).userExists(204890796);
    }

}
