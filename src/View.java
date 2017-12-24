import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** Klasa odpowiedzialna za zarzadzanie interfejsem graficznym po stronie klienta */
public class View 
{
	/** Referencja do obiektu klasy Klient */
	private Klient klient;
	
	/** Referencja do głownego okna JFrame */
	private ActionFrame frame;
	
	/** Referencja do panelu zakladek */
	private JTabbedPaneCloseButton tabbedPane;
	
	View(ActionFrame frame)
	{
		this.frame = frame;
		this.tabbedPane = new JTabbedPaneCloseButton();
		
		/** Dodanie panelu z zakladkami do frame */
		(this.frame).getContentPane().add(tabbedPane);
	}
	
	/** Dodanie referencji na obiekt typu Klient (kotroler aplikacji obslugujacej klienta) */
	public void setKlient(Klient klient)
	{
		this.klient = klient;
	}
	
	/** Dodanie nowej zakladki z nowym panelem bez przycisku zamkniecia karty */
	public void addPanelNoExit()
	{
		/** Stworzenie zakladki z panelem do wysylania i odbierania wiadomosci */
		tabbedPane.addTabNoExit(MessPanel.getNameFirst(), new MessPanel(klient, frame, this));
	}
	
	/** Dodanie nowej zakladki z nowym panelem z przyciskiem zamkniecia karty */
	public void addPanel(String targetClient)
	{
		MessPanel messPanel = new MessPanel(klient, frame, this);
		messPanel.setTargetClient(targetClient);
		tabbedPane.add(targetClient, messPanel);
	}
	
	/** Sprawdza czy karta (typu MessPanel) z podanym targetClient (argument) - klientem docelowym juz istnieje */
	public boolean isOnTheList(String targetClient)
	{
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			if(targetClient.equals(((MessPanel)tabbedPane.getComponentAt(i)).getTargetClient())) return true;
		}
		return false;
	}
	
	/** Tworzenie listy przyciskow z klientami i wyswietlenie */
	public void createButtontList(Message msg) throws IndexOutOfBoundsException, NullPointerException
	{
		/** Iterowanie po kolejnych interfejsach graficznych */
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			/** Stworzenie listy przyciskow z nazwami Klientow */
			((MessPanel)tabbedPane.getComponentAt(i)).createButtontList(msg);
		}
	}
	
	/** Powiadomienie uzytkownika interfejsu o braku Klienta do ktorego wysylana jest wiadomosc przez nadpisanie label'a */
	public void informUser(Message msg) 
	{
		/** Iterowanie po kolejnych interfejsach graficznych */
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			/** Iterowanie do poki nie znajdzie nadawcy tej wiadomosci na liscie interfejsow graficznych */
			if(msg.getTarget().equals(((MessPanel)tabbedPane.getComponentAt(i)).getTargetClient()))
			{
				/** Powiadomienie uzytkownika interfejsu przez nadpisanie label'a */
				((MessPanel)tabbedPane.getComponentAt(i)).setInfo(msg);
			}
		}
	}
	
	/** Sprawdzenie czy istnieje interfejs graficzny do klienta od ktorego przyszla wiadomosc (jesli tak to zaladowanie nowego targetClient)*/
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
	
	/** Ustawienie targetClient w karcie Start (gdy nie jest on tam ustawiony) lub utworzenie nowej karty (jesli targetClient jest juz ustawiony w karcie Start) */
	public void updateCreateNewTab(Message msg, Message listMsg) 
	{
		/** Gdy otwarta jedna karta, ale nie ma zdefiniowanego targetClient (targetClient jest wypelnione przez defaultStr) lub klient pisze sam ze soba */
		if(tabbedPane.getTabCount()==1 && ((((MessPanel)tabbedPane.getComponentAt(0)).getTargetClient()).equals(((MessPanel)tabbedPane.getComponentAt(0)).getDefaultStr()) || (klient.getLogin()).equals(((MessPanel)tabbedPane.getComponentAt(0)).getTargetClient())))
		{
			/** Wyswietlenie wiadomosci w ramce z tekstem */
			((MessPanel)tabbedPane.getComponentAt(0)).addText(msg);

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
		/** Wyswietlenie okna dialogowego gdy strumień nieczynny */
		JOptionPane.showMessageDialog(frame, "Login already exists. Please type another login.");
	}
	
	/** Pokazanie powiadomienia ze nie ma polaczenia z serwerem */
	public void infoNoSerwer()
	{
		/** Wyswietlenie okna dialogowego gdy strumień nieczynny */
		JOptionPane.showMessageDialog(frame, "Login already exists. Please type another login.");
	}
}
