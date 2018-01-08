
import java.io.Serializable;

/** 
* Klasa ktorej obiekty (wiadomosci) sa przesylane miedzy klientem a serwerem 

* @version 1.0

* @author Michal Stolarz

*/
public class Message implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	/** Pole z nazwa adresata wiadomosci (login klienta wysylajacego wiadomosc),
	 * jesli zawiera "noSuchKlient" to wiadomosc jest wyslana przez serwer i oznacza ze ostatnio wyslana wiadomosc 
	 * przez klienta nie zostala dostarczona (brak klienta do ktorego byla zaadresowana), jesli jest puste to wiadomosc jest
	 * wyslana przez serwer i zawiera liste dostepnych klientow */
	private String adresat;
	
	/** Pole z nazwa docelowego odbiorcy wiadomosci (login klienta do ktorego ma byc dostarczona wiadomosc), 
	 * jesli pole jest puste to wiadomosc zostala wyslana przez serwer i zawiera liste dostepnych klientow, 
	 * jesli wiadomosc jest zwrotna od serwera (adresat = "noSuchKlient") to pole zawiera login klienta 
	 * do ktorego nie dostarczono wiadomosci */
	private String targetClient;
	
	/** Pole z trescia wiadomosci, moze zawierac komunikat od innego klienta (gdy jest to wiadomosc od innego klienta),
	 * liste dostepnych klientow (gdy jest to wiadomosc od serwera z lista klientow), lub komunikat "No such a client available" 
	 * (wiadomosc zwrotna od serwera oznaczajaca, ze nie dostarczono ostatnio wyslanej wiadomosci przez klienta) */
	private String messagetxt;
	
	/** Tworzy wiadomosc w formie obiektu Message
	 * @param	messagetxt	tekstowa tresc wiadomosci
	 */
	public Message(String messagetxt)
	{
		this.messagetxt = messagetxt;
		this.adresat = new String();
		this.targetClient = new String();
	}
	
	/** Tworzy wiadomosc w formie obiektu Message
	 * @param	targetClient	login klienta do ktorego ma byc wyslana wiadomosc
	 * @param	messagetxt	tekstowa tresc wiadomosci
	 * @param	adresat	login klienta ktory wysyla wiadomosc
	 */
	public Message(String targetClient, String messagetxt, String adresat)
	{
		this.targetClient = targetClient;
		this.messagetxt = messagetxt;
		this.adresat = adresat;
		
	}
	
	/** Tworzy wiadomosc w formie obiektu Message z zawartosciami pol takimi jak obiekt,
	 *  do ktorego referencja zostala podana jako parametr
	 * @param	msg	referencja na obiekt typu Message (wiadomosc), ktory ma byc skopiowany
	 */
	public Message(Message msg)
	{
		targetClient = msg.targetClient;
		messagetxt = msg.messagetxt;
		adresat = msg.adresat;
	}
	
	/** Zaladowanie referencji na nazwe docelowego odbiorcy wiadomosci
	 * @param	targetClient	login klienta do ktorego ma byc wyslana wiadomosc
	 */
	public void setTarget(String targetClient)
	{
		this.targetClient = targetClient;
	}
	
	/** Pobranie referencji na docelowego odbiorce wiadomosci 
	 * @return	login klienta do ktorego ma byc wyslana wiadomosc
	 */
	public String getTarget()
	{
		return targetClient;
	}
	
	/** Zaladowanie referencji na tresc wiadomosci 
	 * @param	messagetxt	tekstowa tresc wiadomosci 
	 */
	public void setMessagetxt(String messagetxt)
	{
		this.messagetxt = messagetxt;
	}
	
	/** Pobranie referencji na tresc wiadomosci 
	 * @return	tekstowa tresc wiadomosci
	 */
	public String getMessagetxt()
	{
		return messagetxt;
	}

	/** Zaladowanie referencji na nazwe adresata wiadomosci 
	 * @param	adresat	login klienta ktory wysyla wiadomosc
	 */
	public void setAdresat(String adresat)
	{
		this.adresat = adresat;
	}
	
	/** Pobranie referencji na nazwe adresata wiadomosci 
	 * @return	login klienta ktory wysyla wiadomosc 
	 */
	public String getAdresat()
	{
		return adresat;
	}
	
	/** Metoda wskazujaca czy wiadomosc zawiera nazwe docelowego odbiorcy wiadomosci 
	 * @return	wskazanie czy wiadomosc zawiera nazwe docelowego odbiorcy wiadomosci (true - nie zawiera, false - zawiera) 
	 */
	public boolean noTarget()
	{
		return targetClient.isEmpty();
	}
}
