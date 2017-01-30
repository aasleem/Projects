

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

//MainMenuFrame is the window containing the functions of the program after the user has logged in successfully
//the user can use the buttons to:
	//deposit funds into their account
	//withdraw funds from their account
	//transfer funds to another account
	//check the balance and account information
	//change the password of their account
	//log out and go back to the Log In Screen

public class MainMenuFrame extends JFrame implements ActionListener
{
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	private bankAccount correct;
	private int amount;
	private String numStr;
	private ArrayList<bankAccount> accList = new ArrayList<bankAccount>();

	private JPanel menuPanel;
	private JPanel topMenuPanel;
	private JPanel bottomMenuPanel;
	
	private JButton deposit;
	private JButton withdraw;
	private JButton transfer;
	private JButton checkBal;
	private JButton logOut;
	private JButton changePass;
	
	public MainMenuFrame(int posOfCorrect, ArrayList<bankAccount> sentList) throws IOException
	{
		super("Main Menu");
		
		setSize(WIDTH, HEIGHT);
		
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		accList = sentList;
		correct = sentList.get(posOfCorrect);

		menuPanel = new JPanel();
		topMenuPanel = new JPanel();
		bottomMenuPanel = new JPanel();
		
		//Deposit button
		deposit = new JButton("Deposit Funds");
		deposit.addActionListener(this);
		
		//Withdraw button
		withdraw = new JButton("Withdraw Funds");
		withdraw.addActionListener(this);
		
		//Transfer button
		transfer = new JButton("Make a Transfer");
		transfer.addActionListener(this);
		
		//Check Balance button
		checkBal = new JButton("Check Balance");
		checkBal.addActionListener(this);
		
		//Log Out button
		logOut = new JButton("Log Out");
		logOut.addActionListener(this);
		
		//Change Password button
		changePass = new JButton("Change PIN");
		changePass.addActionListener(this);
		
		//add buttons to menuPwhat is a recovering baptistanel
		menuPanel.setLayout(new GridLayout(15,3));		
		//menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS));

		menuPanel.add(new JLabel(" "));	menuPanel.add(new JLabel("       Bank of Houston"));	menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));	menuPanel.add(new JLabel("           Main Menu"));	menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));	menuPanel.add(new JLabel(" "));	menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));	addAButton(deposit, menuPanel);	menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));	menuPanel.add(new JLabel(" "));	menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));addAButton(withdraw, menuPanel);	menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" ")); addAButton(transfer, menuPanel);menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));addAButton(checkBal, menuPanel);menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));addAButton(changePass, menuPanel);menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));addAButton(logOut, menuPanel);menuPanel.add(new JLabel(" "));
		menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));menuPanel.add(new JLabel(" "));
		
		//add menuPanel to center of frame
		add(menuPanel);
		setVisible(true);
	}
	
    public void addAButton(JButton jb, JPanel container)
    { 
       // jb.setAlignmentX(container.CENTER_ALIGNMENT);
        container.add(jb);
    }
        public void actionPerformed(ActionEvent ae)
	{
		//Deposit button function
		if(ae.getActionCommand() == "Deposit Funds")
		{
			numStr = JOptionPane.showInputDialog("How much would you like to deposit? ", amount);
			int temp = Integer.parseInt(numStr);
	
			correct.deposit(temp);
		}
		
		//Withdraw button function
		if(ae.getActionCommand() == "Withdraw Funds")
		{
			numStr = JOptionPane.showInputDialog("How much would you like to withdraw? ", amount);
			int temp = Integer.parseInt(numStr);
		
			correct.withdraw(temp);
		}
		
		//Transfer button function
		if(ae.getActionCommand() == "Make a Transfer")
		{
			int amtToTransfer = Integer.parseInt(JOptionPane.showInputDialog("Amount to transfer: ", amount));
			int accTo = Integer.parseInt(JOptionPane.showInputDialog("Account to transfer to: ", amount));
			transferFunds(amtToTransfer, accTo);
		}
		
		//Check Balance button function
		if(ae.getActionCommand() == "Check Balance")
		{
			JOptionPane.showMessageDialog(getComponent(0), "Your current balance: $" + correct.getBalance());
		}
		
		//Change Password button function
		if(ae.getActionCommand() == "Change PIN")
		{
			numStr = JOptionPane.showInputDialog("Enter your new PIN: ", amount);
			int temp = Integer.parseInt(numStr);
			correct.changePassword(temp);
		}
		
		//Log Out Button function
		if(ae.getActionCommand() == "Log Out")
		{
			try
			{
				FileUpdater.updateFiles(accList);
				new LogInScreenFrame(this);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

		}
		
	}
        
    public void transferFunds(int amtToTransfer, int accTo)
    {
    	//subtract funds from current/correct
    	correct.withdraw(amtToTransfer);
    	
    	//get accTo from the accList
    	int indexOfTo = 0;
    	for(bankAccount b : accList)
    	{
    		if(b.getAccountNum() == accTo)
    		{
    			break;
    		}
    		indexOfTo++;
    	}
    	
    	//add funds to accTo
    	accList.get(indexOfTo).deposit(amtToTransfer);
    }
}
