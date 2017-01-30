import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class mainMenuShape extends JFrame implements ActionListener
{
	private JPanel menuPanel;
	private JLabel displayLabel;
	private JButton triangleButton;
	private JButton circleButton;
	private JButton squareButton;	
	public String color;
	public String shape;

	public mainMenuShape(String col)
	{
		super("Educational Colors and Shapes");
		setSize(550, 450);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		color = col;
	
		displayLabel = new JLabel("Choose a Shape!");
		displayLabel.setFont(displayLabel.getFont().deriveFont(20f));
		
		//establish a panel for the menu
		menuPanel = new JPanel();
		
	
		//button that initiates the transition to the mainMenuShapeFrame		
		triangleButton = new JButton("Triangle");	
		triangleButton.addActionListener(this);
		
		circleButton = new JButton("Circle");
		circleButton.addActionListener(this);
		
		squareButton = new JButton("Square");
		squareButton.addActionListener(this);
	
		//add buttons to menuPanel
			menuPanel.setLayout(new GridLayout(15,3));		
			
				menuPanel.add(new JLabel(" "));		menuPanel.add(displayLabel);			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		addAButton(triangleButton,menuPanel);	menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" ")); 	menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		addAButton(circleButton,menuPanel);		menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		addAButton(squareButton, menuPanel);	menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				menuPanel.add(new JLabel(" "));		menuPanel.add(new JLabel(" "));			menuPanel.add(new JLabel(" "));
				
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
			if(ae.getActionCommand() == "Triangle")
			{
				shape = "Triangle";
				this.setVisible(false);
				this.dispose();
				new drawingFrame(color, shape);
				
			}
			
			if(ae.getActionCommand() == "Circle")
			{
				shape = "Circle";
				this.setVisible(false);
				this.dispose();
				new drawingFrame(color, shape);
			}
			
			if(ae.getActionCommand() == "Square")
			{
				shape = "Square";
				this.setVisible(false);
				this.dispose();
				new drawingFrame(color, shape);
			}
		
	}
	

}



