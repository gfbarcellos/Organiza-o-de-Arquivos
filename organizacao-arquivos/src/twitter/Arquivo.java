package twitter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Arquivo {
	
	private long idTwitter;
	private String mensagem;
	private Date data;
	private String hashtags;
	
	public long getIdTwitter() {
		return idTwitter;
	}
	
	public void setIdtwitter(long idTwitter) {
		this.idTwitter = idTwitter;
	}
	
	public String getMensagem() {
		return mensagem.replaceAll("\r", "").replaceAll("\n", "");
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getDataFormatada() {
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		return fmt.format(data);
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getHashtags() {
		return hashtags;
	}
	public void setHashtags(String hashtags) {
		this.hashtags = hashtags;
	}
	

}