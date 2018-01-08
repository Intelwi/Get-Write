import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/** 
* Klasa odpowedzialna za interfejs graficzny do logowania

* @version 1.0

* @author Michal Stolarz

*/

public class LoginPanel extends JPanel implements ActionListener, KeyListener
{

	/** Wyskosc okna do obslugi komunikatora */
	public static final int HEIGHT = 550;
	
	/** Szerokosc okna do obslugi komunikatora */
	public static final int WIDTH = 450;

	/** Referencja do okna glownego JFrame */
	private ActionFrame frame;
	
	/** Przycisk do logowania */
	private JButton logButton;
	
	/** Pole tekstowe do podania loginu */
	private JTextField logField;
	
	/** Etykieta informacyjna (żeby podac login) */
	private JLabel login;
	
	/** Pole tekstowe do podania nazwy/IP serwera */
	private JTextField serwerName;
	
	/** Etykieta informacyjna (zeby podac nazwe/IP serwera) */
	private JLabel serwerLabel;
	
	/** Tworzy panel interfejsu graficznego do logowania 
	 * @param	frame	referencja na okno interfejsu graficznego
	 */
	public LoginPanel(ActionFrame frame)
	{
		super();
		
		/** Inicjalizacja własciwosci obiektu */
		this.frame = frame;
		logField = new JTextField();
		serwerName = new JTextField();
		logButton = new JButton("Log in");
		
		/** Ustawienie odbiorcy akcji przycisku logowania */
		logButton.addActionListener(this);
		
		/** Ustawienie odbiorcy akcji (JTextField) przycisku ENTER (logowanie) */
		logField.addKeyListener(this);
		
		/** Ustawienie odbiorcy akcji (JTextField) przycisku ENTER (logowanie) */
		serwerName.addKeyListener(this);
		
		/** Ustawienie zawartosci etykiety informacyjnej (wpisanie login'u) */
		login = new JLabel("Please enter your login: ");
		
		/** Ustawienie zawartosci etykiety informacyjnej (wpisanie nazwy serwera) */
		serwerLabel = new JLabel("Enter serwer name: ");
		
		/** Ustawienie stylu okna glownego */
		setLayout(new FlowLayout());
		
		/** Ustawienie rozmiarow okna glownego */
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		/** Pomocniczy panel do wprowadzania danych */
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(4, 1,5,5));//styl i rozmieszczenie
		inputPanel.add(login);//dodanie	
	    inputPanel.add(logField);//dodanie
	    inputPanel.add(serwerLabel);//dodanie
	    inputPanel.add(serwerName);//dodanie
		
		
		/** Pomocniczy panel do wysrodkowania elementow */
	    JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout());//styl
		parentPanel.add(inputPanel, BorderLayout.CENTER);//dodanie i umieszczenie
		parentPanel.add(logButton, BorderLayout.SOUTH);//dodanie i umieszczenie
		
		add(parentPanel);
	}
	
	/** Funkcja otwierajaca nowe okno do pisania i odbierania wiadomosci */
	public void openMainFrame()
	{
			/** Pobranie loginu od uzytkownika */
			String login2 = logField.getText();
			
			/** Sprawdzenie czy nazwa uzytkownika to nazwa zabroniona ("noTarget") */
			if(login2.equals(new MessPanel().getDefaultStr1()))
			{
				JOptionPane.showMessageDialog(frame, "Reserved name. Please choose another login.");
		    	return;
			}
			
			/** Iterowanie w poszukiwaniu spacji */
			for (int i = 0; i < login2.length(); i++){
			    char c = login2.charAt(i);
			    
			    /** Znalezienie spacji */
			    if (c == ' ') 
			    {
			    	JOptionPane.showMessageDialog(frame, "Login must consist only from digits, letters and punctuation.");
			    	return;
			    }
			}
			
			/** Pobranie nazwy serwera od uzytkownika */
			String serwer2 = serwerName.getText();
			
			/** Zmiana okna */
			SwingUtilities.invokeLater(new Runnable() {
			    @Override
			   public void run() 
			   {	
			    	try 
			    	{
			    		
			    		/** Usuwanie panelu logowania */
				    	frame.getContentPane().removeAll();
				    	
				    	/** Utworzenie widoku */
				    	View view = new View(frame);
				    	
			    		/** Utworzenie klienta */
			    		Klient klient = new Klient(serwer2, 5555, login2, view);
			    		
			    		/** Dodanie referencji na Klienta (kontroler) */
			    		view.setKlient(klient);
				    	
				    	/** Stworzenie zakladki z panelem do wysylania i odbierania wiadomosci */
				    	view.addPanelNoExit();
				    	
				    	/** Odswiezanie widoku */
				    	frame.validate();
				    	
				    	/** Uruchomienie watku Klienta odbierajacego informacje */
						klient.start();
					} 
					
			    	catch (IOException e) 
			    	{
						e.printStackTrace();
						
			    		/** Wyswietlenie okna dialogowego gdy nie mozna polaczyc z serwerem */
						JOptionPane.showMessageDialog(frame, "No serwer detected.");
						
						System.exit(0);
					}
			    }
			});
	}
	
	/** Obsluga zdarzenia przycisku logowania 
	 * @param	e	zdarzenie wywolane wcisnieciem przycisku do logowania
	 */
	public void actionPerformed(ActionEvent e)
	{
		/** Wykrycie zdarzenia */
		Object source = e.getSource();
		if(source == logButton)
		{
			/** Otworzenie okna do pisania i odbierania wiadomosci */
			openMainFrame();
		}
    }

	/** Obsluga zdarzenia wywolanego puszczeniem klawisza ENTER (logowanie)
	 * @param	e	zdarzenie klawisza ENTER
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			/** Otworzenie okna do pisania i odbierania wiadomosci */
			openMainFrame();
		}
		
	}
	
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
}
