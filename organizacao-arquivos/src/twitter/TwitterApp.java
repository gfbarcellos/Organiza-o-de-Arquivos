package twitter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterApp {

	public static void main(String[] args) throws TwitterException, IOException {
		Configuracao config = new Configuracao();
		Twitter twitter = config.ObterConfiguracao();
		File file = new File("teste.txt");
        
        // creates the file
        file.createNewFile();
        
     // creates a FileWriter Object
        FileWriter writer = new FileWriter(file); 

		try {
			Arquivo arquivo = new Arquivo();
            Query query = new Query("nfl");
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();
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
        			
            		
            		
            		writer.write(Long.toString(arquivo.getIdTwitter()) + arquivo.getMensagem() + arquivo.getMensagem().length() + arquivo.getDataFormatada() + "\n");
            		
            	}
            }
            writer.flush();
            writer.close();
            
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
	}

}
