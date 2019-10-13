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
import java.util.Random;
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

        try {
        	int indice = ultimo_indice();
			while(remainingTweets > 0) {
				Query query = new Query("nfl");
	            query.setCount(100);
	            QueryResult result = twitter.search(query);
	            tweets = result.getTweets();
	            Collections.reverse(tweets);
	            long ultimoIdTwitterInserido = ultimo_idTwitter();
	            	            
	            for (Status tweet : tweets) {
	            	if(!tweet.isRetweeted() && tweet.getId() > ultimoIdTwitterInserido) {
	            		
	            		Arquivo arquivo = testes_print(tweet);
	            		//Inserção Arquivo de Dados	
	            		System.out.println("último indice twitter: " + ultimoIdTwitterInserido);
	            		buffWriter.write(arquivo.getIdTwitter() + arquivo.getMensagem() + arquivo.getDataFormatada() + "\n");
	            		ultimoIdTwitterInserido = arquivo.getIdTwitter();
	            		
	            		//Inserção Arquivo de Índice
	            		String string_indicie = Integer.toString(indice);
	            		buffWriter_id.write(String.format("%6.6s", string_indicie) + arquivo.getIdTwitter() + "\n");
	            		System.out.println("indice: " + indice);
	            		indice++;
	            	}
	            	System.out.println("");
	            }
	            remainingTweets = remainingTweets - tweets.size();
			}
			
			writer.flush();
            buffWriter.close();
            writer.close();
            
            writer_id.flush();
            buffWriter_id.close();
            writer_id.close();
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        
	}

	private static Arquivo testes_print(Status tweet) {
		HashtagEntity[] hashtag =  tweet.getHashtagEntities();
		if(hashtag.length > 0)
			System.out.println("Hashtag:" + hashtag[0].getText());
		Arquivo arquivo = new Arquivo();
		arquivo.setIdtwitter(tweet.getId());
        arquivo.setMensagem(tweet.getText());
        arquivo.setData(tweet.getCreatedAt());
		System.out.println("id: " + arquivo.getIdTwitter());
		System.out.println("mensagem: " + arquivo.getMensagem());
		System.out.println("data: " + arquivo.getDataFormatada());
		
		return arquivo;
	
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
			
			if (ultimo.equals(String.valueOf('0')) )
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
	
	public static long ultimo_idTwitter()
	{	
	
		String ultimo = "0";
		try {
			InputStream file = new FileInputStream("teste.txt");
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
				System.out.println("Aqui tem erro: " + ultimo);
				ultimo = ultimo.substring(0,19);
			}
	
			 	
			buffer.close();
			file_reader.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ultimo = ultimo.trim();
	
		return Long.parseLong(ultimo);
	}
	
	
}