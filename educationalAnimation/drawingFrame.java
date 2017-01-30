import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class drawingFrame extends JFrame implements ActionListener
{
	public static String COLOR;
	public static String SHAPE;

	private JPanel northPanel;
	private JPanel southPanel;
	private JButton restart;
		
	private JLabel descriptionLabel;

	public drawingFrame(String col, String shap)
	{
		super("Educational Colors and Shapes");
		setSize(550, 450);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		COLOR = col;
		SHAPE = shap;
		
		northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());
		descriptionLabel = new JLabel("This is a "+col+" "+shap+"!");
		descriptionLabel.setFont(descriptionLabel.getFont().deriveFont(23f));
		northPanel.add(descriptionLabel);
		add(northPanel, BorderLayout.NORTH);
		
		restart = new JButton("Pick a new Color and Shape!");
		restart.addActionListener(this);
		southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.add(restart, BorderLayout.CENTER);
	
		add(southPanel,BorderLayout.SOUTH);
	}

    
	public void actionPerformed(ActionEvent ae) 
	{
			if(ae.getActionCommand() == "Pick a new Color and Shape!")
			{
				this.setVisible(false);
				this.dispose();
				new startScreen();
			}
	}
	
	public void paint(Graphics g)
	{	
	
		super.paint(g);
	//red
		if(COLOR.equals("Red"))
		{
			g.setColor(Color.RED);
			
			if(SHAPE.equals("Triangle"))
			{
				int []xPoints={275,370,180};
				int []yPoints={200,320,320};
				g.fillPolygon(xPoints, yPoints, 3);
			}
			
			if(SHAPE.equals("Circle"))
			{
				g.fillOval(175, 150, 200, 200);
			}
			
			if(SHAPE.equals("Square"))
			{
				g.fillRect(175, 150, 200, 200);
			}
		}
		
	//blue
		if(COLOR.equals("Blue"))
		{
			g.setColor(Color.BLUE);
			
			if(SHAPE.equals("Triangle"))
			{
				int []xPoints={275,370,180};
				int []yPoints={200,320,320};
				g.fillPolygon(xPoints, yPoints, 3);
			}
			
			if(SHAPE.equals("Circle"))
			{
				g.fillOval(175, 150, 200, 200);
			}
			
			if(SHAPE.equals("Square"))
			{
				g.fillRect(175, 150, 200, 200);
			}
		}
		
	//green
		if(COLOR.equals("Green"))
		{
			g.setColor(Color.GREEN);
			
			if(SHAPE.equals("Triangle"))
			{
				int []xPoints={275,370,180};
				int []yPoints={200,320,320};
				g.fillPolygon(xPoints, yPoints, 3);
			}
			
			if(SHAPE.equals("Circle"))
			{
				g.fillOval(175, 150, 200, 200);
			}
			
			if(SHAPE.equals("Square"))
			{
				g.fillRect(175, 150, 200, 200);
			}
		}
		
		
	}
}








	