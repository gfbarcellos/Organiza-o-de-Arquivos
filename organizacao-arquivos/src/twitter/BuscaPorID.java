package twitter;

import java.io.IOException;
import java.io.RandomAccessFile;

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
	public static void main(String[] args) {
		BuscaPorID id = new BuscaPorID();
		id.buscaPorId(82128);

	}

}
