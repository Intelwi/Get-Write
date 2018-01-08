package serwer;

import message.*;

import java.io.*; 
import java.net.*; 
import java.util.ArrayList;
import java.util.Scanner;

/** 
* Klasa odpowiedzialna za dzialane strony serwera 

*/
public class Serwer extends Thread 
{		
		/** Maksymalna dopuszczalna liczba watkow (liczba obslugiwanych klientow) */
		public static final int MAXSIZE = 15;
	
		/** Lista klientow serwera */
		static ArrayList<Serwer> clients; 
		
		/** Blok statycznej inicjalizacji */
		static
		{
			try 
			{
				clients = new ArrayList<Serwer>();
			}
			catch(Exception e) 
			{
				System.out.println(e); 
			}
		}
		
		/** Wtyczka do klienta z ktorym sie polaczono */
		private Socket socket1; 
		
		/** Strumien do odbioru (deserializacji) */
		private ObjectInputStream ois;
		
		/** Strumien do wysylania (serializacji) */
		private ObjectOutputStream oos;
		
		/** Login klienta obslugiwanego przez dany watek */
		private String login;
		
		/** Flaga oznaczajaca czy obiekt nadaje sie do uzycia (nie nadaje sie gdy 
		 * klient loguje sie pod loginem ktory juz istnieje na liscie) */
		boolean isUsable;
		
		/** Tworzy obiekt z watkiem obslugujacym jednego klienta
		 * @param s	wtyczka do klienta z ktorym sie polaczono
		 * @throws IOException	jesli strumieni nie uda sie zainicjalizowac/wtyczka nieaktywna
		 * @throws ClassNotFoundException	jesli nie odnaleziono odpowiedniego typu klasy
		 */
		public Serwer (Socket s) throws IOException, ClassNotFoundException 
		{
			/** Domyslnie obiekt jest zdatny do uzycia */
			isUsable = true;
			
			socket1 = s;
			
			/** Utworzenie strumienia wejscia do kontaktu z klientem */
			ois = new ObjectInputStream(socket1.getInputStream());
			
			/** Utworzenie strumienia wyjscia do kontaktu z klientem */
			oos = new ObjectOutputStream(socket1.getOutputStream());
			
			/** Odebranie loginu Klienta */
			login = (String)ois.readObject();
			
			/** Petla do wyszukiwania czy login sie nie powtarza */
			for(Serwer klient: clients)
			{
				/** Wykrycie klienta o tym samym loginie */
				if((klient.getLogin()).equals(login))
				{
					/** Jesli login sie powtorzyl obiekt nie moze byc uzyty */
					isUsable = false;
					
					socket1.close();
					ois.close();
					oos.close();
				}
			}
		}
		
		/** Metoda sprawdzajaca czy Serwer przyjal maksymalna liczbe klientow 
		 * @return	true jesli serwer przyjal maksymalna liczbe klientow, false w przeciwnym razie 
		 */
		static public boolean isFull()
		{
			/** Sprawdzenie czy lista osiagnela teoretyczny maksymalny rozmiar */
			if(clients.size()>=MAXSIZE)
			{
				return true;
			}
			else return false;
		}
		
		/** Metoda wysylajaca wiadomosc do klienta 
		 * @param	message	wiadomosc ktora ma zostac wyslana
		 * @throws Exception	jesli nie uda sie wyslac wiadomosci przez strumien
		 */
		public void sendMsg(Message message) throws Exception
		{
			oos.writeObject(message);
			oos.flush();
		}
		
		/** Metoda wysylajaca liste Klientow do Klienta 
		 * @throws Exception	jesli nie uda sie wyslac wiadomosci przez strumien
		 */
		public void sendList() throws Exception
		{
			ArrayList<Serwer> clients3 = clients;
			String clients1 = new String();
			
			clients1 += "c ";
			
			for(int i = 0; i<clients3.size(); i++)
			{
				clients1 += ((clients3.get(i)).login + " ") ;
			}
			/** Zamkniecie listy w Message */
			Message msg = new Message(clients1);
					
			oos.writeObject(msg);
			oos.flush();
		}
		
		/** Metoda odbierajaca wiadomosc od Klienta 
		 * @return	odebrana wiadomosc
		 * @throws	ClassNotFoundException	jesli nie odnaleziono odpowiedniego typu klasy
		 * @throws	IOException	jesli nie uda sie odebrac wiadomosci (strumien nieczynny)
		 * */
		public Message getMsg() throws ClassNotFoundException, IOException
		{
			return (Message)ois.readObject();
		}
		
		/** Metoda podajaca login Klienta 
		 * @return	login klienta obslugiwanego przez dany watek
		 */
		public String getLogin()
		{
			return login;
		}
		
