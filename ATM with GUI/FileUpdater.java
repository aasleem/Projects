

import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import java.io.File;

//FileUpdater takes the list of account information and writes it back to the files
public class FileUpdater
{
	public static void updateFiles(ArrayList<bankAccount> sentList)
	{
		BufferedWriter pW;
		BufferedWriter aW;
		
		try
		{
			//establish temp output files
			pW = new BufferedWriter(new FileWriter("tempP.txt"));
			aW = new BufferedWriter(new FileWriter("tempA.txt"));
			
			for(bankAccount b : sentList)
			{
				//write account numbers and passwords to temp to replace old Password.txt
				pW.write("" + b.getAccountNum());
				pW.newLine();
				pW.write("" + b.getPassword());
				pW.newLine();
				
				//write account information to temp to replace old AccountInformation.txt
				aW.write("" + b.getAccountNum());
				aW.newLine();
				aW.write("" + b.getPassword());
				aW.newLine();
				aW.write(b.getFirstName());
				aW.newLine();
				aW.write(b.getLastName());
				aW.newLine();
				aW.write("" + b.getBalance());
				aW.newLine();
				aW.write(b.getStatus());
				aW.newLine();
			}
			
			pW.close();
			aW.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error writing to temporary output files.");
		}
		
		//delete old files
		new File("Password.txt").delete();
		new File("AccountInformation.txt").delete();
		
		//rename temp files
		new File("tempP.txt").renameTo(new File("Password.txt"));
		new File("tempA.txt").renameTo(new File("AccountInformation.txt"));
	}
}
