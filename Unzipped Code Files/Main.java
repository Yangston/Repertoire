import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;

public class Main extends JFrame implements ActionListener, KeyListener, MouseListener{
	public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();		
	public static int FRAME_HEIGHT = (dim.height*9)/10;
	public static int FRAME_WIDTH = (dim.width*9)/10;
	public static Main frame = new Main();
	public static Menu menuInterface = new Menu();
	public static DailySchedule dailyScheduleInterface = new DailySchedule();
	public static WeeklySchedule weeklyScheduleInterface = new WeeklySchedule();
	public static CardLayout cl = new CardLayout();
	public static Timer timer;
	public static JPanel panelContainer;
	public static int JframeX, JframeY;
	private static Point mousePos;

    public Main() {
		super("Workout Planner");
		timer = new Timer(50, this);
		timer.start();
		addKeyListener(this);
		addMouseListener(this);
    }
	
	public static void main(String[] args) {
		ImageIcon frameIcon = new ImageIcon("grab.jpg");

		//sets up JFrame
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        frame.setIconImage(frameIcon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		
		//Add jpanels to Jframe
		panelContainer = new JPanel(new CardLayout());	
		panelContainer.add(menuInterface, "Menu Panel");
		panelContainer.add(dailyScheduleInterface, "Daily Schedule Panel");
		panelContainer.add(weeklyScheduleInterface, "Weekly Schedule Panel");
		
		frame.add(panelContainer);	

		cl = (CardLayout)panelContainer.getLayout();
		cl.show(panelContainer, "Menu Panel");		
			
		//Sets position of Jframe
        JframeX = (dim.width-FRAME_WIDTH)/2;
        JframeY = (dim.height-FRAME_HEIGHT)/2;
        frame.setLocation(JframeX, JframeY);
		
		menuInterface.run();
	}
  
	public void keyTyped(KeyEvent e) {
    }
	
    public void keyPressed(KeyEvent e) {
	}
	
    public void keyReleased(KeyEvent e) {
    }
	
	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void paint(Graphics g) {
		super.paint(g);		
		frame.setLocation(JframeX, JframeY);
	}
	
	public void actionPerformed(ActionEvent ev){
		if(menuInterface.loginSuccess){
			cl.show(panelContainer, "Weekly Schedule Panel");	
			weeklyScheduleInterface.requestFocus();
			weeklyScheduleInterface.setFocusable(true);
			menuInterface.loginSuccess = false;
			weeklyScheduleInterface.user = menuInterface.username;
			dailyScheduleInterface.user = menuInterface.username;
			weeklyScheduleInterface.run();
		}
		
		if(!dailyScheduleInterface.selected){
			cl.show(panelContainer, "Weekly Schedule Panel");
			weeklyScheduleInterface.requestFocus();
			weeklyScheduleInterface.setFocusable(true);
			dailyScheduleInterface.selected = true;
			weeklyScheduleInterface.run();
		}
		if(!weeklyScheduleInterface.selected){
			cl.show(panelContainer, "Daily Schedule Panel");
			dailyScheduleInterface.requestFocus();
			dailyScheduleInterface.setFocusable(true);
			weeklyScheduleInterface.selected = true;
			dailyScheduleInterface.selectedDay = weeklyScheduleInterface.selectedDay;
			dailyScheduleInterface.run();
		}
		repaint();
	}
}