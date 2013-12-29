package com.wickedgaminguk.TranxCraft;

import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TCP_Twitter {
    private final static String CONSUMER_KEY = TCP_Util.getConfigFile().getString("CONSUMER_KEY");
    private final static String CONSUMER_KEY_SECRET = TCP_Util.getConfigFile().getString("CONSUMER_KEY_SECRET");
    private final static String ACCESS_TOKEN = TCP_Util.getConfigFile().getString("ACCESS_TOKEN");
    private final static String ACCESS_TOKEN_SECRET = TCP_Util.getConfigFile().getString("ACCESS_TOKEN_SECRET");

    public static void tweet(String tweet) throws TwitterException, IOException {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        AccessToken oathAccessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

        twitter.setOAuthAccessToken(oathAccessToken);

        twitter.updateStatus(tweet);
        
        TCP_Log.info("Tweet Successfully Sent");
    }
}