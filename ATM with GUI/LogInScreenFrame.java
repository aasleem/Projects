

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//when instantiated, LonInScreenFrame opens the window of the log-in screen
//when input is entered, account number and password will be checked by PasswordChecker
//when the correct account number and password is entered: 
	//the MainMenuFrame will open
	//this LogInScreenFrame will close
public class LogInScreenFrame extends JFrame implements ActionListener
{
	public static final int WIDTH = 360;
	public static final int HEIGHT = 360;

	
	private JPanel northPanel;
	private JPanel centerPanel;
	private JPanel southPanel;
	
	private JPanel accInfoPanel;
	private JPanel passPanel;
	private JPanel buttonPanel;
	private JPanel displayPanel;
	
	private JLabel bankLabel;
	private JLabel accLabel;
	private JLabel passLabel;
	private JLabel triesLabel;
	
	private JTextField accField;
	
	private JPasswordField passField;
	
	private JButton logInButton;
	private JTextArea triesLeft;
	
	//creates the frame of the log-in screen
	public LogInScreenFrame() throws FileNotFoundException
	{
		super("Please Log In");
		
		setSize(WIDTH, HEIGHT);
		
		setLayout(new BorderLayout());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setResizable(false);
	
		//establish a panel for the north, center, and south of the frame
		northPanel = new JPanel();
		centerPanel = new JPanel();
		southPanel = new JPanel();
		
		//set layouts of each of the frame's panel
		northPanel.setLayout(new FlowLayout());
		centerPanel.setLayout(new BorderLayout());
		southPanel.setLayout(new BorderLayout());
		
		//display the name of the bank at the top center
		bankLabel = new JLabel("Bank of Houston");
		
		//labels the account information field
		accLabel = new JLabel("Account Number:");
		
		//labels the password field
		passLabel = new JLabel("Password:");
		
		//account field for user input
		accField = new JTextField(10);
		
		//password field for user input
		passField = new JPasswordField(10);
		
		//button that initiates the transition to the MainMenuFrame
		logInButton = new JButton("Log In");
		logInButton.addActionListener(this);
		
		//labels the tries-remaining display
		triesLabel = new JLabel("Tries Remaining:");
		
		//text area that displays the tries remaining
		triesLeft = new JTextArea();
		triesLeft.setEditable(false);
		
		//add title to northPanel
		northPanel.add(bankLabel);
		
		//add label and field for account information to its own panel
		accInfoPanel = new JPanel();
		accInfoPanel.add(accLabel);
		accInfoPanel.add(accField);
		
		//add label and field for password to its own panel
		passPanel = new JPanel();
		passPanel.add(passLabel, BorderLayout.SOUTH);
		passPanel.add(passField, BorderLayout.SOUTH);
		
		//add accInfoPanel to the NORTH of centerPanel
		centerPanel.add(accInfoPanel, BorderLayout.NORTH);
		
		//add passPanel to CENTER of centerPanel
		centerPanel.add(passPanel, BorderLayout.CENTER);
		
		//add login button to its own panel
		buttonPanel = new JPanel();
		buttonPanel.add(logInButton);
		
		//add tries-remaining label and display field to its own panel
		displayPanel = new JPanel();
		displayPanel.add(triesLabel);
		displayPanel.add(triesLeft);
		
		//add buttonPanel to SOUTH of centerPanel
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		//add displayPanel to CENTER of southPanel
		southPanel.add(displayPanel, BorderLayout.CENTER);
		
		//add panels to the frame
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		setVisible(true);
	}
	
	//runs when the user selects Log Out in the main menu
	//closes the MainMenuFrame
	//opens a new LogInScreenFrame
	public LogInScreenFrame(JFrame sentFrame) throws FileNotFoundException
	{
		sentFrame.setVisible(false);
		sentFrame.dispose();
		new LogInScreenFrame();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		//logInButton functions
		if(ae.getActionCommand() == "Log In")
		{
			//JPasswordField returns the text as a char array
			//must turn into String
			try
			{
				PasswordChecker.checkPassword(accField.getText(), String.valueOf(passField.getPassword()), this, triesLeft);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	

}