		/** Metoda znajdujaca login odlaczonego klienta na liscie i usuwajaca go
		 * @throws	Exception	gdy strumienie sa nieaktywne
		 */
		public void findAndDelete() throws Exception
		{
				/** Zamkniecie strumienia wejscia */
				ois.close();
				
				/** Zamkniecie strumienia wyjscia */
				oos.close();
				
				/** Zamkniecie wtyczki do klenta */
				socket1.close();
				
				for(Serwer klient: clients)
				{
					/** Szukanie Klienta */
					if((this.login).equals(klient.getLogin()))
					{
						/** Usuwanie Klienta z listy */
						clients.remove(klient);
						break;
					}
					else continue;
				}
				/** Petla do wysylania listy Klientow */
				for(Serwer klient: clients)
				{
						/** Wysylanie listy */
						klient.sendList();
				}
		}
		
		/** Metoda (watek) serwera obslugujaca danego klienta (przesylanie informacji) */
		public void run() 
		{
			try
			{
				
				while(true)
				{
					/** Odebrana wiadomosc od Klienta */
					Message msg = getMsg();
					
					/** Wyslanie wiadomosci */
					for(int i=0; i<clients.size(); i++)
					{
						/** Szukanie Klienta */
						if((msg.getTarget()).equals(((Serwer)clients.get(i)).getLogin()))
						{
							/** Wysylanie wiadomosci */
							((Serwer)clients.get(i)).sendMsg(msg);
							
							break;
						}
						
						/** Jeśli nie znaleziono Klienta */
						else if(i == clients.size()-1)
						{
							/** Wiadomosc dla nadawcy ze podany odbiorca wiadomosci nie istnieje */
							Message msg1 = new Message("No such a client available");
							
							/** Ustanowienie dowolnego Adresat by odroznic ta wiadomosc od listy klientow wysylanej Klientowi i od normalnej widomosci od innego klienta */
							msg1.setAdresat("noSuchKlient");
							
							/** Zaadresowanie wiadomosci zwrotnej do nadawcy (powiadomienie ze nie ma potencjalnego odbiorcy) */
							msg1.setTarget(msg.getTarget());
							
							/** Wyslanie widomosci */
							sendMsg(msg1);
						}
					}
				}
			}
			
			/** Wyjatek jesli nie mozna odczytac ze strumienia bo nie ma Klienta */
			catch(IOException  e)
			{
				try 
				{
					/** Wykasowanie klienta gdy strumień nieczynny */
					findAndDelete();
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
		
		
		
		public static void main(String args[]) throws Exception 
		{
			
			/** Deklaracja wtyczki serwera */
			ServerSocket ss;
			
			/** Stworzenie wtyczki serwera o numerze gniazda 5555 */
			ss = new ServerSocket(5555);
			
			/** Stworzenie klasy obslugujacej uzytkownika */
			SerwerMenu serwerMenu = new SerwerMenu();
			
			/** Uruchomienie watku obslugujacego uzytkownika */
			serwerMenu.start();
			
			try {
				
				while(true) 
				{	
					/** Nasluchiwanie Kienta */
					Socket s =ss.accept();
					
					/** Sprawdzenie czy maksymalna pula obslugiwanych watkow jest wyczerpana */
					if(isFull())
					{
						s.close();
						continue;
					}
					
					/** Inicjalizacja watku serwera do obslugi klienta */
					Serwer f1 = new Serwer(s);
					
					/** Sprawdzenie czy obiekt mozna utworzyc watek */
					if(f1.isUsable)
					{
						synchronized (clients)
						{
							/** Dodanie wątku do listy */
							clients.add(f1);
						
							/** Rozpoczecie działania watku */
							f1.start();
						
							/** Wyslanie listy klientow do kazdego klienta */
							for(Serwer klient: clients)
							{
								/** Wysylanie listy */
								klient.sendList();
							}
						}
					}
				}
		    }
			
			catch (IOException e) 
			{ 
				System.out.println(e); 
			}
			
			finally
			{
				/** Zamkniecie wtyczki */
				ss.close();
			}
		}
}

/** 
* Klasa odpowiedzialna za obsluge serwera przez uzytkownika
 
* @version 1.0

* @author Michal Stolarz

*/

class SerwerMenu extends Thread 
{
	/** Metoda (watek) do obslugi serwera z konsoli */
	public void run()
	{ 
		/** Scanner do odbierania komend */
		Scanner reader = new Scanner(System.in);
		
		while(true)
		{
			System.out.println("Enter 'exit' to turn off serwer:");
			
			/** Sprawdzenie czy wpisano odpowiednia komende */
			if((reader.next()).equals("exit"))
			{
				/** zamkniecie scanner */
				reader.close();
				
				/** Wylaczenie JVM */
				System.exit(0);
			}
			else
			{
				System.out.println("Error: wrong command typed");
			}
		}
	}
}
