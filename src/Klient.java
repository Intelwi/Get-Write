


import java.io.*;
import java.net.*;

/** Klasa odpowiedzialna za strone kliencka */
public class Klient extends Thread 
{

		/** Wtyczka Klienta */
		private Socket socket;
		
		/** Strumien do odbierania danych */
		private ObjectOutputStream oos;
		
		/** Strumien do wysyłania danych */
		private ObjectInputStream ois;
		
		/** Login Klienta */
		private String login;
		
		/** Referencja do interfejsu graficznego */
		private View view;
		
		/** Aktualna Wiadomosc od serwera z lista dostepnych klientow */
		private Message listMsg;
		
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
		
		/** Pobranie wiadomosci z lista Klientow */
		public Message getListMsg()
		{
			return listMsg;
		}
		
		/** Wyslanie wiadomosci */
		private void send(Message msg) throws Exception
		{
			oos.writeObject(msg);
			oos.flush();
		}
		
		/** Wylaczenie klienta */
		public void closeKlient() throws Exception
		{
			/** Zamkniecie strumienia wejscia */
			ois.close();
			
			/** Zamkniecie strumienia wyjscia */
			oos.close();
			
			/** Zamkniecie wtyczki do serwera */
			socket.close();
		}
		
		
		/** Odebranie wiadomosci */
		public Message getMsg() throws ClassNotFoundException, IOException
		{
			return (Message)ois.readObject();
		}
					
		/** Utworzenie i wyslanie wiadomosci */			
		public void createSendMessage(String targetClient, String text) throws Exception
		{
			/** Utworzenie wiadomosci */
			Message message = new Message(targetClient, text, this.login);
				
			/** Wyslanie wiadomosci */
			send(message);			
		}
		
		/** Pobieranie loginu od Klienta */
		public String getLogin()
		{
			return login;
		}
		
		/** Metoda - watek */
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