package klient;

import message.*;

import java.io.*;
import java.net.*;

/** 
* Klasa odpowiedzialna za obsulge interfejsu graficznego, odbieranie i wysylanie wiadomosci (kontroler)

*/
public class Klient extends Thread 
{

		/** Wtyczka Klienta */
		private Socket socket;
		
		/** Strumien do odbierania danych */
		private ObjectOutputStream oos;
		
		/** Strumien do wysylania danych */
		private ObjectInputStream ois;
		
		/** Login Klienta */
		private String login;
		
		/** Referencja do interfejsu graficznego */
		private View view;
		
		/** Aktualna wiadomosc od serwera z lista dostepnych klientow */
		private Message listMsg;
		
		/** 
		* Tworzy obiekt obslugujacy interfejs graficzny, odbierajacy i wysylajacy wiadomosci
		* @param	host	nazwa/IP serwera
        * @param	port	numer portu polaczenia z serwerem
        * @param	login1	login klienta
        * @param	view	referencja na obiekt zarzadzajacy interfejsem graficznym
        * @throws	IOException	jesli strumieni nie uda sie zainicjalizowac/wtyczka nieaktywna	
        * */
		public Klient(String host, int port, String login1, View view) throws IOException
		{
			listMsg = new Message("");
			login = login1;
			socket = new Socket(host, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			this.view = view;
			
			/** Wyslanie loginu */
			oos.writeObject(login);
			
			oos.flush();
		}
		
		/** Pobranie ostatniej wiadomosci z lista klientow
		 * @return	ostatnia odebrana wiadomosc (od serwera) z lista klientow 
		 */
		public Message getListMsg()
		{
			return listMsg;
		}
		
		/** Wyslanie wiadomosci 
		 * @param	msg	wiadomosc do wyslania
		 * @throws	Exception	jesli nie uda sie wyslac wiadomosci przez strumien
		 */
		private void send(Message msg) throws Exception
		{
			oos.writeObject(msg);
			oos.flush();
		}
		
		/** Wylaczenie klienta 
		 * @throws	Exception	gdy strumienie sa nieaktywne 
		 */
		public void closeKlient() throws Exception
		{
			/** Zamkniecie strumienia wejscia */
			ois.close();
			
			/** Zamkniecie strumienia wyjscia */
			oos.close();
			
			/** Zamkniecie wtyczki do serwera */
			socket.close();
		}
		
		
		/** Odebranie wiadomosci 
		 * @return	wiadomosc odebrana od serwera
		 * @throws	ClassNotFoundException	jesli nie odnaleziono odpowiedniego typu klasy
		 * @throws	IOException	jesli nie uda sie odebrac wiadomosci (strumien nieczynny)
		 */
		public Message getMsg() throws ClassNotFoundException, IOException
		{
			return (Message)ois.readObject();
		}
					
		/** Utworzenie i wyslanie wiadomosci 
		 * @param	targetClient	logn klienta do ktorego wysylana jest wiadomosc
		 * @param	text	wiadomosc tekstowa ktora ma byc wyslana do drugiego klienta
		 * @throws	Exception	jesli nie uda sie wyslac wiadomosci przez strumien
		 */			
		public void createSendMessage(String targetClient, String text) throws Exception
		{
			/** Utworzenie wiadomosci */
			Message message = new Message(targetClient, text, this.login);
				
			/** Wyslanie wiadomosci */
			send(message);			
		}
		
		/** Pobieranie loginu od klienta 
		 * @return	login klienta
		 */
		public String getLogin()
		{
			return login;
		}
		
		/** Watek obslugujacy odbieranie wiadomosci od serwera */
		public void run() 
		{
			/** Flaga oznaczajaca czy wykonywany jest aktualnie pierwszy obieg petli */
			boolean isFirst = true;
			
			try 
			{
				while(true)
				{
					
					/** Odebrana wiadomosc od Serwera */
					Message msg = getMsg();
					
					/** Odznaczenie ze wykonywany jest pierwszy obieg petli */
					if(isFirst)
					{	
						isFirst = false;
					}
					
					/** Jeśli wiadomosc wyslana przez serwer czyli pole targetClient puste to otrzymana zostala lista z klientami */
					if(msg.noTarget() == true)//zrobione w View createButtonList()
					{
						/** Utworzenie listy przyciskow z klientami */
						view.createButtontList(msg);
						
						/** Zapisanie wiadomosci z lista klientow */
						listMsg = msg;
					}
					
					else
					{
						/** Rozpoznanie wiadomosci od serwera od zwyklej wiadomosci od innego klienta (pole Adresat = "noSuchKlient") */
						if(msg.getAdresat().equals("noSuchKlient"))
						{
							/** Powiadomienie uzytkownika interfejsu o braku Klienta do ktorego wysylana jest wiadomosc przez nadpisanie label'a */
							view.informUser(msg); 
						}
						
						else
						{
							/** Zmienna wskazujaca czy jest interfejs do klienta od ktorego przyszla wiadomosc */
							boolean was = view.isPanel(msg);
						
							/** Gdy nie ma jeszcze interfejsu graficznego do klienta od ktorego przyszla wiadomosc */
							if(!was)
							{	
								/** Ustawienie targetClient w karcie Start (gdy nie jest on tam ustawiony) lub utworzenie nowej karty (jesli targetClient jest juz ustawiony w karcie Start) */
								view.updateCreateNewTab(msg,listMsg); 
							}
						}
					}	
				}
			}
			
			/** Wyjatek jesli nie mozna odczytac wiadomosci ze strumienia bo nie ma Serwera */
			catch(IOException  e)
			{
				try 
				{
					closeKlient();
					
					/** Wpisanie zlego loginu (dlatego przerwanie polaczenia juz w pierwszym obiegu petli) */
					if(isFirst)
					{
						view.infoDoubleLogin();
						
						System.exit(0);
					}
					
					/** Zwykle przerwanie polaczenia */
					else
					{
						view.infoNoSerwer();
					}
				} 
				
				catch (Exception e1) 
				{
					System.out.println(e1); 
					e1.printStackTrace();
				}
			}
			catch(Exception e)
			{
				System.out.println(e); 
				e.printStackTrace();
			}
		}
		
}		