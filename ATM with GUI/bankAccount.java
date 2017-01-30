import javax.swing.JOptionPane;

public class bankAccount
{
	
	private int accNumber;
	private int password;
	private String firstName;
	private String lastName;
	private int balance;
	private String status;
	
	
	public bankAccount(int a, int p, String f, String l, int b, String s)
	{
		accNumber = a;
		password = p;
		firstName = f;
		lastName = l;
		balance = b;
		status = s;
	}
	
	public void deposit(int x)		
	{
		balance += x;
		JOptionPane.showMessageDialog(null, "Balance Updated");
	}
		
	public void withdraw(int x)
	{
		if(x > balance)
		{
			JOptionPane.showMessageDialog(null, "Insufficient funds");
		}
		else
		{
			balance -= x;
			JOptionPane.showMessageDialog(null, "Balance Updated");
		}
	}
		
	public void changePassword(int newPass)
	{
		password = newPass;
		JOptionPane.showMessageDialog(null, "PIN Updated");
	}
		
	public String getFirstName()
	{
		return firstName;
	}
		
	public String getLastName()
	{
		return lastName;
	}
		
	public String getStatus()
	{
		return status;
	}
		
	public int getAccountNum()
	{
		return accNumber;
	}
		
	public int getBalance()
	{
		return balance;
	}
		
	public int getPassword()
	{
		return password;
	}
		
	public String toString()
	{	
		return getAccountNum() + " " + getPassword() + " " + getFirstName() + " " + getLastName() + " " + getBalance() + " " + getStatus(); 
	}
}
