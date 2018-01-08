import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** 
* Klasa odpowedzialna za interfejs graficzny do wysylania i odbierania wiadomosci

* @version 1.0

* @author Michal Stolarz

*/

public class MessPanel extends JPanel
{
	/** Wysokosc okna do obslugi komunikatora */
	public static final int HEIGHT = 550;
	
	/** Szerokosc okna do obslugi komunikatora */
	public static final int WIDTH = 450;
	
	/** Nazwa pierwszej karty */
	private static final String firstName = new String("Start");//moze lagowac

	/** Referencja do obiektu klasy Klient */
	private Klient klient;
	
	/** Referencja do glownego okna JFrame */
	private ActionFrame frame;
	
	/** Referencja na obiekt zarzadzajacy cala grafika */
	private View view;
	
	/** Przycisk do wysylania wiadomosci */
	private JButton inButton;
	
	/** Przycisk do wylogowania */
	private JButton logoutButton;
	
	/** Aktualnie wybrany RadioButton */
	private JRadioButton selectedButton;
	
	/** Pole tekstowe do pisania wiadomosci */
	private JTextArea inText;
	
	/** Pole tekstowe do wyswietlania wiadomosci od drugiego klienta */
	private JTextArea outText;
	
	/** Sowak dla pola tekstowego do pisania */
	private JScrollPane scrollPaneIn;
	
	/** Sowak dla pola tekstowego do wyswietlania */
	private JScrollPane scrollPaneOut;
	
	/** Etykieta z nazwa uzytkownika */
	private JLabel login;
	
	/** Etykieta z nazwa docelowego odbiorcy wiadomosci */
	private JLabel targetClientL;
	
	/** Nazwa docelowego odbiorcy wiadomosci */
	private String targetClient;
	
	/** Etykieta informacyjna (nad polem z otrzymanymi wiadomosciami) */
	private JLabel infoLabel1;
	
	/** Etykieta informacyjna (nad polem do wpisywania wiadomosci) */
	private JLabel infoLabel2;
	
	/** Etykieta informacyjna (nad lista klientow) */
	private JLabel infoLabel3;
	
	/** Panel do wyswietlania listy dostepnych klientow */
	 private JPanel listPanel;
	 
	 /** Zawartosc targetClient na poczatku dzialania aplikacji */
	 private final String defaultStr = new String("Target not defined"); 
	 
	 /** Zawartosc targetClient gdy nie ma wybranego, istniejacego na liscie klienta z ktorym sie komunikujemy */
	 private final String defaultStr1 = new String("noTarget"); 
	 
	 /** Etykieta karty w ktorej bedzie znajdowac sie panel */
	 private JLabel name;
	 
	 /** Tworzy obiekt tylko do pobrania defaultStr */
	 public MessPanel()
		{
			super();
		}
	 
