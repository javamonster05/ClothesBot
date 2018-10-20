package mock;


import model.Basket;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class DBTestMocked {
    @Test
    public void test(){

    }

    public static void main(String[] args) {
        Basket b = mock(Basket.class);
    }
}
