package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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
		File file_indice_id = new File("teste_indice_id.txt");
		
     // creates a FileWriter Object
        FileWriter writer = new FileWriter(file,true); 
        BufferedWriter buffWriter = new BufferedWriter(writer);
        
        FileWriter writer_id = new FileWriter(file_indice_id,true);
        BufferedWriter buffWriter_id = new BufferedWriter(writer_id);
        
        int remainingTweets = 1000;
        String ultimoIDInserido = "1183072758101663744";
		try {
			//while(remainingTweets > 0) {
				Arquivo arquivo = new Arquivo();
				Query query = new Query("nfl");
	            query.setCount(100);
	            QueryResult result = twitter.search(query);
	            tweets = result.getTweets();
	            Collections.reverse(tweets);
	            
	            int indice = ultimo_indice();
	            System.out.println("Último índice: " + indice);
	            
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
	            		
	            		String string_indicie = Integer.toString(indice);
	            		buffWriter_id.write(String.format("%6.6s", string_indicie) + arquivo.getIdTwitter() + "\n");
	            		System.out.println("indice: " + indice);
	            		indice++;
	            	}
	            }
	            writer.flush();
	            buffWriter.close();
	            writer.close();
	            
	            writer_id.flush();
	            buffWriter_id.close();
	            writer_id.close();
	            
	            remainingTweets--;
			//}
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
	}

	public static int ultimo_indice()
	{	
	
		String ultimo = "0";
		try {
			InputStream file = new FileInputStream("teste_indice_id.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);
	
			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					ultimo = line;
				}
			}
			
			if (ultimo.equals(String.valueOf('0')))
			{
				
			}
			else
			{
				ultimo = ultimo.substring(0, 6);
			}
	
			 	
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ultimo = ultimo.trim();
	
		return Integer.parseInt(ultimo);
	}
}