package twitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HashTab {
	
	public String data;
	public String Indice;
	
	public HashTab(){
		this.data = "0";
		Indice = "0";
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getIndice() {
		return Indice;
	}
	public void setIndice(String indice) {
		Indice = indice;
	}	
	public static void TabelaHash(  )
	{
		
		int max = 61;
		
		HashTab[] tabela = new HashTab[max];
		for(int i=0;i<max;i++)
		{
			tabela[i] = new HashTab();
		}
		
		int indice = 0;
		String data,dataConvertida;
		try {
			InputStream file = new FileInputStream("hash.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);

			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					indice++;
					data = line.substring(299,309);
					if(data.substring(2,3).contentEquals("/") && data.substring(5,6).contentEquals("/"))
					{
						dataConvertida = data.substring(6,10) + data.substring(3,5) + data.substring(0,2);
						
						if(tabela[indiceHash(Integer.parseInt(dataConvertida), max)].getIndice().equals("0"))//Ainda não tem valor
						{
							tabela[indiceHash(Integer.parseInt(dataConvertida), max)].setIndice(Integer.toString(indice));
							tabela[indiceHash(Integer.parseInt(dataConvertida), max)].setData(data);
						}
						else
						{
							tabela[indiceHash(Integer.parseInt(dataConvertida), max)].setIndice(tabela[indiceHash(Integer.parseInt(dataConvertida), max)].getIndice() + "," + Integer.toString(indice));
						}
					}
					
				}
			}
			
			for(int i=0;i<max;i++)
			{	
				System.out.println(tabela[i].getData() + ";" +tabela[i].getIndice() );
			}
			
			buffer.close();
			file_reader.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static int indiceHash (int chave, int tamanho) {
		return chave % tamanho;
	}
	public static void main(String[] args) {
		HashTab b = new HashTab();
		b.TabelaHash();
	}
	
}
