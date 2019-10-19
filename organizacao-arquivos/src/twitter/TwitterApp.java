package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterApp {

	private static List<Status> tweets = new ArrayList<Status>();
	private static List<Status> tweetsInseridos = new ArrayList<Status>();
	private RandomAccessFile arquivo;
	
	
	//Listas arquivos
	private static List<Arquivo> arquivoDadosList = new ArrayList<Arquivo>();
	private static List<ArquivoIndice> arquivoIndiceList = new ArrayList<ArquivoIndice>();

	public static void main(String[] args) throws TwitterException, IOException {
		Configuracao config = new Configuracao();
		Twitter twitter = config.ObterConfiguracao();

		int qntdBuscas = 14;
		try {
			
			//Descomentar a linha abaixo apenas se o arquivo de hashtags ainda não foi criado
			//CriarArquivoHashtags();
			
			while (qntdBuscas > 0) {
				BuscarTweets(twitter);
				InserirArquivoDeDados();
				InserirArquivoIndice();
				InserirArquivoHashtags();
				
				qntdBuscas--;
				arquivoDadosList.clear();
				arquivoIndiceList.clear();
			}
	
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}

	}

	private static void InserirArquivoDeDados() throws IOException {

		FileWriter writer = new FileWriter("teste.txt", true);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		tweetsInseridos.clear();

		long ultimoIdTwitterInserido = ultimo_idTwitter();
		for (Status tweet : tweets) {
			if (!tweet.isRetweet() && tweet.getId() > ultimoIdTwitterInserido) {
				Arquivo arquivo = new Arquivo(tweet);
				System.out.println("ï¿½ltimo indice twitter: " + ultimoIdTwitterInserido);
				buffWriter.write(arquivo.getIdTwitter() + arquivo.getMensagem() + arquivo.getDataFormatada()
						+ arquivo.getHashtags() + "\n");
				ultimoIdTwitterInserido = arquivo.getIdTwitter();

				tweetsInseridos.add(tweet);
				arquivoDadosList.add(arquivo);
			}
			// System.out.println("");
		}

		writer.flush();
		buffWriter.close();
		writer.close();
	}
	
 
	private static void InserirArquivoIndice() throws IOException {
		FileWriter writerIndc = new FileWriter("teste_indice_id.txt", true);
		BufferedWriter buffWriterIndc = new BufferedWriter(writerIndc);

		int indice = ultimo_indice();
		for (Status tweet : tweetsInseridos) {
			ArquivoIndice arquivoIndice = new ArquivoIndice(tweet, indice);
			String string_indice = Integer.toString(arquivoIndice.getIndice());
			buffWriterIndc.write(String.format("%6.6s", string_indice) + arquivoIndice.getIdTwitter() + "\n");
			System.out.println("indice: " + indice);
			indice++;
			
			arquivoIndiceList.add(arquivoIndice);
		}

		writerIndc.flush();
		buffWriterIndc.close();
		writerIndc.close();
	}
	
	private static void CriarArquivoHashtags() throws IOException {
		FileWriter writer;
		writer = new FileWriter("arquivo_hashtags.txt", true);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		List<String> HashtagsTimes = ObterListaHashtagsTimes();
		for(String hashtag : HashtagsTimes) {
			writer.write(String.format("%-14.14s", hashtag) + "\n");
		}
		
		writer.flush();
		buffWriter.close();
		writer.close();
	}
	
	private static List<String> ObterListaHashtagsTimes(){
		
		//Fonte: https://ftw.usatoday.com/2019/09/2019-nfl-kickoff-every-nfl-teams-official-twitter-emoji-hashtag
		
		List<String> retorno = new ArrayList<String>();
		retorno.add("RedSea");        // Arizona Cardinals
		retorno.add("InBrotherhood"); // Atlanta Falcons
		retorno.add("RavensFlock");   // Baltimore Ravens
		retorno.add("GoBills");       // Buffalo Bills
		retorno.add("KeepPounding");  // Carolina Panthers
		retorno.add("Bears100");      // Chicago Bears
		retorno.add("SeizeTheDEY");   // Cincinnati Bengals
		retorno.add("Browns");		  // Cleveland Browns
		retorno.add("DallasCowboys"); // Dallas Cowboys
		retorno.add("BroncosCountry");// Denver Broncos
		retorno.add("OnePride");      // Detroit Lions
		retorno.add("GoPackGo");	  // Green Bay Packers
		retorno.add("WeAreTexans");   // Houston Texans
		retorno.add("Colts");		  // Indianapolis Colts
		retorno.add("DUUUVAL");		  // Jacksonville Jaguars
		retorno.add("ChiefsKingdom"); // Kansas City Chiefs
		retorno.add("BoltUp");		  // Los Angeles Chargers
		retorno.add("LARams");		  // Los Angeles Rams
		retorno.add("FinsUp");		  // Miami Dolphins
		retorno.add("Skol");		  // Minnesota Vikings
		retorno.add("GoPats");		  // New England Patriots
		retorno.add("Saints");		  // New Orleans Saints
		retorno.add("GiantsPride");   // New York Giants
		retorno.add("TakeFlight");    // New York Jets
		retorno.add("RaiderNation");  // Oakland Raiders
		retorno.add("FlyEaglesFly");  // Philadelphia Eagles
		retorno.add("HereWeGo");      // Pittsburgh Steelers
		retorno.add("GoNiners");      // San Francisco 49ers
		retorno.add("Seahawks");      // Seattle Seahawks
		retorno.add("GoBucs");        // Tampa Bay Buccaneers
		retorno.add("Titans");        // Tennessee Titans
		retorno.add("HTTR");		  // Washington Redskins
		
		return retorno;
	}

	private static void BuscarTweets(Twitter twitter) throws TwitterException {
		Query query = new Query("nfl");
		query.setCount(100);
		QueryResult result = twitter.search(query);
		tweets = result.getTweets();
		Collections.reverse(tweets);

	}

	public static int ultimo_indice() {

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

			if (ultimo.equals(String.valueOf('0'))) {

			} else {
				ultimo = ultimo.substring(0, 6);
			}

			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ultimo = ultimo.trim();

		retorno = Integer.parseInt(ultimo);
		retorno++;
		return retorno;
	}

	public static long ultimo_idTwitter() {

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

			if (ultimo.equals(String.valueOf('0'))) {

			} else {
				ultimo = ultimo.substring(0, 19);
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

	public static void InserirArquivoHashtags() throws IOException {

		try {
			InputStream file = new FileInputStream("arquivo_hashtags.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);

			StringBuffer inputBuffer = new StringBuffer();
	        String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					inputBuffer.append(AtualizarLinhaArquivoHashtags(line));
					inputBuffer.append("\n");
				}
			}
			
			buffer.close();
			
			String inputStr = inputBuffer.toString();
			System.out.println(inputStr);
			
			
	        FileOutputStream fileOut = new FileOutputStream("arquivo_hashtags.txt");
	        fileOut.write(inputStr.getBytes());
	        fileOut.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static String AtualizarLinhaArquivoHashtags(String linha) {
		String hashtag = linha.substring(0,14).trim();
		
		for(Arquivo arquivo : arquivoDadosList) {
			List<String> hashtagsArquivo = Arrays.asList(arquivo.getHashtags().split(","));
			for(String hashtagArquivo : hashtagsArquivo) {
				if(hashtagArquivo.toUpperCase().equals(hashtag.toUpperCase()))
					linha = AdicionarNovoIndiceParaHashtag(arquivo, linha);
			}
		}
		
		return linha;
	}
	
	public static String AdicionarNovoIndiceParaHashtag(Arquivo arquivo, String linha) {
		int indice = 0;
		for(ArquivoIndice arquivoIndice : arquivoIndiceList) {
			if(arquivoIndice.getIdTwitter() == arquivo.getIdTwitter())
				indice = arquivoIndice.getIndice();
		}
		String hashtags = linha.substring(14);
		if(!hashtags.contains(Integer.toString(indice))) {
			if(hashtags.equals(""))
				return linha.concat(Integer.toString(indice));
			
			return linha.concat("," + Integer.toString(indice));
		}
		
		return linha;
		
	}
}