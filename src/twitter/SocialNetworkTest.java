/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.jupiter.api.Assertions.*;
import static twitter.SocialNetwork.guessFollowsGraph;
import static twitter.SocialNetwork.influencers;

import java.time.Instant;
import java.util.*;

import org.junit.jupiter.api.Test;

public class SocialNetworkTest {
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = guessFollowsGraph(new ArrayList<>());
        
        assertTrue(followsGraph.isEmpty());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = influencers(followsGraph);
        
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void testTweetsWithoutMentions() {
        List<Tweet> tweets = Arrays.asList(
                new Tweet(1, "user1", "This is a tweet without mentions.", Instant.now()),
                new Tweet(2, "user2", "Another tweet with no mentions.", Instant.now())
        );
        Map<String, Set<String>> result = guessFollowsGraph(tweets);
        assertTrue(result.isEmpty(), "Graph should be empty if there are no mentions.");
    }

    @Test
    public void testSingleMention() {
        List<Tweet> tweets = Collections.singletonList(
                new Tweet(1, "user1", "Hello @user2", Instant.now())
        );
        Map<String, Set<String>> result = guessFollowsGraph(tweets);
        assertEquals(Set.of("user2"), result.get("user1"), "user1 should be following user2.");
    }

    @Test
    public void testMultipleMentions() {
        List<Tweet> tweets = Collections.singletonList(
                new Tweet(1, "user1", "Hi @user2 @user3", Instant.now())
        );
        Map<String, Set<String>> result = guessFollowsGraph(tweets);
        assertEquals(Set.of("user2", "user3"), result.get("user1"), "user1 should follow user2 and user3.");
    }

    @Test
    public void testMultipleTweetsFromOneUser() {
        List<Tweet> tweets = Arrays.asList(
                new Tweet(1, "user1", "Hello @user2", Instant.now()),
                new Tweet(2, "user1", "Goodbye @user3", Instant.now())
        );
        Map<String, Set<String>> result = guessFollowsGraph(tweets);
        assertEquals(Set.of("user2", "user3"), result.get("user1"), "user1 should follow both user2 and user3.");
    }

    @Test
    public void testSingleUserWithoutFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>());
        List<String> influencers = influencers(followsGraph);
        assertTrue(influencers.isEmpty(), "No influencers should exist if no one has followers.");
    }

    @Test
    public void testSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", Set.of("user2"));
        List<String> influencers = influencers(followsGraph);
        assertEquals(List.of("user2"), influencers, "user2 should be the only influencer.");
    }

    @Test
    public void testMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", Set.of("user2", "user3"));
        followsGraph.put("user4", Set.of("user3"));
        List<String> influencers = influencers(followsGraph);
        assertEquals(List.of("user3", "user2"), influencers, "user3 should be first as they have more followers.");
    }

    @Test
    public void testTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", Set.of("user2"));
        followsGraph.put("user3", Set.of("user2", "user4"));
        followsGraph.put("user5", Set.of("user4"));
        List<String> influencers = influencers(followsGraph);
        assertEquals(List.of("user2", "user4"), influencers, "user2 and user4 should both be influencers, user2 first.");
    }
}