	/** Tworzy obiekt z panelem obslugi wysylania i odbierania wiadomosci 
	 * @param	klient	referencja na obiekt typu Klient 
	 * @param	frame	referencja na obiekt ramki interfejsu graficznego
	 * @param	view	referencja na obiekt zarzadzajacy interfejsem graficznym
	 */
	public MessPanel(Klient klient, ActionFrame frame, View view)
	{
		super();
		
		/** Utworzenie obiektow interfejsu użytkownika */
		this.view = view;
		this.klient = klient;
		this.frame = frame;
		name = new JLabel();
		inButton = new JButton("Send");
		logoutButton = new JButton("Log out");
		selectedButton = new JRadioButton();
		inText = new JTextArea(5, 20);
		outText = new JTextArea(20, 20);
		targetClientL = new JLabel(defaultStr);
		login = new JLabel("User: " + klient.getLogin());
		infoLabel1 = new JLabel("Income:");
		infoLabel2 = new JLabel("Me:");
		infoLabel3 = new JLabel("Available users:");
		targetClient = new String(defaultStr1);
		
		/** Ustawienie sowakow dla pol tekstowych */
		scrollPaneIn = new JScrollPane(inText);
		scrollPaneOut = new JScrollPane(outText);
		
		/** Ustawienie możliwosci edytowania pol tekstowych */
		inText.setEditable(true);
		outText.setEditable(false);
		
		/** Ustawienie odbiorcy akcji wywolywanej przez przycisk wysylania wiadomosci */
		inButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					/** Wyslanie wiadomosci jesli nie jest pusta */
					if(inText.getText().isEmpty() == false)
					{
						/** Klient wysyla wiadomosc jesli wybrano odbiorce wiadomosci */
						if(!(getTargetClientL().equals(getDefaultStr())))
						{
							/** Stworzenie i wyslanie wiadomosci */
							klient.createSendMessage(targetClient, inText.getText());
					
							/** Wstawienie do pola z przychodzacymi wiadomosciami wiadomosci napisanej przez uzytkownika */ 
							addText(klient.getLogin() + ": " + inText.getText());
					
							/** Wyczyszczenie pola tekstowego do wpisywania wiadomosci */
							inText.setText("");
						}
						
						/** Jesli nie wybrano odbiorcy wiadomosci wyswietlenie komunikatu */
						else 
						{
							/** Wyswietlenie komunikatu */
							view.infoNoTarget();
							
							/** Wyczyszczenie pola tekstowego do wpisywania wiadomosci */
							inText.setText("");
						}
					}
				}
				
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		
		/** Ustawienie odbiorcy akcji (JTextArea do wpisywania wiadomosci) wywolywanej przez ENTER (wysylanie wiadomosci) */
		inText.addKeyListener(new KeyListener() 
        {
			/** Obsluga zdarzenia wywolanego wcisnieciem klawisza
			 * @param	e	zdarzenie klawisza
			 */
			@Override
			public void keyTyped(KeyEvent e) {}

			/** Obsluga zdarzenia wywolanego wcisnieciem klawisza
			 * @param	e	zdarzenie klawisza
			 */
			@Override
			public void keyPressed(KeyEvent e) {}

			/** Ustawienie ENTER do wysylania wiadomosci (dopiero po puszczeniu ENTER) */
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					try 
					{
						/** Pomocniczy string z wycietym znakiem nadmiarowego znaku nowej linii (powstałego przez wcisniecie klawisza ENTER) */
						String str = cutNewLine(inText.getText());
						
						/** Klient wysyla wiadomosc jesli wybrano odbiorce wiadomosci */
						if(!(getTargetClientL().equals(getDefaultStr())))
						{
							/** Stworzenie i wyslanie wiadomosci */
							klient.createSendMessage(targetClient, str);
					
							/** Wstawienie do pola z przychodzacymi wiadomosciami wiadomosci napisanej przez uzytkownika */ 
							addText(klient.getLogin() + ": " + str);
					
							/** Wyczyszczenie pola tekstowego do wpisywania wiadomosci */
							inText.setText("");
						}
						
						/** Jesli nie wybrano odbiorcy wiadomosci wyswietlenie komunikatu */
						else 
						{
							/** Wyswietlenie komunikatu */
							view.infoNoTarget();
							
							/** Wyczyszczenie pola tekstowego do wpisywania wiadomosci */
							inText.setText("");
						}
					}
				
					catch (Exception e1) 
					{
						e1.printStackTrace();
					}
				}
			}
        });
		
		/** Ustawienie odbiorcy akcji wywolywanej przez przycisk wylogowania */
		logoutButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					/** Znikniecie okna */
					frame.setVisible(false);
				 
					/** Skasowanie okna */
					frame.dispose();
					
					/** Wylaczenie klienta */
					klient.closeKlient();
					
					System.exit(0);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
         
		/** Ustawienie rozmiarow okna glownego */
		setLayout(new FlowLayout(FlowLayout.CENTER,35,0));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		/** Pomocniczy panel do naglowkow informacyjnych (login uzytkownika i login klienta do ktorego bedzie wysylana wiadomosc) */
		JPanel headPanel = new JPanel();
		headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.PAGE_AXIS));//styl i rozmieszczenie
		headPanel.add(login);//dodanie
		headPanel.add(targetClientL);//dodanie
		
		/** Pomocniczy panel do umiejscowienia label'a informacyjnego i textbox'a wyswietlajacego wiadomosci */
	    JPanel incomePanel = new JPanel();
		incomePanel.setLayout(new BoxLayout(incomePanel, BoxLayout.PAGE_AXIS));//styl i rozmieszczenie
		incomePanel.add(infoLabel1);//dodanie
		incomePanel.add(scrollPaneOut);//dodanie
		
		/** Pomocniczy panel do umiejscowienia label'a informacyjnego i textbox'a do wyswietlania wiadomosci */
	    JPanel outcomePanel = new JPanel();
		outcomePanel.setLayout(new BoxLayout(outcomePanel, BoxLayout.PAGE_AXIS));//styl i rozmieszczenie
		outcomePanel.add(infoLabel2);//dodanie
		outcomePanel.add(scrollPaneIn);//dodanie
		
		/** Pomocniczy panel do pol tekstowych */
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());//styl i rozmieszczenie
		textPanel.add(incomePanel,BorderLayout.CENTER);//dodanie
		textPanel.add(outcomePanel, BorderLayout.PAGE_END);//dodanie
		
	    /** Pomocniczy panel do przyciskow */
	    JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));//styl i rozmieszczenie
		buttonPanel.add(inButton);//dodanie	
	    buttonPanel.add(logoutButton);//dodanie

		/** Utworzenie panelu z lista Klientow */
	    listPanel = new JPanel();
	    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
	    
	    /** Pomocniczy panel do wyswietlania panelu z lista klientow i label'a informacyjnego */
	    JPanel listPanelSurround = new JPanel();
		listPanelSurround.setLayout(new BoxLayout(listPanelSurround, BoxLayout.PAGE_AXIS));//styl i rozmieszczenie
		listPanelSurround.add(infoLabel3);//dodanie
		listPanelSurround.add(listPanel);//dodanie
		
		/** Pomocniczy panel do wysrodkowania elementow */
	    JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout());//styl
		parentPanel.add(headPanel, BorderLayout.PAGE_START);
		parentPanel.add(textPanel, BorderLayout.LINE_START);//dodanie i umieszczenie
		parentPanel.add(buttonPanel, BorderLayout.PAGE_END);//dodanie i umieszczenie
		
		
		add(parentPanel);
		add(listPanelSurround);
	}
	
	/** Pobranie nazwy pierwszej karty 
	 * @return	tytul pierwszej, startowej zakladki karty 
	 */
	public static String getNameFirst()
	{
		return MessPanel.firstName;
	}
	
	/** Pobranie informacji z etykiety informacyjnej o targetClient
	 * @return	login klienta z ktorym jest prowadzona konwersacja 
	 */
	public String getTargetClientL()
	{
		return targetClientL.getText();
	}
	
	/** Zainicjowanie referencji do etykiety informacyjnej z nazwa karty w ktorej znajduje sie panel
	 * @param	name	etykieta informacyjna z nazwa klienta z ktorym prowadzona jest rozmowa
	 */
	public void setName(JLabel name)
	{
		this.name = name;
	}
	
	/** Pobranie domyslnej zawartosci pola targetClient(defaultStr) 
	 * @return zawartosc pola defaultStr
	 */
	public String getDefaultStr()
	{
		return defaultStr;
	}
	
	/** Pobranie domyslnej zawartosci pola targetClient(defaultStr1) 
	 * @return	zawartosc pola defaultStr1 
	 */
	public String getDefaultStr1()
	{
		return defaultStr1;
	}
	
	/** Pobranie nazwy docelowego odbiorcy wiadomosci 
	 * @return	login klienta z ktorym prowadzona jest rozmowa 
	 */
	public String getTargetClient()
	{
		return targetClient;
	}
	
	/** Ustawienie docelowego odbiorcy wiadomosci 
	 * @param	targetClient	login klienta z ktorym prowadzona jest rozmowa
	 */
	public void setTargetClient(String targetClient)
	{
		this.targetClient = targetClient;
		
		/** Nadpisanie etykiety informujacej o targetClient */
		(this.targetClientL).setText("To: " + targetClient);
	}
	
	/** Wyswietlenie powiadomienia dla klienta od serwera 
	 * @param	msg	odebrana wiadomosc
	 */
	public void setInfo(Message msg)
	{
		this.targetClient = defaultStr1;
		
		/** Wyczyszczenie pola tekstowego do wpisywania wiadomosci */
		setText("");
		
		/** Nadpisanie etykiety informujacej o targetClient */
		(this.targetClientL).setText("To:");
	}
	
	/** Tworzenie listy przyciskow z klientami i wyswietlenie 
	 * @param	msg	odebrana wiadomosc
	 * @throws	NullPointerException	gdy obiekt wiadomosci tekstowej obiektu msg nie jest zainicjalizowany 
	 */
	public void createButtontList(Message msg) throws NullPointerException
	{
		/** Wyczyszczenie panelu */
		listPanel.removeAll();
		
		/** Pobranie wiadomosci tekstowej */
		String list = msg.getMessagetxt(); 
		
		/** Indeks */
		int i = 1;
		
		/** Stworzenie grupy przyciskow */
		ButtonGroup group = new ButtonGroup();
		
		/** Wyczytawanie nazw Klientow ze String */
		while(++i<list.length())
		{
			/** Pomocniczy String */
			String helpStr = new String();
			
			/** Zapisanie nazwy Klienta do String */
			while(list.charAt(i) != ' ')
			{
				helpStr += list.charAt(i);
				i++;
			}
	
			/** Sprawdzenie czy helpStr to login Klienta, jesli tak to pominiecie reszty petli */
			if(helpStr.equals(klient.getLogin())) continue;
			
			/** Stworzenie przycisku */
			JRadioButton button = new JRadioButton(helpStr);
			
			/** Warunek ze jesli nazwa przycisku taka jak targetClient to ma zostac wcisniety */
			if(helpStr.equals(targetClient))
			{
				button.setSelected(true);
				selectedButton = button;
			}
			
			/** Dodanie przycisku do grupy */
			group.add(button);
			
			/** Dodanie słuchacza nowo powstałego przycisku */
			button.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) 
				{
					Object o = e.getSource();
					JRadioButton b = null;
					String buttonText = "Error: getting targetClient name from JButton";
					
					if(o instanceof JRadioButton) b = (JRadioButton)o;
					
					/** Wyszukanie czy karta z konwersacja do tego klienta (b.getText()) juz powstala - jesli tak wyswietlenie komunikatu*/
					if(view.isOnTheList(b.getText()))
					{
						b.setSelected(false);
						selectedButton.setSelected(true);
						JOptionPane.showMessageDialog(frame, "Such a client already selected.");
						
						/** Jesli nie jest zaznaczony zaden RadioButton i powinno tak zostac to odswiezenie listy (jesli tego nie ma to b i tak zostaje wcisniety) */
						if(b.isSelected()) view.createButtontList(klient.getListMsg());
					}
					
					/** Jesli karta z konwersacja do tego klienta (b.getText()) nie powstala to tworzona jest nowa karta lub zmieniany targetClient w karcie */
					else
					{	
						/** Ustawienie docelowego odbiorcy wiadomosci */
						if(b != null) 
						{
							selectedButton = b;
							setTargetClient(b.getText());
						}
					
						/** Gdy ustawienie docelowego odbiorcy wiadomosci sie nie powiodlo */
						if(b == null) setTargetClient(buttonText);
					
						/** Wykasowanie wszystkich dotychczasowych wiadomosci z interfejsu */
						setText("");
					
						try
						{
							/** Jesli to nie jest pierwsza karta */
							if(!((name.getText()).equals(MessPanel.getNameFirst())))
							{
								/** Zmiana tytulu zakladki */
								name.setText(b.getText());
							}
						}
						catch(Exception e1)
						{
							System.out.println(e1);
						}
					}
				}
			});
			
			/** Dodanie przycisku z nazwa Klienta do listy */
			listPanel.add(button);
			
		}			
		
		/** Odswiezenie widoku */
		frame.validate();
		
	}
	
	/** Dodanie komunikatu w ramce z tekstem 
	 * @param	message	wiadomosc ktorej tekst (messagetxt) ma byc dodany w polu tekstowym
	 * */
	public void addText(Message message)
	{
		outText.append(message.getAdresat() + ": " + message.getMessagetxt() + "\n");
		
	}
	
	/** Dodanie komunikatu w ramce z tekstem 
	 * @param	text	tekst ktory ma byc dodany w polu tekstowym 
	 */
	public void addText(String text)
	{
		outText.append(text + "\n");
		
	}
	
	/** Wstawienie nowego komunikatu w ramce z tekstem (usuwa poprzednia zawartosc) 
	 * @param	message	wiadomosc ktorej tekst (messagetxt) ma byc zamieszczony w polu tekstowym
	 */
	public void setText(Message message)
	{
		outText.setText(message.getAdresat()+": "+message.getMessagetxt() + "\n");//juz obsluzone jesli message.getMessagetxt() jest puste
	}
	
	/** Wstawienie nowego komunikatu w ramce z tekstem (usuwa poprzednia zawartosc)
	 * @param	text	 tekst ktory ma byc zamieszczony w polu tekstowym
	 */
	public void setText(String text)
	{
		/** Jesli text nie jest pusty nalezy dodac znak nowej linii */
		if(text.isEmpty() == false)
		{
			outText.setText(text + "\n");
		}
		
		/** Jesli text jest pusty nie nalezy dodawac znaku nowej linii */
		else
		{
			outText.setText(text);
		}
	}
	
	/** Metoda do wycinania nadmiarowego znaku nowej linii powstalego przy zatwierdzaniu zmian przez klawisz ENTER 
	 * @param	str	tekst w ktorym ma zostac obciety nadmiarowy znak nowej linii
	 * @return	tekst powstaly po obcieciu nadmiarowego znaku nowej linii
	 */
	public String cutNewLine(String str) 
	{
	    if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '\n') {
	        str = str.substring(0, str.length() - 1);
	    }
	    return str;
	}
}