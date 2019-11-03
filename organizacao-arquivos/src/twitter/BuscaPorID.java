package twitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

public class BuscaPorID {
	private RandomAccessFile arquivo;
	public void buscaPorId(int id) {
		try {
			arquivo = new RandomAccessFile("testexxx.txt", "rw");
			arquivo.seek((id-1) * 591);
			String leitura = arquivo.readLine();
			System.out.println(leitura);
		} catch (IOException e) {
		}
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
	public static void main(String[] args) throws IOException {
		BuscaPorID id = new BuscaPorID();
		//id.buscaPorId(82128);
		//id.mostrarTimeMaisComentado();
		id.buscarTweetsPorHashtag("Browns");

	}

}
