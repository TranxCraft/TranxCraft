package com.wickedgaminguk.tranxcraft;

import net.pravian.bukkitlib.util.LoggerUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TCP_Twitter {

    private final TranxCraft plugin;

    public TCP_Twitter(TranxCraft plugin) {
        this.plugin = plugin;
    }

    private String CONSUMER_KEY;
    private String CONSUMER_KEY_SECRET;
    private String ACCESS_TOKEN;
    private String ACCESS_TOKEN_SECRET;

    public void tweet(String tweet) {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        AccessToken oathAccessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

        twitter.setOAuthAccessToken(oathAccessToken);
        
        try {
            LoggerUtils.info("Tweeting: " + tweet);
            twitter.updateStatus(tweet);
        }
        catch (TwitterException ex) {
            LoggerUtils.warning(plugin, "Failed to Submit Tweet.");
        }

        LoggerUtils.info(plugin, "Tweet Successfully Sent");
    }

    public void init() {
        CONSUMER_KEY = plugin.config.getString("consumer_key");
        CONSUMER_KEY_SECRET = plugin.config.getString("consumer_key_secret");
        ACCESS_TOKEN = plugin.config.getString("access_token");
        ACCESS_TOKEN_SECRET = plugin.config.getString("access_token_secret");
    }
}
