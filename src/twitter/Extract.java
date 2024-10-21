package twitter;

import java.util.*;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Extract {

    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            return new Timespan(null, null);
        }

        Instant startTime = tweets.get(0).getTimestamp();
        Instant endTime = tweets.get(0).getTimestamp();

        for (Tweet tweet : tweets) {
            Instant timestamp = tweet.getTimestamp();
            if (timestamp.isBefore(startTime)) {
                startTime = timestamp;
            }
            if (timestamp.isAfter(endTime)) {
                endTime = timestamp;
            }
        }
        return new Timespan(startTime, endTime);
    }

    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Map<String, Integer> userMentionCount = new HashMap<>();
        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_]{1,15})(?![A-Za-z0-9_])");

        for (Tweet tweet : tweets) {
            Matcher matcher = mentionPattern.matcher(tweet.getText());
            while (matcher.find()) {
                String username = matcher.group(1).toLowerCase();
                userMentionCount.put(username, userMentionCount.getOrDefault(username, 0) + 1);
            }
        }

        return userMentionCount.keySet();
    }
}



