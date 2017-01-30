import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

	
public class mainMenuColor extends JFrame implements ActionListener
{
	private JPanel menuPanel;
	private JLabel displayLabel;
	private JButton redButton;
	private JButton blueButton;
	private JButton greenButton;
	public String color;
	
	
	public mainMenuColor()
	{
		super("Educational Colors and Shapes");
		setSize(550, 450);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		displayLabel = new JLabel("Choose a Color!");
		displayLabel.setFont(displayLabel.getFont().deriveFont(20f));
	
		//establish a panel for the menu
		menuPanel = new JPanel();
		
		//button that initiates the transition to the mainMenuColorFrame		
		redButton = new JButton("Red");
		redButton.setBackground(Color.RED);
		redButton.setOpaque(true);	
		redButton.addActionListener(this);
		
		blueButton = new JButton("Blue");
		blueButton.setBackground(Color.BLUE);
		blueButton.setOpaque(true);
		blueButton.addActionListener(this);
		
		greenButton = new JButton("Green");
		greenButton.setBackground(Color.GREEN);
		greenButton.setOpaque(true);
		greenButton.addActionListener(this);
	
		//add buttons to menuPanel
			menuPanel.setLayout(new GridLayout(15,3));		

				menuPanel.add(new JLabel(" "));		menuPanel.add(displayLabel);		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		addAButton(redButton,menuPanel);	menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" ")); 	menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		addAButton(blueButton,menuPanel);	menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		addAButton(greenButton,menuPanel);	menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));
				
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
			if(ae.getActionCommand() == "Red")
			{
				color = "Red";
				this.setVisible(false);
				this.dispose();
				new mainMenuShape(color);
			}
			
			if(ae.getActionCommand() == "Blue")
			{
				color = "Blue";
				this.setVisible(false);
				this.dispose();
				new mainMenuShape(color);
			}
			
			if(ae.getActionCommand() == "Green")
			{
				color = "Green";
				this.setVisible(false);
				this.dispose();
				new mainMenuShape(color);
			}
		
	}
	

}



