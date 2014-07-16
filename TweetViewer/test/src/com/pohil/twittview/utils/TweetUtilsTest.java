package com.pohil.twittview.utils;

import android.test.AndroidTestCase;
import com.pohil.twittview.model.Segment;
import com.pohil.twittview.ui.TweetAdapter;
import com.pohil.twittview.utils.TweetUtils;

import java.util.ArrayList;


public class TweetUtilsTest extends AndroidTestCase {

    public void testCreateTweet() {
        String tag = TweetUtils.createTag("test");
        assertEquals("#test", tag);
        tag = TweetUtils.createTag("#tag");
        assertEquals("#tag", tag);
    }

    public void testParseTag() {
        String word = TweetUtils.parseTag("#test");
        assertEquals("test", word);
        word = TweetUtils.parseTag("test");
        assertEquals("test", word);
    }

    public void testFindRegex() {
        String text = "012#bbb #dd";
        ArrayList<Segment> segmentList = TweetUtils.findRegex(text, TweetAdapter.TAG_REGEX);
        assertEquals(segmentList.size(), 2);

        Segment segment = segmentList.get(0);
        assertEquals("#bbb", segment.getContent());
        assertEquals(3, segment.getStart());
        assertEquals(7, segment.getEnd());

        segment = segmentList.get(1);
        assertEquals("#dd", segment.getContent());
        assertEquals(8, segment.getStart());
        assertEquals(11, segment.getEnd());
    }
}
