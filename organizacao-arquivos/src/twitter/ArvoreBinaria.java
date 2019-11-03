package twitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ArvoreBinaria {
	
	private No raiz;
	
	public void inserir(String hashtag, String indices) {
        if (raiz == null) {
            raiz = new No(hashtag, indices);
        } else {
            No novo = new No(hashtag, indices);
            inserirNovo(raiz, novo);
        }
    }
	
	private void inserirNovo(No arvore, No novo) {
        if (novo.hashtag.compareTo( arvore.hashtag ) < 0 ) {
            if (arvore.direito == null) {
                arvore.direito = novo;
            } else {
                inserirNovo(arvore.direito, novo);
            }
        } else {
            if (arvore.esquerdo == null) {
                arvore.esquerdo = novo;
            } else {
                inserirNovo(arvore.esquerdo, novo);
            }
        }
    }
	
	public void pesquisaArvore(No arvore, String hashtag) 
	{
		String hashtag_convertida;
		if(arvore != null)
		{
			hashtag_convertida = arvore.hashtag.trim();
			if(hashtag_convertida.equals(hashtag))
			{
				System.out.println(arvore.indice); 
			}
			else
			{
				pesquisaArvore(arvore.direito, hashtag);
				pesquisaArvore(arvore.esquerdo, hashtag);
			}
		}
	}
	
	public void pesquisa(String hashtag)
	{
		String hashtag_convertida;
		if(raiz == null)
		{
			System.out.println("Sem valores na árvore"); 
		}
		else
		{
			hashtag_convertida = raiz.hashtag.trim();
			if(hashtag_convertida.equals(hashtag))
			{
				System.out.println(raiz.indice); 
			}
			else
			{
				pesquisaArvore(raiz.direito, hashtag);
				pesquisaArvore(raiz.esquerdo, hashtag);
				
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		
		ArvoreBinaria arvore = new ArvoreBinaria();
		String hashtag;
		String indices;
		
		try {
			InputStream file = new FileInputStream("arquivo_hashtags.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);

			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {				
					hashtag = line.substring(0,14);
					indices = line.substring(14, line.length());
					//System.out.println(indices);
					arvore.inserir(hashtag, indices);
				}
			}
			buffer.close();
			file_reader.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		arvore.pesquisa("Skol");
		
		
	}

}
