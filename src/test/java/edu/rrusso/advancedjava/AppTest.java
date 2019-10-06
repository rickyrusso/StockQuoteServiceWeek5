package edu.rrusso.advancedjava;

import org.junit.Test;

public class AppTest {


    //  test that no exception is thrown
    @Test
    public void testMain(){
        String[] params = new String[]{"GOOG", "08/19/2004", "02/03/2015"};
        App.main(params);
    }

    //  test that a NullPointerException is thrown
    @Test(expected = NullPointerException.class)
    public void testNullMain(){
        App.main(null);
    }
}
