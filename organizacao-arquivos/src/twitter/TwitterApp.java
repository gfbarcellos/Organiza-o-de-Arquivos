package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	private static List<Status> tweetsInseridos = new ArrayList<Status>();
	private static List<String> hashtagList = new ArrayList();
	public static void main(String[] args) throws TwitterException, IOException {
		Configuracao config = new Configuracao();
		Twitter twitter = config.ObterConfiguracao();
        
		int remainingTweets = 100;
        
		try {
			
			BuscaArquivoHashtag();
			while(remainingTweets > 0) {
	            BuscarTweets(twitter);         
	            InserirArquivoDeDados();
	            InserirArquivoIndice();
	            
	            remainingTweets = remainingTweets - tweetsInseridos.size();
			}
			
			InserirArquivoHastags();
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
	}
	
	private static void InserirArquivoDeDados() throws IOException {
		
		FileWriter writer = new FileWriter("teste.txt",true); 
        BufferedWriter buffWriter = new BufferedWriter(writer);
        tweetsInseridos.clear();
        
		long ultimoIdTwitterInserido = ultimo_idTwitter();
        for (Status tweet : tweets) {
        	if(!tweet.isRetweet() && tweet.getId() > ultimoIdTwitterInserido) {
        		Arquivo arquivo = CriarModelo(tweet);
        		System.out.println("�ltimo indice twitter: " + ultimoIdTwitterInserido);
        		buffWriter.write(arquivo.getIdTwitter() + arquivo.getMensagem() + arquivo.getDataFormatada() + arquivo.getHashtags() + "\n");
        		ultimoIdTwitterInserido = arquivo.getIdTwitter();
        		
        		tweetsInseridos.add(tweet);
        		addListaHastags(arquivo.getHashtags());
        	}
        	System.out.println("");
        }
        
        
        writer.flush();
        buffWriter.close();
        writer.close();
	}
	
	
	private static void InserirArquivoIndice() throws IOException {
		FileWriter writerIndc = new FileWriter("teste_indice_id.txt",true); 
        BufferedWriter buffWriterIndc = new BufferedWriter(writerIndc);
        
        int indice = ultimo_indice();
		for (Status tweet : tweetsInseridos) {
    		Arquivo arquivo = CriarModelo(tweet);	            		
    		//Inser��o Arquivo de �ndice
    		String string_indicie = Integer.toString(indice);
    		buffWriterIndc.write(String.format("%6.6s", string_indicie) + arquivo.getIdTwitter() + "\n");
    		System.out.println("indice: " + indice);
    		indice++;
    		
    		System.out.println("");
        	
        }
		
		writerIndc.flush();
        buffWriterIndc.close();
        writerIndc.close();
	}
	
	private static void BuscarTweets(Twitter twitter) throws TwitterException{
		Query query = new Query("nfl");
        query.setCount(100);
        QueryResult result = twitter.search(query);
        tweets = result.getTweets();
        Collections.reverse(tweets);

	}

	private static Arquivo CriarModelo(Status tweet) {
		Arquivo arquivo = new Arquivo();
		arquivo.setIdtwitter(tweet.getId());
        arquivo.setMensagem(tweet.getText());
        arquivo.setData(tweet.getCreatedAt());
        arquivo.setHashtags(tweet.getHashtagEntities());
		System.out.println("id: " + arquivo.getIdTwitter());
		System.out.println("mensagem: " + arquivo.getMensagem());
		System.out.println("data: " + arquivo.getDataFormatada());
		System.out.println("Hashtags: " + arquivo.getHashtags());
		return arquivo;
	
	}

	public static int ultimo_indice()
	{	
	
		String ultimo = "0";
		int retorno = 0;
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
		
		
		retorno =  Integer.parseInt(ultimo);
		retorno++;
		return retorno;
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
	
	public static void addListaHastags( String hastags_juntas )
	{	
		//Recebe por par�metro a string com as hastags
		//Separa cada uma no vetor
		String hastags[] = hastags_juntas.split(",");
		//Itera para cada hastag
		for(int i = 0; i<hastags.length; i++)
		{
			if(!hastags[i].isEmpty())
			{
				//Insere a hastag na lista
				hashtagList.add(hastags[i]);
			}		
		}
	}
	
	public static void InserirArquivoHastags() throws IOException {
		String ultimo = "xxxx";
		FileWriter writerIndc = new FileWriter("teste_indice_hastag.txt",true); 
        BufferedWriter buffWriterIndc = new BufferedWriter(writerIndc);
        
        Collections.sort(hashtagList);
        
        for(String hashtag : hashtagList)
        {
        	
        	if(!ultimo.equals(hashtag))
			{
        		buffWriterIndc.write(String.format("%200.200s", hashtag) + "\n");
            	System.out.println("Hashtag: " + hashtag);;
			}
			ultimo = hashtag;

        }

		writerIndc.flush();
        buffWriterIndc.close();
        writerIndc.close();
	}
	
	public static void BuscaArquivoHashtag() throws IOException {
		
		String ultimo = " ";
		try {
			InputStream file = new FileInputStream("teste_indice_hastag.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);
	
			String line = " ";
			while (line != null) 
			{					
				line = buffer.readLine();
				if(line !=null ) {
					System.out.println("Alou: " + line);
					ultimo = line.substring(0,200);
					ultimo = ultimo.trim();
					hashtagList.add(ultimo);
				}
			}
		 	
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}