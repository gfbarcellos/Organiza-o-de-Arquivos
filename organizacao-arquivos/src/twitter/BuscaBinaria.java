package twitter;

import java.io.IOException;
import java.io.RandomAccessFile;

public class BuscaBinaria {
	private RandomAccessFile arquivo;
	private static String fatia;

	public void Arquivo_Java() {
		try {
			arquivo = new RandomAccessFile("indicerandom.txt", "rw");
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

	private long buscaRegistro(long me) {
		try {
			fatia = null;
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
		long pIni = 0, pFim = filesize(), pMeio = pFim / 2;
		System.out.println("ini =" + pIni + " pFim =" + pFim + " pMeio =" + pMeio+"\n");
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
			System.out.println("novo inicio = "+ pIni + " novo meio = " + pMeio + " novo fim = " + pFim );
		}
		Long indice = pMeio / 591;
		return Integer.valueOf(indice.toString())+1;
	}

	public static void main(String[] args) {
		BuscaBinaria b = new BuscaBinaria();
		b.Arquivo_Java();
//		long vet[] = {1185750537460842496L,1185750563318689792L,1185750566603055105L,1185750583833174022L,1185750587851210752L,1185750599234654209L,1185750617806925824L,1185750625063260160L,1185750637767778305L,1185750684358057985L,1185750692130099200L,1185750692583002112L,1185750693455380481L,1185750694709645312L,1185750697406541825L,1185750743753592832L,1185750744143560705L,1185750758622453762L,1185750760409186304L,1185750764767076353L,1185750766272823296L,1185750776494190592L,1185750788070694912L,1185750799164403713L,1185750816034045952L,1185750822652645378L,1185750824653348864L,1185750824980377600L,1185750830693138432L,1185750830873362432L,1185750849290735616L,1185750857318567936L,1185750901908221952L,1185750903711621120L,1185750910133325825L,1185750922204348416L,1185750923659939840L,1185750982078148608L,1185750997370466305L,1185751009202790400L,1185751011471822849L,1185751031038316544L,1185751037845606400L,1185751042777968642L,1185751068019347456L,1185751068170489857L};
//		for(int i = 0; i < vet.length ; i++) {
//			System.out.println(b.buscaBinaria(vet[i]));
//		}
		System.out.println(b.buscaBinaria(1188583535000424450L));

	}

}
