import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** 
* Klasa tworzaca panel z zkladkami mozliwymi do zamkniecia 

* @version 1.0

* @author Michal Stolarz

*/
public class JTabbedPaneCloseButton extends JTabbedPane {

	/** Referencja klasy na sama siebie dla klasy anonimowej (implementacja interfejsu ActionListener dla przycisku) */
	private JTabbedPaneCloseButton closePane;
	
    public JTabbedPaneCloseButton() {
        super();
        this.closePane = this;
    }
    
    /** Metoda dodajaca karte z przyciskiem wylaczenia (component - zawartosc interfejsu komunikatora) */
    @Override
    public void addTab(String title, Icon icon, Component component, String tip) {
        super.addTab(title, icon, component, tip);
        int count = this.getTabCount() - 1;
        setTabComponentAt(count, new CloseButtonTab(component, title, icon));
    }
    
    /** Metoda dodajaca karte z przyciskiem wylaczenia */
    @Override
    public void addTab(String title, Icon icon, Component component) {
        addTab(title, icon, component, null);
    }

    /** Metoda dodajaca karte z przyciskiem wylaczenia */
    @Override
    public void addTab(String title, Component component) {
        addTab(title, null, component);
    }

    /** Metoda dodajaca karte bez przycisku wylaczenia (component - zawartosc interfejsu komunikatora) */
    public void addTabNoExit(String title, Icon icon, Component component, String tip) {
        super.addTab(title, icon, component, tip);
    }

    /** Metoda dodajaca karte bez przycisku wylaczenia */
    public void addTabNoExit(String title, Icon icon, Component component) {
        addTabNoExit(title, icon, component, null);
    }

    /** Metoda dodajaca karte bez przycisku wylaczenia */
    public void addTabNoExit(String title, Component component) {
        addTabNoExit(title, null, component);
    }

    /** Panel z przyciskiem i label'em z napisem */
    public class CloseButtonTab extends JPanel {
    	
    	/** Panel z zawartosca interfejsu komunikatora */
        private Component tab;

        public CloseButtonTab(final Component tab, String title, Icon icon) {
            this.tab = tab;
            setOpaque(false);
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 3, 3);
            setLayout(flowLayout);
            JLabel jLabel = new JLabel(title);
            jLabel.setIcon(icon);
            add(jLabel);
            
            /** Zapisanie referencji do label'a z nazwa karty */
            ((MessPanel)tab).setName(jLabel);//moze lagowac
            
            JButton button = new JButton("x");
            button.setBackground(new Color(245, 116, 97));
            button.setMargin(new Insets(0, 0, 0, 0));
            add(button);
            
            /** Dodanie sluchacza przycisku zamykania karty */
            button.addActionListener(new ActionListener() { 
    			public void actionPerformed(ActionEvent e) 
    			{
    				try 
    				{
    					
    					/** Zamkniecie karty */
    					closePane.remove(tab);
    				}
    				
    				catch(Exception e1)
    				{
    						System.out.println(e1);
    				}
    			}
    		});
        }
    }
}