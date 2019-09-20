package twitter;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApp {

	public static void main(String[] args) throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("0AhYPKEblgpFYZ0VNCutciw3s")
		  .setOAuthConsumerSecret("hpnrlyPSoYJiQJM6JL2i8Y99j4dXoYQEnCEy1EDyOsxAeHBroX")
		  .setOAuthAccessToken("1082398505770119168-tPVDuT0RFKs7vpSFjaqYVRU9MzmcPK")
		  .setOAuthAccessTokenSecret("MbPt5iTZyRFYyhDbSLPgLLG9DNjpNVP9ZeMvgj8frXGuz");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		try {
            Query query = new Query("#nfl");
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + "- location - " + tweet.getPlace() + "----" + tweet.getCreatedAt().toString());
            }
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
	}

}
