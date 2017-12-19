

import java.io.Serializable;

/** Klasa ktorej obiekty (wiadomosci) sa przesylane miedzy klientem a serwerem */
public class Message implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	/** Pole z nazwa adresata wiadomosci */
	private String adresat;
	
	/** Pole z nazwa docelowego odbiorcy wiadomosci */
	private String targetClient; // jesli nic tu nie ma to wiadomosc wysyla serwer
	
	/** Pole z trescia wiadomosci */
	private String messagetxt;
	
	public Message(String messagetxt)
	{
		this.messagetxt = messagetxt;
		this.adresat = new String();
		this.targetClient = new String();
	}
	
	public Message(String targetClient, String messagetxt, String adresat)
	{
		this.targetClient = targetClient;
		this.messagetxt = messagetxt;
		this.adresat = adresat;
		
	}
	
	public Message(Message msg)
	{
		targetClient = msg.targetClient;
		messagetxt = msg.messagetxt;
		adresat = msg.adresat;
	}
	
	/** Zaladowanie referencji na nazwe docelowego odbiorcy wiadomosci */
	public void setTarget(String targetClient)
	{
		this.targetClient = targetClient;
	}
	
	/** Pobranie referencji na docelowego odbiorce wiadomosci */
	public String getTarget()
	{
		return targetClient;
	}
	
	/** Zaladowanie referencji na tresc wiadomosci */
	public void setMessagetxt(String messagetxt)
	{
		this.messagetxt = messagetxt;
	}
	
	/** Pobranie referencji na tresc wiadomosci */
	public String getMessagetxt()
	{
		return messagetxt;
	}

	/** Zaladowanie referencji na nazwe adresata wiadomosci */
	public void setAdresat(String adresat)
	{
		this.adresat = adresat;
	}
	
	/** Pobranie referencji na nazwe adresata wiadomosci */
	public String getAdresat()
	{
		return adresat;
	}
	
	/** Metoda wskazujaca czy wiadomosc zawiera nazwe docelowego odbiorcy wiadomosci (true - nie zawiera, false - zawiera) */
	public boolean noTarget()//zwraca 1 gdy nie ma targetclient
	{
		return targetClient.isEmpty();
	}
}
