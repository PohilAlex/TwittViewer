package com.pohil.twittview.ui;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import com.pohil.twittview.R;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.pohil.twittview.ui.MainActivityTest \
 * com.pohil.twittview.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super("com.pohil.twittview", MainActivity.class);
    }

    MainActivity activity;
    Solo solo;
    ListView listView;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        listView = (ListView) activity.findViewById(R.id.tweet_list);
    }

    public void testBuildContentFragment() {
        assertNotNull( activity.getFragmentManager().findFragmentById(com.pohil.twittview.R.id.container));
    }

    public void testLoad() {
        solo.enterText(0, "android");

        solo.waitForCondition(new Condition() {
            @Override
            public boolean isSatisfied() {
                return listView.getVisibility() == View.VISIBLE;
            }
        }, 3000);
        assertEquals(View.VISIBLE, listView.getVisibility());
        assertTrue(listView.getAdapter().getCount() > 0);
        assertTrue(solo.searchText("#android"));
    }

}
