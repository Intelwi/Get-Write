import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** 
* Klasa odpowiedzialna za zarzadzanie interfejsem graficznym po stronie klienta (widok)

* @version 1.0

* @author Michal Stolarz

*/

public class View 
{
	/** Referencja do obiektu klasy Klient */
	private Klient klient;
	
	/** Referencja do glownego okna ActionFrame */
	private ActionFrame frame;
	
	/** Referencja do panelu zakladek */
	private JTabbedPaneCloseButton tabbedPane;
	
	/** Tworzy obiekt zarzadzajacy interfejsem graficznym
	 * @param	frame	referencja na ramke okna interfejsu graficznego  
	 */
	View(ActionFrame frame)
	{
		this.frame = frame;
		this.tabbedPane = new JTabbedPaneCloseButton();
		
		/** Dodanie panelu z zakladkami do frame */
		(this.frame).getContentPane().add(tabbedPane);
	}
	
	/** Dodanie referencji na obiekt typu Klient (kotroler aplikacji obslugujacej klienta)
	 * @param	klient	referencja na obiekt typu Klient 
	 */
	public void setKlient(Klient klient)
	{
		this.klient = klient;
	}
	
	/** Dodanie nowej karty z nowym panelem z zakladka bez przycisku zamkniecia karty */
	public void addPanelNoExit()
	{
		/** Stworzenie zakladki z panelem do wysylania i odbierania wiadomosci */
		tabbedPane.addTabNoExit(MessPanel.getNameFirst(), new MessPanel(klient, frame, this));
	}
	
	/** Dodanie nowej karty z nowym panelem z zakladka z przyciskiem zamkniecia karty 
	 * @param	targetClient	login klienta z ktorym prowadzona jest konwersacja
	 */
	public void addPanel(String targetClient)
	{
		MessPanel messPanel = new MessPanel(klient, frame, this);
		messPanel.setTargetClient(targetClient);
		tabbedPane.add(targetClient, messPanel);
	}
	
