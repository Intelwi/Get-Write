

import javax.swing.*;

import java.awt.EventQueue;

/** Klasa uruchamiająca interfejs graficzny do logowania */
public class ActionFrame extends JFrame 
{
	public ActionFrame() {
		super("Get&Write");
		
		JPanel loginPanel = new LoginPanel(this);
		add(loginPanel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
	}
	
	public static void main(String[] args)
	{
		/** Uruchomienie interfejsu graficznego */
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ActionFrame();
			}
		});
	}
}
