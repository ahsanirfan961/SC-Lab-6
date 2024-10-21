package twitter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;


class ExtractTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));

        assertEquals(d1, timespan.getStart());
        assertEquals(d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanSingleTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals(d1, timespan.getStart());
        assertEquals(d1, timespan.getEnd());
    }

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue(mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersMixedCase() {
        Tweet tweetWithMixedCase = new Tweet(5, "user5", "Shoutout to @Alyssa and @ALYSSA!", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMixedCase));
        assertTrue(mentionedUsers.contains("alyssa"));
        assertEquals(1, mentionedUsers.size());
    }
}