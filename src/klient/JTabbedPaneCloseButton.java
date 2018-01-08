package klient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** 
* Klasa tworzaca panel z zakladkami mozliwymi do zamkniecia 

*/
public class JTabbedPaneCloseButton extends JTabbedPane {

	/** Referencja klasy na sama siebie dla klasy anonimowej (implementacja interfejsu ActionListener dla przycisku) */
	private JTabbedPaneCloseButton closePane;
	
	/** Tworzy panel z zakladkami mozliwymi do zamkniecia */
    public JTabbedPaneCloseButton() {
        super();
        this.closePane = this;
    }
    
    /** 
     * Metoda dodajaca karte z przyciskiem wylaczenia
     * @param	title	tytul zakladki karty
     * @param	icon	ikona zakladki karty
     * @param	component	komponent bedacy zawartoscia karty
     * @param	tip	 tekst dymku wyswietlanego po najechaniu na zakladke
     */
    @Override
    public void addTab(String title, Icon icon, Component component, String tip) {
        super.addTab(title, icon, component, tip);
        int count = this.getTabCount() - 1;
        setTabComponentAt(count, new CloseButtonTab(component, title, icon));
    }
    
    /** Metoda dodajaca karte z przyciskiem wylaczenia 
     * @param	title	tytul zakladki karty
     * @param	icon	ikona zakladki karty
     * @param	component	komponent bedacy zawartoscia karty
     */
    @Override
    public void addTab(String title, Icon icon, Component component) {
        addTab(title, icon, component, null);
    }

    /** Metoda dodajaca karte z przyciskiem wylaczenia 
     * @param	title	tytul zakladki karty
     * @param	component	komponent bedacy zawartoscia karty
     */
    @Override
    public void addTab(String title, Component component) {
        addTab(title, null, component);
    }

    /** Metoda dodajaca karte bez przycisku wylaczenia
     * @param	title	tytul zakladki karty
     * @param	icon	ikona zakladki karty
     * @param	component	komponent bedacy zawartoscia karty
     * @param	tip	 tekst dymku wyswietlanego po najechaniu na zakladke
     */
    public void addTabNoExit(String title, Icon icon, Component component, String tip) {
        super.addTab(title, icon, component, tip);
    }

    /** Metoda dodajaca karte bez przycisku wylaczenia 
     * @param	title	tytul zakladki karty
     * @param	icon	ikona zakladki karty
     * @param	component	komponent bedacy zawartoscia karty
     */
    public void addTabNoExit(String title, Icon icon, Component component) {
        addTabNoExit(title, icon, component, null);
    }

    /** Metoda dodajaca karte bez przycisku wylaczenia  
     * @param	title	tytul zakladki karty
     * @param	component	komponent bedacy zawartoscia karty
     */
    public void addTabNoExit(String title, Component component) {
        addTabNoExit(title, null, component);
    }

    /** Klasa panelu z przyciskiem i label'em do zamieszczenia w tytule zakladki */
    public class CloseButtonTab extends JPanel {
    	
    	/** Panel z zawartosca interfejsu komunikatora */
        private Component tab;

        /** Tworzy panel z zakladka z przyciskiem i label'em
         * @param	tab	komponent bedacy zawartoscia karty
         * @param	title	tytul zakladki karty
         * @param	icon	ikona zakladki karty
         */
        public CloseButtonTab(final Component tab, String title, Icon icon) {
            this.tab = tab;
            setOpaque(false);
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 3, 3);
            setLayout(flowLayout);
            JLabel jLabel = new JLabel(title);
            jLabel.setIcon(icon);
            add(jLabel);
            
            /** Zapisanie referencji do label'a z nazwa karty */
            ((MessPanel)tab).setName(jLabel);
            
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