package klient;

import javax.swing.*;

import java.awt.EventQueue;

/** 
* Klasa uruchamiajaca interfejs graficzny do logowania

*/
public class ActionFrame extends JFrame 
{
	/** Tworzy glowna ramke okna intefrejsu graficznego */
	public ActionFrame() {
		super("Get&Write");
		
		JPanel loginPanel = new LoginPanel(this);
		add(loginPanel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setResizable(false);
		
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
