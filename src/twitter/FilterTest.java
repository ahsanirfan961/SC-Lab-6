package twitter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;


class FilterTest {
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");

        assertEquals(1, writtenBy.size());
        assertTrue(writtenBy.contains(tweet1));
    }

    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));

        assertFalse(inTimespan.isEmpty());
        assertTrue(inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals(0, inTimespan.indexOf(tweet1));
    }

    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));

        assertFalse(containing.isEmpty());
        assertTrue(containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals(0, containing.indexOf(tweet1));
    }

    @Test
    public void testContainingNoMatch() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("apple"));
        assertTrue(containing.isEmpty());
    }
}