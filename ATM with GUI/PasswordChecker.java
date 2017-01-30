
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextArea;

//establishes input from a Password.txt to check the entered account number and password
//if the sent account and password are correct, then the log in is successful
//if the log in is successful, then the LogInFrame will be closed and the MainMenuFrame will be opened
//the user is limited to 4 tries to enter the correct information

//Format of AccountInformation.txt is:
	//account number
	//password
	//first name
	//last name
	//balance
	//status
		//repeat

//Format of Password.txt is: 
	//account number
	//password
		//repeat
public class PasswordChecker
{
	private static int numOfTries = 4;
	
	private static int correctAccNum;
	
	public static void checkPassword(String sentAcc, String sentPass, JFrame sentFrame, JTextArea tries) throws IOException
	{
		//if the entered information is correct
		if(checker(sentAcc, sentPass))
		{
			//close LoginFrame, open MainMenuFrame
			sentFrame.setVisible(false);
			sentFrame.dispose();
			MainMenuFrame m = new MainMenuFrame(getPosOfCorrect(), getAccList());
		}
		else
		{
			//handle tracking and display of tries remaining
			numOfTries--;
			if(numOfTries<=0)
			{
				tries.setText("You have run out of tries.");
				JOptionPane.showMessageDialog(null, "You have been locked out. Click OK to exit." , "Excessive Incorrect Attempts", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			else
			{
				tries.setText("You have " + numOfTries + " tries left. Please try again.");
			}	
		}
	}
	
	//returns false(default) if the sent information is not correct
	//returns true if the sent information is correct
	public static boolean checker(String sentAcc, String sentPass) throws IOException
	{
		Scanner passwordReader = null;
		boolean correctPass = false;

		//establish input from Password.txt
		try
		{
			passwordReader = new Scanner(new FileReader("Password.txt"));
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "An error occured while connecting to Password.txt. Click OK to exit.", "File IO Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		//check all contents of the Password.txt
			//this is according to the format of the example Password.txt
			//for any other format, the algorithm would have to be adapted
		while(passwordReader.hasNext())
		{
			String accountNum = passwordReader.nextLine();
			String passInfo = passwordReader.nextLine();
			
			if(accountNum.matches(sentAcc) && passInfo.matches(sentPass))
			{
				correctPass = true;
				correctAccNum = Integer.parseInt(accountNum);
				break;
			}
		}
		
		passwordReader.close();
		//sr.close();
		//bs.close();
		return correctPass;
	}
	
	public static ArrayList<bankAccount> getAccList()
	{
		ArrayList<bankAccount> listToSend = new ArrayList<bankAccount>();
		Scanner aiReader = null;
		
		//establish input from AccountInformation.txt
		try
		{
			aiReader = new Scanner(new FileReader("AccountInformation.txt"));
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "An error occured while connecting to AccountInformation.txt. Click OK to exit.", "File IO Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		//get all accounts in AccountInformation.txt and compile into the ArrayList
			//also get the position of the current user's account in the file
		while(aiReader.hasNext())
		{
			//get the information
			int accNum = Integer.parseInt(aiReader.nextLine());
			int passW = Integer.parseInt(aiReader.nextLine());
			String fN = aiReader.nextLine();
			String lN = aiReader.nextLine();
			int bal = Integer.parseInt(aiReader.nextLine());
			String stat = aiReader.nextLine();
			
			//add to the list
			listToSend.add(new bankAccount(accNum, passW, fN, lN, bal, stat));
		}
		
		aiReader.close();
		
		return listToSend;
	}
	
	public static int getPosOfCorrect()
	{
		int p = 0;
		
		try
		{
			Scanner aR = new Scanner(new FileReader("Password.txt"));
			
			while(aR.hasNext())
			{
				int aN = Integer.parseInt(aR.nextLine());
				aR.nextLine();
				
				if(aN == correctAccNum)
				{
					break;
				}
				
				p++;
			}
			
			aR.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error");
		}
		
		return p;
	}
}

