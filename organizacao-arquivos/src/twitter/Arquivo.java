package twitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import twitter4j.HashtagEntity;
import twitter4j.Status;

public class Arquivo {
	
	private long idTwitter;
	private String mensagem;
	private Date data;
	private String hashtags;
	
	
	public Arquivo(Status tweet) {
		this.setIdtwitter(tweet.getId());
		this.setMensagem(tweet.getText());
		this.setData(tweet.getCreatedAt());
		this.setHashtags(tweet.getHashtagEntities());
		System.out.println("id: " + this.getIdTwitter());
		System.out.println("mensagem: " + this.getMensagem());
		System.out.println("data: " + this.getDataFormatada());
		System.out.println("Hashtags: " + this.getHashtags());
	}
	
	
	public long getIdTwitter() {
		return idTwitter;
	}
	
	public void setIdtwitter(long idTwitter) {
		this.idTwitter = idTwitter;
	}
	
	public String getMensagem() {
		return String.format("%-280.280s",mensagem.replaceAll("\r", "").replaceAll("\n", ""));
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
	public void setHashtags(HashtagEntity[] hashtag) {
		ArrayList<String> lista = new ArrayList<String>();
		for(HashtagEntity hash : hashtag) {
			lista.add(hash.getText());
		}
		
		this.hashtags =String.format("%-280.280s",String.join(",", lista));
	}
}
