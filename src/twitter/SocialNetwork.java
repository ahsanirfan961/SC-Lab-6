package twitter;/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

import java.util.*;
import java.util.stream.Collectors;

/**
 * twitter.SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        // Create an empty map to store the follows graph
        Map<String, Set<String>> followsGraph = new HashMap<>();

        // Loop through each tweet in the list
        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor(); // Get the author of the tweet
            String tweetText = tweet.getText(); // Get the tweet's text

            // Split the tweet into words and find @-mentions
            Set<String> mentionedUsers = new HashSet<>();
            String[] words = tweetText.split(" "); // Split the tweet by spaces
            for (String word : words) {
                if (word.startsWith("@") && word.length() > 1) {
                    String mentionedUser = word.substring(1); // Remove "@" symbol
                    mentionedUsers.add(mentionedUser); // Add to the set of mentioned users
                }
            }

            // Update the follows graph
            for (String mentionedUser : mentionedUsers) {
                if (!mentionedUser.equalsIgnoreCase(author)) {
                    // Make sure the author doesn't follow themselves
                    followsGraph.putIfAbsent(author, new HashSet<>()); // If the author isn't in the map, add them
                    followsGraph.get(author).add(mentionedUser); // Add the mentioned user to the author's set
                }
            }
        }
        return followsGraph; // Return the follows graph
    }

    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        // Create a map to store the follower counts
        Map<String, Integer> followerCounts = new HashMap<>();

        // Traverse through the followsGraph to count followers
        for (Set<String> followers : followsGraph.values()) {
            for (String follower : followers) {
                followerCounts.put(follower, followerCounts.getOrDefault(follower, 0) + 1);
            }
        }

        // Sort users by follower count in descending order using stream API
        return followerCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
