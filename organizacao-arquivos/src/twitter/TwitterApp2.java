package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Scanner;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterApp2 {

	private static List<Status> tweets = new ArrayList<Status>();
	private static List<Status> tweetsInseridos = new ArrayList<Status>();
	private RandomAccessFile arquivo;
	private static int ultimoIndice = 0;
	private Scanner scan2;
	// Listas arquivos
	private static List<Arquivo> arquivoDadosList = new ArrayList<Arquivo>();
	private static List<ArquivoIndice> arquivoIndiceList = new ArrayList<ArquivoIndice>();

	public static void main(String[] args)  {
		TwitterApp2 app = new TwitterApp2();
		app.menu();
	}

	private void menu() {
		int opcao = 0;
		scan2 = new Scanner(System.in);
		do {
			System.out.println("\t******MENU******\t");
			System.out.println("\nDigite uma opcao:\n");
			System.out.println("1 Coletar dados");
			System.out.println("2 Converter arquivos");
			System.out.println("3 Buscar tweet por Twitter_id");
			System.out.println("4 Buscar tweet por indice");
			System.out.println("5 Buscar tweets por hashtag");
			System.out.println("6 Busca time mais comentado");
			System.out.println("0 Para sair\n");
			opcao = this.scan2.nextInt();
			if (opcao > 6) {
				System.out.println("Opcao invalida");
				opcao = 0;
			}
			switch (opcao) {
			case 1:
				System.out.println("1 Coletar dados");
				try {
				fetchDados();
				}
				catch(IOException ww) {
					ww.printStackTrace();
					
				}
				catch(InterruptedException xx) {
					xx.printStackTrace();
				}
				break;
			case 2:
				System.out.println("2 Converter arquivos");
				ConverteArquivo convert = new ConverteArquivo();
				convert.serial();
				break;
			case 3:
				System.out.println("3 Buscar tweet por Twitter_id");
				System.out.println("Digite o indice que deseja:");
				scan2.nextLine();
				String tw_id = scan2.nextLine();
				Long twid = Long.parseLong(tw_id);
				System.out.println(twid);
				int idx1 = buscaBinaria(twid);
				buscaPorId(idx1);
				break;
			case 4:
				System.out.println("4 Buscar tweet por indice");
				System.out.println("Digite o indice que deseja:");
				int idx = scan2.nextInt();
				buscaPorId(idx);
				break;
			case 5:
					System.out.println("5 Buscar tweets por hashtag");
					scan2.nextLine();
					System.out.println("Digite a hashtag desejada");
					String hst = scan2.nextLine();
					System.out.println(hst);
					
				try {
					buscarTweetsPorHashtag(hst);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 6:
				System.out.println("6 Busca time mais comentado");
				try {
					mostrarTimeMaisComentado();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			scan2.nextLine();
		} while (opcao != 0);
	}
	private void fetchDados() throws IOException, InterruptedException {
		Configuracao config = new Configuracao();
		Twitter twitter = config.ObterConfiguracao();
		try {
			// Descomentar a linha abaixo apenas se o arquivo de hashtags ainda n�o foi
			// criado
			CriarArquivoHashtags();
			while (ultimoIndice < 120000) {
				if (PodeRealizarBusca(twitter)) {
					BuscarTweets(twitter);
					InserirArquivoDeDados();
					InserirArquivoIndice();
					InserirArquivoHashtags();
					arquivoDadosList.clear();
					arquivoIndiceList.clear();

					Thread.sleep(5000);
				} else {
					// Thread.sleep (ObterTempoEspera(twitter));
					System.out.println(ObterTempoEspera(twitter));
				}
			}

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}

	}
	private static boolean PodeRealizarBusca(Twitter twitter) throws TwitterException {
		RateLimitStatus status = twitter.getRateLimitStatus("search").get("/search/tweets");
		System.out.println("Buscar Restantes: " + status.getRemaining());
		return status.getRemaining() > 1;
	}

	private static int ObterTempoEspera(Twitter twitter) throws TwitterException {
		RateLimitStatus status = twitter.getRateLimitStatus("search").get("/search/tweets");
		return status.getSecondsUntilReset() * 1000;
	}

	private static void InserirArquivoDeDados() throws IOException {

		FileWriter writer = new FileWriter("teste.txt", true);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		tweetsInseridos.clear();

		long ultimoIdTwitterInserido = ultimo_idTwitter();
		for (Status tweet : tweets) {
			if (!tweet.isRetweet() && tweet.getId() > ultimoIdTwitterInserido) {
				Arquivo arquivo = new Arquivo(tweet);
				System.out.println("�ltimo indice twitter: " + ultimoIdTwitterInserido);
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
			ultimoIndice = indice;
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
		for (String hashtag : HashtagsTimes) {
			writer.write(String.format("%-14.14s", hashtag) + "\n");
		}

		writer.flush();
		buffWriter.close();
		writer.close();
	}

	private static List<String> ObterListaHashtagsTimes() {

		// Fonte:
		// https://ftw.usatoday.com/2019/09/2019-nfl-kickoff-every-nfl-teams-official-twitter-emoji-hashtag

		List<String> retorno = new ArrayList<String>();
		retorno.add("RedSea"); // Arizona Cardinals
		retorno.add("InBrotherhood"); // Atlanta Falcons
		retorno.add("RavensFlock"); // Baltimore Ravens
		retorno.add("GoBills"); // Buffalo Bills
		retorno.add("KeepPounding"); // Carolina Panthers
		retorno.add("Bears100"); // Chicago Bears
		retorno.add("SeizeTheDEY"); // Cincinnati Bengals
		retorno.add("Browns"); // Cleveland Browns
		retorno.add("DallasCowboys"); // Dallas Cowboys
		retorno.add("BroncosCountry");// Denver Broncos
		retorno.add("OnePride"); // Detroit Lions
		retorno.add("GoPackGo"); // Green Bay Packers
		retorno.add("WeAreTexans"); // Houston Texans
		retorno.add("Colts"); // Indianapolis Colts
		retorno.add("DUUUVAL"); // Jacksonville Jaguars
		retorno.add("ChiefsKingdom"); // Kansas City Chiefs
		retorno.add("BoltUp"); // Los Angeles Chargers
		retorno.add("LARams"); // Los Angeles Rams
		retorno.add("FinsUp"); // Miami Dolphins
		retorno.add("Skol"); // Minnesota Vikings
		retorno.add("GoPats"); // New England Patriots
		retorno.add("Saints"); // New Orleans Saints
		retorno.add("GiantsPride"); // New York Giants
		retorno.add("TakeFlight"); // New York Jets
		retorno.add("RaiderNation"); // Oakland Raiders
		retorno.add("FlyEaglesFly"); // Philadelphia Eagles
		retorno.add("HereWeGo"); // Pittsburgh Steelers
		retorno.add("GoNiners"); // San Francisco 49ers
		retorno.add("Seahawks"); // Seattle Seahawks
		retorno.add("GoBucs"); // Tampa Bay Buccaneers
		retorno.add("Titans"); // Tennessee Titans
		retorno.add("HTTR"); // Washington Redskins

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

			FileOutputStream fileOut = new FileOutputStream("arquivo_hashtags.txt");
			fileOut.write(inputStr.getBytes());
			fileOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String AtualizarLinhaArquivoHashtags(String linha) {
		String hashtag = linha.substring(0, 14).trim();

		for (Arquivo arquivo : arquivoDadosList) {
			List<String> hashtagsArquivo = Arrays.asList(arquivo.getHashtags().split(","));
			for (String hashtagArquivo : hashtagsArquivo) {
				if (hashtagArquivo.toUpperCase().equals(hashtag.toUpperCase()))
					linha = AdicionarNovoIndiceParaHashtag(arquivo, linha);
			}
		}

		return linha;
	}

	public static String AdicionarNovoIndiceParaHashtag(Arquivo arquivo, String linha) {
		int indice = 0;
		for (ArquivoIndice arquivoIndice : arquivoIndiceList) {
			if (arquivoIndice.getIdTwitter() == arquivo.getIdTwitter())
				indice = arquivoIndice.getIndice();
		}
		String hashtags = linha.substring(14);
		if (!hashtags.contains(Integer.toString(indice))) {
			if (hashtags.equals(""))
				return linha.concat(Integer.toString(indice));

			return linha.concat("," + Integer.toString(indice));
		}

		return linha;

	}

	public static void TabelaHash(HashTab tabela[]) {

		int max = 61;
		/*
		 * HashTab[] tabela = new HashTab[max]; for(int i=1;i<max;i++) { tabela[i] = new
		 * HashTab(); }
		 */
		int indice = 0;
		String data, dataConvertida;
		try {
			InputStream file = new FileInputStream("hash.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);

			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					indice++;
					data = line.substring(299, 309);
					if (data.substring(2, 3).contentEquals("/") && data.substring(5, 6).contentEquals("/")) {
						dataConvertida = data.substring(6, 10) + data.substring(3, 5) + data.substring(0, 2);

						if (tabela[indiceHash(Integer.parseInt(dataConvertida), max)].getIndice().equals("0"))// Ainda
																												// n�o
																												// tem
																												// valor
						{
							tabela[indiceHash(Integer.parseInt(dataConvertida), max)]
									.setIndice(Integer.toString(indice));
							tabela[indiceHash(Integer.parseInt(dataConvertida), max)].setData(data);
						} else {
							tabela[indiceHash(Integer.parseInt(dataConvertida), max)]
									.setIndice(tabela[indiceHash(Integer.parseInt(dataConvertida), max)].getIndice()
											+ "," + Integer.toString(indice));
						}
					}

				}
			}

			for (int i = 1; i < max; i++) {
				System.out.println(tabela[i].getData() + ";" + tabela[i].getIndice());
			}

			buffer.close();
			file_reader.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int indiceHash(int chave, int tamanho) {
		return chave % tamanho;
	}

	public void buscaPorId(int id) {
		RandomAccessFile registroleitura;
		try {
			registroleitura = new RandomAccessFile("testexxx.txt", "rw");
			registroleitura.seek((id-1) * 591);
			String leitura = registroleitura.readLine();
			System.out.println(leitura);
		} catch (IOException e) {
		}
	}
	public int filesize() {
		try {
			return (int) arquivo.length();
		} catch (IOException e) {

		}
		return 0;
	}

	public void seekArq(int indice) {
		try {
			
			arquivo.seek(590 * indice);
		} catch (IOException e) {
		}
	}
	public void Arquivo_Java() {
		try {
			arquivo = new RandomAccessFile("indicerandom.txt", "rw");
		} catch (IOException e) {
		}
	}


	private long buscaRegistro(long me) {
		try {
			
			String fatia = null;
			arquivo.seek(me);
			String leitura = arquivo.readLine();
			fatia = leitura.substring(6, 25);
			fatia = fatia.trim();
	//		System.out.println(Long.parseLong(fatia));
		//	System.out.println("\n");
//			System.out.println("Meio = "+me);
			return Long.parseLong(fatia);
		} catch (IOException x) {
			x.printStackTrace();
		}
		return 0;
	}

	public int buscaBinaria(long chave) {
		Arquivo_Java();
		long pIni = 0, pFim = filesize(), pMeio = pFim / 2;
		//System.out.println("ini =" + pIni + " pFim =" + pFim + " pMeio =" + pMeio+"\n");
		try {
		} catch (Exception err) {
			err.printStackTrace();
		}
		while (pIni < pFim && chave != buscaRegistro(pMeio)) {

			if (chave < buscaRegistro(pMeio)) {
				pFim = pMeio;
				pMeio = (pMeio / 590) / 2;
				pMeio = pMeio * 590 + pMeio;

			} else {
				pIni = pMeio;
				if (pIni + pFim < filesize() && pIni + pFim < 2*pMeio) {
					pMeio = (pIni + pFim) / 590;
					pMeio = pMeio * 590 + pMeio;
				} else {
					pMeio = (pIni + pFim) / 2 / 591;
					pMeio = pMeio * 590 + pMeio;
				}
			}
//			if(pMeio > pFim) {
//				pMeio = pFim -590;
//			}
		//	System.out.println("novo inicio = "+ pIni + " novo meio = " + pMeio + " novo fim = " + pFim );
		}
		Long indice = pMeio / 591;
		return Integer.valueOf(indice.toString())+1;
	}
	public void mostrarTimeMaisComentado() throws IOException {
		InputStream file;
		int indicesHashtagMaisComentada = 0;
		String hashtagMaisComentada = "";
		try {
			file = new FileInputStream("arquivo_hashtags.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);
			
			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					String hashtag = line.substring(0,14);
					List<String> indices = Arrays.asList(line.substring(15).split(","));
					if(indices.size() > indicesHashtagMaisComentada) {
						indicesHashtagMaisComentada = indices.size();
						hashtagMaisComentada = hashtag;
					}
				}
			}
			
			System.out.println("Hashtag: " + hashtagMaisComentada.trim() + " " + indicesHashtagMaisComentada + " tweets");

			buffer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void buscarTweetsPorHashtag(String hashtag) throws IOException {
		InputStream file;
		try {
			file = new FileInputStream("arquivo_hashtags.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);
			
			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null && line.substring(0,14).trim().toUpperCase().equals(hashtag.trim().toUpperCase())) {
					List<String> indices = Arrays.asList(line.substring(15).split(","));
					for(String indice : indices) {
						buscaPorId(Integer.parseInt(indice));
					}
				}
			}
			buffer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}