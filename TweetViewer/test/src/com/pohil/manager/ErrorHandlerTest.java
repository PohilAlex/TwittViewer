package com.pohil.manager;

import com.android.volley.VolleyError;
import com.pohil.twittview.manager.ErrorHandler;
import junit.framework.TestCase;

public class ErrorHandlerTest  extends TestCase {

    ErrorHandler errorHandler;
    int count = 0;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        errorHandler = new ErrorHandler();
        count = 0;
    }

    public void testAddObserver() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        errorHandler.addObserver(observer1);
        errorHandler.addObserver(observer2);
        errorHandler.handleError(null, null);
        assertEquals(2, count);
    }

    public void testRemoveObserver() {
        TestObserver observer1 = new TestObserver();
        errorHandler.addObserver(observer1);
        errorHandler.handleError(null, null);
        assertEquals(1, count);
        errorHandler.removeObserver(observer1);
        errorHandler.handleError(null, null);
        assertEquals(1, count);
    }

    class TestObserver implements ErrorHandler.ErrorObserver {

        @Override
        public void onError(VolleyError error, Class request) {
            count++;
        }
    }
}