	/** Sprawdza czy karta (typu MessPanel) z podanym targetClient juz istnieje 
	 * @param	targetClient	login klienta z ktorym prowadzona jest konwersacja
	 * @return	jesli karta z podanym targetClient juz istnieje to zwraca true, jesli nie to false	
	 */
	public boolean isOnTheList(String targetClient)
	{
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			if(targetClient.equals(((MessPanel)tabbedPane.getComponentAt(i)).getTargetClient())) return true;
		}
		return false;
	}
	
	/** Tworzenie listy przyciskow z klientami i wyswietlenie 
	 * @param	msg	odebrana wiadomosc zawierajaca liste klientow
	 * @throws	NullPointerException	gdy obiekt msg, albo jego pole (messagetxt) nie jest zainicjalizowany/e
	 */
	public void createButtontList(Message msg) throws NullPointerException
	{
		/** Iterowanie po kolejnych interfejsach graficznych */
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			/** Stworzenie listy przyciskow z nazwami Klientow */
			((MessPanel)tabbedPane.getComponentAt(i)).createButtontList(msg);
		}
	}
	
	/** Powiadomienie uzytkownika interfejsu o braku Klienta do ktorego wysylana jest wiadomosc przez 
	 * nadpisanie etykiety informacyjnej
	 * @param	msg	odebrana wiadomosc od serwera z powiadomieniem o braku danego klienta	
	 */
	public void informUser(Message msg) 
	{
		/** Iterowanie po kolejnych interfejsach graficznych */
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			/** Iterowanie do poki nie znajdzie nadawcy tej wiadomosci na liscie interfejsow graficznych */
			if(msg.getTarget().equals(((MessPanel)tabbedPane.getComponentAt(i)).getTargetClient()))
			{
				/** Jesli pierwsza karta (o nazwie "Start") */
				if(i==0)
				{
					/** Powiadomienie uzytkownika interfejsu przez nadpisanie label'a */
					((MessPanel)tabbedPane.getComponentAt(i)).setInfo(msg);
				
					/** Wyswietlenie informacji w oknie dialogowym*/
					JOptionPane.showMessageDialog(frame, msg.getMessagetxt());
				}
				
				/** Jesli kazda kolejna karta */
				else
				{
					/** Wyswietlenie informacji w oknie dialogowym*/
					JOptionPane.showMessageDialog(frame, msg.getMessagetxt());
					
					/** Wykasowanie karty */
					tabbedPane.removeTabAt(i);
				}
			}
		}
	}
	
	/** Sprawdzenie czy istnieje interfejs graficzny do klienta od ktorego przyszla wiadomosc 
	 * @param	msg	odebrana wiadomosc (od innego klienta)
	 * @return	jesli na liscie jest juz panel do danego klienta (ktory wyslal msg) zwraca true, jesli nie to false
	 */
	public boolean isPanel(Message msg)
	{ 
		/** Iterowanie po kolejnych interfejsach graficznych */
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			/** Sprawdzenie czy istnieje interfejs graficzny do klienta od ktorego przyszla wiadomosc () */
			if(msg.getAdresat().equals(((MessPanel)tabbedPane.getComponentAt(i)).getTargetClient()))
			{
				/** Wyswietlenie wiadomosci w ramce z tekstem */
				((MessPanel)tabbedPane.getComponentAt(i)).addText(msg);

				/** Ustawienie nowego docelowego odbiorcy wiadomosci (targetClient) */
				((MessPanel)tabbedPane.getComponentAt(i)).setTargetClient(msg.getAdresat());
				
				return true;
			}
		}	
		
		return false;
	}
	
	/** Ustawienie targetClient w karcie Start (gdy nie jest on tam ustawiony) lub utworzenie nowej karty (jesli targetClient 
	 * jest juz ustawiony w karcie Start) 
	 * @param	msg	odebrana wiadomosc (od innego klienta)
	 * @param	listMsg	ostatnia wiadomosc od serwera zawierajaca liste z klientami
	 */
	public void updateCreateNewTab(Message msg, Message listMsg) 
	{
		/** Gdy otwarta jedna karta, ale nie ma zdefiniowanego targetClient (targetClient jest wypelnione przez defaultStr) */
		if(tabbedPane.getTabCount()==1 && (((MessPanel)tabbedPane.getComponentAt(0)).getTargetClient()).equals(((MessPanel)tabbedPane.getComponentAt(0)).getDefaultStr1()))
		{
			/** Wyswietlenie wiadomosci w ramce z tekstem */
			((MessPanel)tabbedPane.getComponentAt(0)).setText(msg);

			/** Ustawienie nowego docelowego odbiorcy wiadomosci (targetClient) */
			((MessPanel)tabbedPane.getComponentAt(0)).setTargetClient(msg.getAdresat());
			
			/** Odswiezenie listy klientow (wlacznie z zaznaczeniem aktualnego targetClient) */
			((MessPanel)tabbedPane.getComponentAt(0)).createButtontList(listMsg);
		}
		
		/** Gdy otwarta jedna karta i ma zdefiniowanego targetClient */
		else
		{
			/** Nowa zawartośsc okna */
			JPanel messPanel1 = new MessPanel(klient, frame, this);
			
			/** Wyswietlenie wiadomosci w ramce z tekstem i wykasowanie wszystkich poprzednich wiadomosci */
			((MessPanel)messPanel1).setText(msg);

			/** Ustawienie nowego docelowego odbiorcy wiadomosci (targetClient) */
			((MessPanel)messPanel1).setTargetClient(msg.getAdresat());
    	
			/** Aktualizacja listy klientow w nowej zakladce */
			((MessPanel)messPanel1).createButtontList(listMsg);

			/** Stworzenie zakladki z panelem do wysylania i odbierania wiadomosci */
			tabbedPane.addTab(msg.getAdresat(), messPanel1);
    	
			/** Odswiezanie widoku */
			frame.validate(); 
		}
	}
	
	/** Pokazanie powiadomienia ze podany login juz zostal wybrany */
	public void infoDoubleLogin()
	{
		/** Wyswietlenie okna dialogowego gdy wpisano juz istniejacy login */
		JOptionPane.showMessageDialog(frame, "Login already exists. Please type another login.");
	}
	
	/** Pokazanie powiadomienia ze nie ma polaczenia z serwerem */
	public void infoNoSerwer()
	{
		/** Wyswietlenie okna dialogowego gdy strumień nieczynny */
		JOptionPane.showMessageDialog(frame, "No serwer detected.");
	}
	
	/** Pokazanie powiadomienia ze nie wybrano odbiorcy wiadomosci */
	public void infoNoTarget()
	{
		/** Wyswietlenie okna dialogowego gdy nie wybrano klienta */
		JOptionPane.showMessageDialog(frame, "No client chosen.");
	}
}
