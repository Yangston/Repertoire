import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.io.*;

@SuppressWarnings("unchecked")
public class DailySchedule extends JPanel implements ActionListener, MouseListener, MouseWheelListener{
	public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();	
	public static int PANEL_HEIGHT = (dim.height*9)/10;
	public static int PANEL_WIDTH = (dim.width*9)/10;
	public static Timer timer;
	public static SlotsAndOptions exerciseOptions[] = new SlotsAndOptions[61], exerciseSlots[] = new SlotsAndOptions[61], dailySlots[] = new SlotsAndOptions[49], timeSlots[] = new SlotsAndOptions[49];
	public static Workouts workouts[] = new Workouts[61];
	public static String[] viewLayouts = {"Daily", "Weekly"}, workoutDetails = new String[4], weekNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}, allDataArr;
	public static String currentLine = "", user = "", selectedDay = "", allData = "", tempLine = "";
	public static ArrayList<String> scheduleData = new ArrayList<String>(), tempData;
	public static JComboBox selectMenu;
	public static JLabel slotsCont, optionsCont, menuBar;
	private static Point mousePos;
	public static int JpanelX, JpanelY, mouseLatestX = 0, mouseLatestY = 0, labelHeld = 0, clipX = 0, clipY = 0, tickCounter = 0, scrollSensitivity = 40, counter = 0, accountCounter = 0, accountNum = 0;
	public static boolean mouseHold = false, clip = false, selected = true, scrollUp = false, scrollDown = false, accountFound = false, paintLoad = false;
	private Scanner scFile = null, dataScan;
	private static FileWriter dataWriter;

    public DailySchedule() {
		timer = new Timer(10, this);	
		mousePos = MouseInfo.getPointerInfo().getLocation();

		this.setLayout(null);
		addMouseListener(this);
		addMouseWheelListener(this);

        JpanelX = ((dim.width-PANEL_WIDTH)/2)+5;
        JpanelY = ((dim.height-PANEL_HEIGHT)/2)+30;
		
		/*
		menuBar = new MenuBar(0, 0, PANEL_WIDTH-14, PANEL_HEIGHT/10);
		menuBar.setBounds(menuBar.posX, menuBar.posY, menuBar.sizeX, menuBar.sizeY);
		menuBar.setOpaque(true);
		menuBar.setBackground(new Color(255, 165, 205));
		menuBar.setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 4));
		this.add(menuBar);
		*/
		
		selectMenu = new JComboBox(viewLayouts);
		selectMenu.setVisible(true);
		selectMenu.setEditable(false);
		selectMenu.setBounds(8*(PANEL_WIDTH/10), (PANEL_HEIGHT/20)-10, 100, 20);
		this.add(selectMenu);

		menuBar = new JLabel();
		menuBar.setBounds(0, 0, PANEL_WIDTH-14, PANEL_HEIGHT/10);
		menuBar.setOpaque(true);
		menuBar.setBackground(new Color(255, 165, 205));
		this.add(menuBar);
		
		try {
			scFile = new Scanner(new File("WorkoutList.txt"));
		}
		catch (Exception error) {
			System.out.println("An error occurred:" + error);
			error.printStackTrace();
		}
		
		for(int i = 0; i < exerciseOptions.length; i++){
			currentLine = scFile.nextLine();
			workoutDetails = currentLine.split("% ");
			workouts[i] = new Workouts(workoutDetails[0], workoutDetails[1], workoutDetails[2], workoutDetails[3], Integer.parseInt(workoutDetails[4]));
			exerciseOptions[i] = new SlotsAndOptions(1000, 200+(i*60), 300, 50);
			exerciseOptions[i].setBounds(exerciseOptions[i].posX, exerciseOptions[i].posY, exerciseOptions[i].sizeX, exerciseOptions[i].sizeY);
			exerciseOptions[i].setOpaque(true);
			if(workouts[i].difficulty.equals("Beginner")){
				exerciseOptions[i].setBackground(new Color(205, 255, 205));
			}
			else if(workouts[i].difficulty.equals("Intermediate")){
				exerciseOptions[i].setBackground(new Color(255, 255, 205));
			}
			else if(workouts[i].difficulty.equals("Advanced")){
				exerciseOptions[i].setBackground(new Color(255, 205, 205));
			}
			exerciseOptions[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 2));
			exerciseOptions[i].setFont(new Font("SansSerif", 1, 18));
			exerciseOptions[i].setText(exerciseOptions[i].getText() + " " + workouts[i].name + "- " + workouts[i].muscleGroup);
			this.add(exerciseOptions[i]);
			exerciseSlots[i] = new SlotsAndOptions(980, 195+(i*60), 340, 60);
			exerciseSlots[i].setBounds(exerciseSlots[i].posX, exerciseSlots[i].posY, exerciseSlots[i].sizeX, exerciseSlots[i].sizeY);
			exerciseSlots[i].setOpaque(true);
			if(workouts[i].difficulty.equals("Beginner")){
				exerciseSlots[i].setBackground(new Color(135, 205, 135, 85));
			}
			else if(workouts[i].difficulty.equals("Intermediate")){
				exerciseSlots[i].setBackground(new Color(205, 205, 135, 85));
			}
			else if(workouts[i].difficulty.equals("Advanced")){
				exerciseSlots[i].setBackground(new Color(205, 135, 135, 85));
			}
			exerciseSlots[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 1));
			this.add(exerciseSlots[i]);
		}
		scFile.close();

		for(int i = 0; i < dailySlots.length; i++){
			dailySlots[i] = new SlotsAndOptions(125, 200+(i*68), 800, 70);
			dailySlots[i].setBounds(dailySlots[i].posX, dailySlots[i].posY, dailySlots[i].sizeX, dailySlots[i].sizeY);
			dailySlots[i].setOpaque(true);
			dailySlots[i].setBackground(new Color(255, 255, 255));
			dailySlots[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 3));
			this.add(dailySlots[i]);
			timeSlots[i] = new SlotsAndOptions(50, 200+(i*68), 95, 70);
			timeSlots[i].setBounds(timeSlots[i].posX, timeSlots[i].posY, timeSlots[i].sizeX, timeSlots[i].sizeY);
			timeSlots[i].setOpaque(true);
			timeSlots[i].setBackground(new Color(255, 255, 255));
			timeSlots[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 3));
			this.add(timeSlots[i]);
		}
		
		counter = 0;
		for(double i = 8; counter < dailySlots.length; i+=0.25){
			timeSlots[counter].setFont(new Font("SansSerif", 1, 20));
			if(i%1==0){
				timeSlots[counter].setText(timeSlots[counter].getText()+" "+(int)i + ":00");
			}
			else{
				timeSlots[counter].setText(timeSlots[counter].getText()+" "+(int)i + ":" + (((int)((i%1)/0.25)*15)));
			}
			counter++;
		}

		slotsCont = new JLabel();
		slotsCont.setBounds(0, PANEL_HEIGHT/10, 950, PANEL_HEIGHT);
		slotsCont.setOpaque(true);
		slotsCont.setBackground(new Color(205, 205, 245));
		slotsCont.setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 1));
		this.add(slotsCont);
		
		optionsCont = new JLabel();
		optionsCont.setBounds(950, PANEL_HEIGHT/10, 430, PANEL_HEIGHT);
		optionsCont.setOpaque(true);
		optionsCont.setBackground(new Color(205, 245, 245));
		optionsCont.setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 1));
		this.add(optionsCont);
    }
	
	public void run(){	
		timer.start();	
		selected = true;
		selectMenu.setSelectedItem("Daily");
		loadData(selectedDay);
	}
	
	public void stop(){	
		reset();	
		selected = false;
		timer.stop();	
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(255, 255, 205));
		g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

		if(paintLoad){
			for(int i = 1; i < scheduleData.size(); i+=3){
				if(Integer.parseInt(scheduleData.get(i)) != java.util.Arrays.asList(weekNames).indexOf(selectedDay)){
					continue;
				}
				clip = true;
				labelHeld = Integer.parseInt(scheduleData.get(i+2));
				dailySlots[Integer.parseInt(scheduleData.get(i+1))].taken = true;
				dailySlots[Integer.parseInt(scheduleData.get(i+1))].holding = labelHeld;
				clipX = dailySlots[Integer.parseInt(scheduleData.get(i+1))].posX+(dailySlots[Integer.parseInt(scheduleData.get(i+1))].sizeX/2)-(exerciseOptions[labelHeld].sizeX/2);
				clipY = dailySlots[Integer.parseInt(scheduleData.get(i+1))].posY+(dailySlots[Integer.parseInt(scheduleData.get(i+1))].sizeY/2)-(exerciseOptions[labelHeld].sizeY/2);
				exerciseOptions[labelHeld].posX = clipX;
				exerciseOptions[labelHeld].posY = clipY;
				while(exerciseOptions[labelHeld].sizeX < dailySlots[0].sizeX-12){
					exerciseOptions[labelHeld].setBounds(clipX-=(((dailySlots[0].sizeX-exerciseOptions[labelHeld].sizeX)/20)+1), clipY, exerciseOptions[labelHeld].sizeX+=2*(((dailySlots[0].sizeX-exerciseOptions[labelHeld].sizeX)/20)+1), exerciseOptions[labelHeld].sizeY);
					exerciseOptions[labelHeld].posX = clipX;
				}
			}
			dataScan.close();	
			clip = false;
			paintLoad = false;
		}

		if(mouseHold){
			if(exerciseOptions[labelHeld].held){
				exerciseOptions[labelHeld].posX = mousePos.x-JpanelX-(exerciseOptions[labelHeld].sizeX)/2;
				exerciseOptions[labelHeld].posY = mousePos.y-JpanelY-(exerciseOptions[labelHeld].sizeY)/2;
				exerciseOptions[labelHeld].sizeX = exerciseOptions[labelHeld].origSizeX;
				
				if(!clip){
					exerciseOptions[labelHeld].setBounds(exerciseOptions[labelHeld].posX, exerciseOptions[labelHeld].posY, exerciseOptions[labelHeld].origSizeX, exerciseOptions[labelHeld].origSizeY);
				}
				else if(clip){
					exerciseOptions[labelHeld].setBounds(clipX, clipY, exerciseOptions[labelHeld].sizeX, exerciseOptions[labelHeld].sizeY);
				}
			}
			
			clip = false;
			for(int i = 0; i < dailySlots.length; i++){
				if((dailySlots[i].getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY))) && !dailySlots[i].taken){
					clip = true;
					clipX = dailySlots[i].posX+(dailySlots[i].sizeX/2)-(exerciseOptions[labelHeld].sizeX/2);
					clipY = dailySlots[i].posY+(dailySlots[i].sizeY/2)-(exerciseOptions[labelHeld].sizeY/2);
					exerciseOptions[labelHeld].posX = clipX;
					exerciseOptions[labelHeld].posY = clipY;
				}
			}
		}	
		if(exerciseOptions[labelHeld].sizeX < dailySlots[0].sizeX-12 && clip){
			exerciseOptions[labelHeld].setBounds(clipX-=(((dailySlots[0].sizeX-exerciseOptions[labelHeld].sizeX)/20)+1), clipY, exerciseOptions[labelHeld].sizeX+=2*(((dailySlots[0].sizeX-exerciseOptions[labelHeld].sizeX)/20)+1), exerciseOptions[labelHeld].sizeY);
			exerciseOptions[labelHeld].posX = clipX;
		}
		
		if(scrollUp){
			if(slotsCont.getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY)) && dailySlots[0].posY < dailySlots[0].origY){
				for(int j = 0; j < dailySlots.length; j++){
					dailySlots[j].posY+=scrollSensitivity;
					dailySlots[j].setBounds(dailySlots[j].posX, dailySlots[j].posY, dailySlots[j].sizeX, dailySlots[j].sizeY);
					timeSlots[j].posY+=scrollSensitivity;
					timeSlots[j].setBounds(timeSlots[j].posX, timeSlots[j].posY, timeSlots[j].sizeX, timeSlots[j].sizeY);
				}
				for(int i = 0; i < exerciseOptions.length; i++){
					if(exerciseOptions[i].posX != exerciseOptions[i].origX && exerciseOptions[i].posY != exerciseOptions[i].origY && !mouseHold){
						exerciseOptions[i].posY+=scrollSensitivity;
						exerciseOptions[i].setBounds(exerciseOptions[i].posX, exerciseOptions[i].posY, exerciseOptions[i].sizeX, exerciseOptions[i].sizeY);
					}
				}	
			}
			else if(optionsCont.getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY)) && exerciseSlots[0].posY < exerciseSlots[0].origY){
				for(int i = 0; i < exerciseOptions.length; i++){
					if(exerciseOptions[i].posX == exerciseOptions[i].origX){
						exerciseOptions[i].posY+=scrollSensitivity;
						exerciseOptions[i].setBounds(exerciseOptions[i].posX, exerciseOptions[i].posY, exerciseOptions[i].sizeX, exerciseOptions[i].sizeY);
					}
					exerciseSlots[i].posY+=scrollSensitivity;
					exerciseSlots[i].setBounds(exerciseSlots[i].posX, exerciseSlots[i].posY, exerciseSlots[i].sizeX, exerciseSlots[i].sizeY);
				}		
			}
			scrollUp = false;
		}
		else if(scrollDown){
			if(slotsCont.getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY)) && dailySlots[48].posY > 650){
				for(int j = 0; j < dailySlots.length; j++){
					dailySlots[j].posY-=scrollSensitivity;
					timeSlots[j].posY-=scrollSensitivity;
					dailySlots[j].setBounds(dailySlots[j].posX, dailySlots[j].posY, dailySlots[j].sizeX, dailySlots[j].sizeY);
					timeSlots[j].setBounds(timeSlots[j].posX, timeSlots[j].posY, timeSlots[j].sizeX, timeSlots[j].sizeY);
				}
				for(int i = 0; i < exerciseOptions.length; i++){
					if(exerciseOptions[i].posX != exerciseOptions[i].origX && exerciseOptions[i].posY != exerciseOptions[i].origY && !mouseHold){
						exerciseOptions[i].posY-=scrollSensitivity;
						exerciseOptions[i].setBounds(exerciseOptions[i].posX, exerciseOptions[i].posY, exerciseOptions[i].sizeX, exerciseOptions[i].sizeY);
					}
				}
			}
			else if(optionsCont.getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY)) && exerciseSlots[60].posY > 670){
				for(int i = 0; i < exerciseOptions.length; i++){
					if(exerciseOptions[i].posX == exerciseOptions[i].origX){
						exerciseOptions[i].posY-=scrollSensitivity;
						exerciseOptions[i].setBounds(exerciseOptions[i].posX, exerciseOptions[i].posY, exerciseOptions[i].sizeX, exerciseOptions[i].sizeY);
					}
					exerciseSlots[i].posY-=scrollSensitivity;
					exerciseSlots[i].setBounds(exerciseSlots[i].posX, exerciseSlots[i].posY, exerciseSlots[i].sizeX, exerciseSlots[i].sizeY);
				}
			}
			scrollDown = false;
		}
		tickCounter++;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(255, 165, 205));
		g.fillRect(0, 0, PANEL_WIDTH-400, PANEL_HEIGHT/10);
		g.setColor(new Color(0, 0, 0));
		g.setFont(new Font("SansSerif: ", 1, 28));
		g.drawString("Daily Schedule", ((PANEL_WIDTH-14)/2)-80, ((PANEL_HEIGHT/10)/2)+10);

		g.setColor(new Color(195, 195, 195));
		g.fillRect(0, PANEL_HEIGHT/10, 950, 100);
		g.fillRect(950, PANEL_HEIGHT/10, 430, 100);
		g.setColor(new Color(0, 0, 0));
		g.drawRect(950, PANEL_HEIGHT/10, 430, 100);
		g.drawRect(0, PANEL_HEIGHT/10, 950, 100);
		g.setColor(new Color(0, 0, 0));
		g.setFont(new Font("SansSerif: ", 1, 40));
		g.drawString("Schedule Timeslots", 275, (PANEL_HEIGHT/10)+65);
		g.drawString("Workout Options", 1000, (PANEL_HEIGHT/10)+65);
	}
	
	public void actionPerformed(ActionEvent ev){	
		mousePos = MouseInfo.getPointerInfo().getLocation();
		buttonChecker(ev);
		
		if (((String)selectMenu.getSelectedItem()).equals("Weekly")){
			stop();
		}
		
		repaint();
	}
	
	public static void buttonChecker(ActionEvent ev){
		//(jButton1.getModel().isPressed())
	}


	public void mouseClicked(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		mouseLatestX = e.getX();
		mouseLatestY = e.getY();
		clip = false;
		
		for(int i = 0; i < exerciseOptions.length; i++){
			if(exerciseOptions[i].getBounds().contains(new Point(mouseLatestX, mouseLatestY))){
				mouseHold = true;
				exerciseOptions[i].held = true;
				labelHeld = i;
			}
		}
		for(int i = 0; i < dailySlots.length; i++){
			if(dailySlots[i].getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY))){
				dailySlots[i].taken = false;
				dailySlots[i].holding = -1;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(!clip && mouseHold){
			exerciseOptions[labelHeld].posX = exerciseSlots[labelHeld].posX+20;
			exerciseOptions[labelHeld].posY = exerciseSlots[labelHeld].posY+5;
			exerciseOptions[labelHeld].setBounds(exerciseOptions[labelHeld].posX, exerciseOptions[labelHeld].posY, exerciseOptions[labelHeld].origSizeX, exerciseOptions[labelHeld].origSizeY);
		}
		for(int i = 0; i < exerciseOptions.length; i++){
			exerciseOptions[i].held = false;
		}
		for(int i = 0; i < dailySlots.length; i++){
			if(dailySlots[i].getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY)) && mouseHold){
				dailySlots[i].taken = true;
				dailySlots[i].holding = labelHeld;
			}
		}
		mouseHold = false;
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() < 0){
			scrollUp = true;
		} 
		else if(e.getWheelRotation() > 0){
			scrollDown = true;
		}
	}
	
	public void reset(){
		for(int i = 0; i < exerciseOptions.length; i++){
			exerciseOptions[i].held = false;
			exerciseOptions[i].posX = exerciseOptions[i].origX;
			exerciseOptions[i].posY = exerciseOptions[i].origY;
			exerciseOptions[i].sizeX = exerciseOptions[i].origSizeX;
			exerciseOptions[i].sizeY = exerciseOptions[i].origSizeY;
			exerciseSlots[i].posX = exerciseSlots[i].origX;
			exerciseSlots[i].posY = exerciseSlots[i].origY;
			exerciseOptions[i].setBounds(exerciseOptions[i].posX, exerciseOptions[i].posY, exerciseOptions[i].sizeX, exerciseOptions[i].sizeY);
			exerciseSlots[i].setBounds(exerciseSlots[i].posX, exerciseSlots[i].posY, exerciseSlots[i].sizeX, exerciseSlots[i].sizeY);
		}
		for(int i = 0; i < dailySlots.length; i++){
			dailySlots[i].taken = false;
		}
	}
	
	public void loadData(String day){
		try {
			dataScan = new Scanner(new File("ScheduleData.txt"));
		}
		catch (Exception error) {
			System.out.println("An error occurred:" + error);
			error.printStackTrace();
		}
		accountCounter = 0;
		allData = "";
		currentLine = "";
		accountFound = false;
		while(dataScan.hasNextLine()){
			currentLine = dataScan.nextLine();
			allData += currentLine + "%";
			tempData = new ArrayList<>(Arrays.asList(currentLine.split(", ")));
			if(user.equals(tempData.get(0))){
				accountFound = true;
				accountNum = accountCounter;
				scheduleData = tempData;
			}
			accountCounter++;
		}
		allDataArr = allData.split("%");
		paintLoad = true;
		dataScan.close();
	}
}