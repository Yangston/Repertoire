import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.io.*;
import java.time.*;

@SuppressWarnings("unchecked")
public class WeeklySchedule extends JPanel implements ActionListener, MouseListener, MouseWheelListener{
	public static Calendar calendar = Calendar.getInstance();
	public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();	
	public static int PANEL_HEIGHT = (dim.height*9)/10;
	public static int PANEL_WIDTH = (dim.width*9)/10;
	public static Timer timer;
	public static SlotsAndOptions exerciseOptions[] = new SlotsAndOptions[61], exerciseSlots[] = new SlotsAndOptions[61], dailySlots[][] = new SlotsAndOptions[7][49], timeSlots[] = new SlotsAndOptions[49];
	public static Workouts workouts[] = new Workouts[61], tempWorkout = new Workouts();
	public static String[] viewLayouts = {"Daily", "Weekly"}, workoutDetails = new String[4], sortNames = {"Alphanetical", "Muscle Group", "Difficulty"}, weekNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}, allDataArr;
	public static String currentLine = "", allData = "", tempLine = "", selectedDay = weekNames[calendar.get(Calendar.DAY_OF_WEEK)-1]; 
	public static ArrayList<String> scheduleData = new ArrayList<String>(), tempData;
	public String user = "";
	public static JComboBox selectMenu;
	public static JLabel slotsCont, optionsCont, menuBar;
	public static JButton daysOfWeek[] = new JButton[7], sortOptions[] = new JButton[3];
	private static Point mousePos;
	public static int JpanelX, JpanelY, mouseLatestX = 0, mouseLatestY = 0, labelHeld = 0, clipX = 0, clipY = 0, tickCounter = 0, scrollSensitivity = 40, counter = 0, accountCounter = 0, accountNum = 0;
	public static boolean mouseHold = false, clip = false, selected = true, scrollUp = false, scrollDown = false, accountFound = false, paintLoad = false, enterDayView = false, update = false, scheduleEmpty = true;
	private static Scanner scFile = null, dataScan;
	private static FileWriter dataWriter;

    public WeeklySchedule() {
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
		
		for(int i = 0; i < daysOfWeek.length; i++){
			daysOfWeek[i] = new JButton();
			daysOfWeek[i].setFont(new Font("SansSerif", 1, 14));
			daysOfWeek[i].setText(weekNames[i]);
			daysOfWeek[i].setBounds(100+(i*120), 178, 120, 70);
			daysOfWeek[i].addActionListener(this);
			daysOfWeek[i].setOpaque(true);
			daysOfWeek[i].setContentAreaFilled(true);
			daysOfWeek[i].setBackground(new Color(255, 255, 255));
			daysOfWeek[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 2));
			daysOfWeek[i].setBorderPainted(true);
			daysOfWeek[i].setFocusPainted(false);
			this.add(daysOfWeek[i]);
		}
		
		for(int i = 0; i < sortOptions.length; i++){
			sortOptions[i] = new JButton();
			sortOptions[i].setFont(new Font("SansSerif", 1, 14));
			sortOptions[i].setText(sortNames[i]);
			sortOptions[i].setBounds(951+(i*139), 145, 139, 34);
			sortOptions[i].addActionListener(this);
			sortOptions[i].setOpaque(true);
			sortOptions[i].setContentAreaFilled(true);
			sortOptions[i].setBackground(new Color(255, 255, 255));
			sortOptions[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 2));
			sortOptions[i].setBorderPainted(true);
			sortOptions[i].setFocusPainted(false);
			this.add(sortOptions[i]);
		}
		
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
			else if(workouts[i].difficulty.equals("Experienced")){
				exerciseOptions[i].setBackground(new Color(255, 255, 205));
			}
			else if(workouts[i].difficulty.equals("Expert")){
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
			else if(workouts[i].difficulty.equals("Experienced")){
				exerciseSlots[i].setBackground(new Color(205, 205, 135, 85));
			}
			else if(workouts[i].difficulty.equals("Expert")){
				exerciseSlots[i].setBackground(new Color(205, 135, 135, 85));
			}
			exerciseSlots[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 1));
			this.add(exerciseSlots[i]);
		}
		scFile.close();
		
		for(int i = 0; i < dailySlots.length; i++){
			for(int j = 0; j <dailySlots[i].length; j++){
				dailySlots[i][j] = new SlotsAndOptions(100+(i*120), 246+(j*68), 120, 70);
				dailySlots[i][j].setBounds(dailySlots[i][j].posX, dailySlots[i][j].posY, dailySlots[i][j].sizeX, dailySlots[i][j].sizeY);
				dailySlots[i][j].setOpaque(true);
				dailySlots[i][j].setBackground(new Color(255, 255, 255));
				dailySlots[i][j].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 2));
				this.add(dailySlots[i][j]);
			}
		}
		
		counter = 0;
		for(double i = 8; counter < timeSlots.length; i+=0.25){
			timeSlots[counter] = new SlotsAndOptions(10, 246+(counter*68), 95, 70);
			timeSlots[counter].setBounds(timeSlots[counter].posX, timeSlots[counter].posY, timeSlots[counter].sizeX, timeSlots[counter].sizeY);
			timeSlots[counter].setOpaque(true);
			timeSlots[counter].setBackground(new Color(255, 255, 255));
			timeSlots[counter].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 3));
			this.add(timeSlots[counter]);
			
			timeSlots[counter].setFont(new Font("SansSerif", 1, 20));
			if(i%1==0){
				timeSlots[counter].setText(timeSlots[counter].getText()+"   "+(int)i + ":00");
			}
			else{
				timeSlots[counter].setText(timeSlots[counter].getText()+"   "+(int)i + ":" + (((int)((i%1)/0.25)*15)));
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
		selectMenu.setSelectedItem("Weekly");	
		selectedDay = weekNames[calendar.get(Calendar.DAY_OF_WEEK)-1];
		loadData();
	}
	
	public void stop(){		
		selected = false;
		timer.stop();	
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(255, 255, 205));
		g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		if(paintLoad){
			for(int i = 1; i < scheduleData.size(); i+=3){
				scheduleEmpty = false;
				clip = true;
				labelHeld = Integer.parseInt(scheduleData.get(i+2));
				dailySlots[Integer.parseInt(scheduleData.get(i))][Integer.parseInt(scheduleData.get(i+1))].taken = true;
				dailySlots[Integer.parseInt(scheduleData.get(i))][Integer.parseInt(scheduleData.get(i+1))].holding = workouts[labelHeld].num;
				clipX = dailySlots[Integer.parseInt(scheduleData.get(i))][Integer.parseInt(scheduleData.get(i+1))].posX+(dailySlots[Integer.parseInt(scheduleData.get(i))][Integer.parseInt(scheduleData.get(i+1))].sizeX/2)-(exerciseOptions[labelHeld].sizeX/2);
				clipY = dailySlots[Integer.parseInt(scheduleData.get(i))][Integer.parseInt(scheduleData.get(i+1))].posY+(dailySlots[Integer.parseInt(scheduleData.get(i))][Integer.parseInt(scheduleData.get(i+1))].sizeY/2)-(exerciseOptions[labelHeld].sizeY/2);
				exerciseOptions[labelHeld].posX = clipX;
				exerciseOptions[labelHeld].posY = clipY;
				exerciseOptions[labelHeld].inUse = true;
				while(exerciseOptions[labelHeld].sizeX > dailySlots[0][0].sizeX-12){
					exerciseOptions[labelHeld].setBounds(clipX+=(((exerciseOptions[labelHeld].sizeX-dailySlots[0][0].sizeX)/20)+1), clipY, exerciseOptions[labelHeld].sizeX-=2*(((exerciseOptions[labelHeld].sizeX-dailySlots[0][0].sizeX)/20)+1), exerciseOptions[labelHeld].sizeY);
					exerciseOptions[labelHeld].posX = clipX;
				}
			}
			dataScan.close();	
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
				for(int j = 0; j <dailySlots[i].length; j++){
					if((dailySlots[i][j].getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY))) && !dailySlots[i][j].taken){
						clip = true;
						clipX = dailySlots[i][j].posX+(dailySlots[i][j].sizeX/2)-(exerciseOptions[labelHeld].sizeX/2);
						clipY = dailySlots[i][j].posY+(dailySlots[i][j].sizeY/2)-(exerciseOptions[labelHeld].sizeY/2);
						exerciseOptions[labelHeld].posX = clipX;
						exerciseOptions[labelHeld].posY = clipY;
					}
				}
			}
		}	
		if(exerciseOptions[labelHeld].sizeX > dailySlots[0][0].sizeX-12 && clip){
			exerciseOptions[labelHeld].setBounds(clipX+=(((exerciseOptions[labelHeld].sizeX-dailySlots[0][0].sizeX)/20)+1), clipY, exerciseOptions[labelHeld].sizeX-=2*(((exerciseOptions[labelHeld].sizeX-dailySlots[0][0].sizeX)/20)+1), exerciseOptions[labelHeld].sizeY);
			exerciseOptions[labelHeld].posX = clipX;
		}
		
		if(scrollUp){
			if(slotsCont.getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY)) && dailySlots[0][0].posY < dailySlots[0][0].origY){
				for(int i = 0; i < dailySlots.length; i++){
					for(int j = 0; j <dailySlots[i].length; j++){
						dailySlots[i][j].posY+=scrollSensitivity;
						dailySlots[i][j].setBounds(dailySlots[i][j].posX, dailySlots[i][j].posY, dailySlots[i][j].sizeX, dailySlots[i][j].sizeY);
					}
				}
				for(int i = 0; i < timeSlots.length; i++){
					timeSlots[i].posY+=scrollSensitivity;
					timeSlots[i].setBounds(timeSlots[i].posX, timeSlots[i].posY, timeSlots[i].sizeX, timeSlots[i].sizeY);
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
			if(slotsCont.getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY)) && dailySlots[0][48].posY > 650){
				for(int i = 0; i < dailySlots.length; i++){
					for(int j = 0; j <dailySlots[i].length; j++){
						dailySlots[i][j].posY-=scrollSensitivity;
						dailySlots[i][j].setBounds(dailySlots[i][j].posX, dailySlots[i][j].posY, dailySlots[i][j].sizeX, dailySlots[i][j].sizeY);
					}
				}
				for(int i = 0; i < timeSlots.length; i++){
					timeSlots[i].posY-=scrollSensitivity;
					timeSlots[i].setBounds(timeSlots[i].posX, timeSlots[i].posY, timeSlots[i].sizeX, timeSlots[i].sizeY);
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

		if(tickCounter%100 == 0){
			saveData();
		}

		tickCounter++;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(255, 165, 205));
		g.fillRect(0, 0, PANEL_WIDTH-400, PANEL_HEIGHT/10);
		g.setColor(new Color(0, 0, 0));
		g.setFont(new Font("SansSerif: ", 1, 28));
		g.drawString("Weekly Schedule", ((PANEL_WIDTH-14)/2)-80, ((PANEL_HEIGHT/10)/2)+10);

		g.setColor(new Color(195, 195, 195));
		g.fillRect(0, PANEL_HEIGHT/10, 950, 100);
		g.fillRect(950, PANEL_HEIGHT/10, 430, 70);
		g.setColor(new Color(0, 0, 0));
		g.drawRect(950, PANEL_HEIGHT/10, 430, 70);
		g.drawRect(0, PANEL_HEIGHT/10, 950, 100);
		g.setColor(new Color(0, 0, 0));
		g.setFont(new Font("SansSerif: ", 1, 40));
		g.drawString("Schedule Timeslots", 275, (PANEL_HEIGHT/10)+65);
		g.drawString("Workout Options", 1000, (PANEL_HEIGHT/10)+45);
		
		if(mouseHold){
			g.setColor(new Color(252, 195, 3));
			g.fillRect(exerciseOptions[labelHeld].posX, exerciseOptions[labelHeld].posY, exerciseOptions[labelHeld].sizeX, exerciseOptions[labelHeld].sizeY);
			g.setColor(new Color(0, 0, 0));
			g.drawRect(exerciseOptions[labelHeld].posX, exerciseOptions[labelHeld].posY, exerciseOptions[labelHeld].sizeX, exerciseOptions[labelHeld].sizeY);
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("SansSerif: ", 1, 20));
			g.drawString(workouts[labelHeld].name + "- " + workouts[labelHeld].muscleGroup, exerciseOptions[labelHeld].posX+(exerciseOptions[labelHeld].sizeX/20), exerciseOptions[labelHeld].posY+(2*exerciseOptions[labelHeld].sizeY/3));
		}
		
		if(update){
			refresh();
			update = false;
		}
	}
	
	public void actionPerformed(ActionEvent ev){	
		mousePos = MouseInfo.getPointerInfo().getLocation();
		buttonChecker(ev);

		if (((String)selectMenu.getSelectedItem()).equals("Daily") || enterDayView){
			selectMenu.setSelectedItem("Weekly");
			enterDayView = false;
			stop();
		}
		
		repaint();
	}
	
	public static void buttonChecker(ActionEvent ev){
		for(int i = 0; i < daysOfWeek.length; i++){
			if (ev.getSource() == daysOfWeek[i]){
				selectedDay = weekNames[i];
				enterDayView = true;
				break;
			}
		}
		
		if(ev.getSource() == sortOptions[0] && scheduleEmpty){
			for(int i = 0; i < workouts.length-1; i++){
				for(int j =0; j<i+1; j++){
					if(workouts[i+1-j].name.compareTo(workouts[i-j].name) < 0){
						tempWorkout = workouts[i+1-j];
						workouts[i+1-j] = workouts[i-j];
						workouts[i-j] = tempWorkout;
					}
				}
			}
			update = true;
		}
		
		if(ev.getSource() == sortOptions[1] && scheduleEmpty){
			for(int i = 0; i < workouts.length-1; i++){
				for(int j =0; j<i+1; j++){
					if(workouts[i+1-j].muscleGroup.compareTo(workouts[i-j].muscleGroup) < 0){
						tempWorkout = workouts[i+1-j];
						workouts[i+1-j] = workouts[i-j];
						workouts[i-j] = tempWorkout;
					}
				}
			}
			update = true;
		}
		
		if(ev.getSource() == sortOptions[2] && scheduleEmpty){
			for(int i = 0; i < workouts.length-1; i++){
				for(int j =0; j<i+1; j++){
					if(workouts[i+1-j].difficulty.compareTo(workouts[i-j].difficulty) < 0){
						tempWorkout = workouts[i+1-j];
						workouts[i+1-j] = workouts[i-j];
						workouts[i-j] = tempWorkout;
					}
				}
			}
			update = true;
		}
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
			for(int j = 0; j <dailySlots[i].length; j++){
				if(dailySlots[i][j].getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY))){
					dailySlots[i][j].taken = false;
					exerciseOptions[labelHeld].inUse = false;
					dailySlots[i][j].holding = -1;
				}
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(!clip && mouseHold){
			exerciseOptions[labelHeld].posX = exerciseSlots[labelHeld].posX+20;
			exerciseOptions[labelHeld].posY = exerciseSlots[labelHeld].posY+5;
			exerciseOptions[labelHeld].setBounds(exerciseOptions[labelHeld].posX, exerciseOptions[labelHeld].posY, exerciseOptions[labelHeld].origSizeX, exerciseOptions[labelHeld].origSizeY);
		}
		
		scheduleEmpty = true;
		for(int i = 0; i < exerciseOptions.length; i++){
			exerciseOptions[i].held = false;
			if(exerciseOptions[i].inUse){
				scheduleEmpty = false;
			}
		}
		for(int i = 0; i < dailySlots.length; i++){
			for(int j = 0; j <dailySlots[i].length; j++){
				if(dailySlots[i][j].getBounds().contains(new Point(mousePos.x-JpanelX, mousePos.y-JpanelY)) && mouseHold){
					dailySlots[i][j].taken = true;
					exerciseOptions[labelHeld].inUse = true;
					dailySlots[i][j].holding = workouts[labelHeld].num;
				}
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
	
	public void loadData(){
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
	
	public void saveData(){		
		tempLine = "";
		try {
			tempLine += user;
			
			for(int i = 0; i < dailySlots.length; i++){
				for(int j = 0; j <dailySlots[i].length; j++){
					if(dailySlots[i][j].taken){
						tempLine += (", " + i + ", " + j + ", " + dailySlots[i][j].holding);
					}
				}
			}
			allDataArr[accountNum] = tempLine;
			
			dataWriter = new FileWriter("ScheduleData.txt", false);
			BufferedWriter bDW = new BufferedWriter(dataWriter);
			for(int i = 0; i < allDataArr.length; i++){
				bDW.write(allDataArr[i]);
				bDW.newLine();
			}
			
			bDW.close();
		} 
		catch (IOException error) {
			System.out.println("An error occurred:" + error);
			error.printStackTrace();
		}
	}
	
	public void refresh(){
		this.remove(selectMenu);
		this.remove(menuBar);
		
		for(int i = 0; i < daysOfWeek.length; i++){
			this.remove(daysOfWeek[i]);
		}
		
		for(int i = 0; i < sortOptions.length; i++){
			this.remove(sortOptions[i]);
		}
		
		for(int i = 0; i < exerciseOptions.length; i++){
			this.remove(exerciseOptions[i]);
			this.remove(exerciseSlots[i]);
		}
		
		for(int i = 0; i < dailySlots.length; i++){
			for(int j = 0; j <dailySlots[i].length; j++){
				this.remove(dailySlots[i][j]);
			}
		}
		
		counter = 0;
		for(double i = 8; counter < timeSlots.length; i+=0.25){
			this.remove(timeSlots[counter]);
			counter++;
		}
		this.remove(slotsCont);
		this.remove(optionsCont);
		
		this.add(selectMenu);
		this.add(menuBar);
		
		for(int i = 0; i < daysOfWeek.length; i++){
			this.add(daysOfWeek[i]);
		}
		
		for(int i = 0; i < sortOptions.length; i++){
			this.add(sortOptions[i]);
		}
		
		for(int i = 0; i < exerciseOptions.length; i++){
			exerciseOptions[i].setBounds(exerciseOptions[i].posX, exerciseOptions[i].posY, exerciseOptions[i].sizeX, exerciseOptions[i].sizeY);
			exerciseOptions[i].setOpaque(true);
			if(workouts[i].difficulty.equals("Beginner")){
				exerciseOptions[i].setBackground(new Color(205, 255, 205));
			}
			else if(workouts[i].difficulty.equals("Experienced")){
				exerciseOptions[i].setBackground(new Color(255, 255, 205));
			}
			else if(workouts[i].difficulty.equals("Expert")){
				exerciseOptions[i].setBackground(new Color(255, 205, 205));
			}
			exerciseOptions[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 2));
			exerciseOptions[i].setFont(new Font("SansSerif", 1, 18));
			exerciseOptions[i].setText(" " + workouts[i].name + "- " + workouts[i].muscleGroup);
			this.add(exerciseOptions[i]);
			exerciseSlots[i].setBounds(exerciseSlots[i].posX, exerciseSlots[i].posY, exerciseSlots[i].sizeX, exerciseSlots[i].sizeY);
			exerciseSlots[i].setOpaque(true);
			if(workouts[i].difficulty.equals("Beginner")){
				exerciseSlots[i].setBackground(new Color(135, 205, 135, 85));
			}
			else if(workouts[i].difficulty.equals("Experienced")){
				exerciseSlots[i].setBackground(new Color(205, 205, 135, 85));
			}
			else if(workouts[i].difficulty.equals("Expert")){
				exerciseSlots[i].setBackground(new Color(205, 135, 135, 85));
			}
			exerciseSlots[i].setBorder(BorderFactory.createLineBorder((new Color(0,0,0)), 1));
			this.add(exerciseSlots[i]);
		}
		
		for(int i = 0; i < dailySlots.length; i++){
			for(int j = 0; j <dailySlots[i].length; j++){
				this.add(dailySlots[i][j]);
			}
		}
		
		counter = 0;
		for(double i = 8; counter < timeSlots.length; i+=0.25){
			this.add(timeSlots[counter]);
			counter++;
		}
		this.add(slotsCont);
		this.add(optionsCont);
	}
}