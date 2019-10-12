package twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterApp {

	private static List<Status> tweets = new ArrayList<Status>();
	
	public static void main(String[] args) throws TwitterException, IOException {
		Configuracao config = new Configuracao();
		Twitter twitter = config.ObterConfiguracao();
		File file = new File("teste.txt");
        
     // creates a FileWriter Object
        FileWriter writer = new FileWriter(file,true); 
        BufferedWriter buffWriter = new BufferedWriter(writer);
		try {
			Arquivo arquivo = new Arquivo();
            Query query = new Query("nfl");
            query.setCount(100);
            QueryResult result = twitter.search(query);
            tweets = result.getTweets();
            for (Status tweet : tweets) {
            	if(!tweet.isRetweeted()) {
            		HashtagEntity[] hashtag =  tweet.getHashtagEntities();
            		if(hashtag.length > 0)
            			System.out.println("Hashtag:" + hashtag[0].getText());
	                arquivo.setIdtwitter(tweet.getId());
	                arquivo.setMensagem(tweet.getText());
	                arquivo.setData(tweet.getCreatedAt());
            		System.out.println("id: " + arquivo.getIdTwitter());
            		System.out.println("mensagem: " + arquivo.getMensagem());
            		System.out.println("data: " + arquivo.getDataFormatada());
        			
            		
            		buffWriter.write(arquivo.getIdTwitter() + arquivo.getMensagem() + arquivo.getDataFormatada() + "\n");
            	}
            }
            writer.flush();
            buffWriter.close();
            writer.close();
            
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
	}
}
