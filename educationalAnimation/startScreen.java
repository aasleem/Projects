import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class startScreen extends JFrame implements ActionListener
{
	private JPanel northPanel;
	private JPanel centerPanel;
	
	private JLabel welcomeLabel;
	
	private JButton startScreenButton;
	
	//creates the frame of the log-in screen
	public startScreen()
	{
		super("Educational Shapes and Colors");
		setSize(550, 450);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	
		//establish a panel for the north, center, and south of the frame
		northPanel = new JPanel();
		centerPanel = new JPanel();
		
		//set layouts of each of the frame's panel
		northPanel.setLayout(new FlowLayout());
		centerPanel.setLayout(new BorderLayout());
	
		//displays a welcome message
		welcomeLabel = new JLabel("Learn your shapes and colors!");
		welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(23f));
			//add title to northPanel
			northPanel.add(welcomeLabel);
		
		//button that initiates the transition to the MainMenuFrame
		startScreenButton = new JButton("Start");
		startScreenButton.addActionListener(this);		
			//add buttonPanel to SOUTH of centerPanel
			centerPanel.add(startScreenButton, BorderLayout.CENTER);

		//add panels to the frame
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		setVisible(true);
	}
	

	public void actionPerformed(ActionEvent ae)
	{
		//startScreenButton functions
		if(ae.getActionCommand() == "Start")
		{
			try
			{
				this.setVisible(false);
				this.dispose();
				new mainMenuColor();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	

}
