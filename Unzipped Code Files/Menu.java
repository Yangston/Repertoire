import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.io.*;

public class Menu extends JPanel implements ActionListener{
	public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();	
	public static int PANEL_HEIGHT = (dim.height*9)/10;
	public static int PANEL_WIDTH = (dim.width*9)/10;
	public static Timer timer;
	public static JButton buttons[] = new JButton[2], back;
	public static JTextField accDetails[] = new JTextField[4], user, pass;
	public static boolean login = false, signUp = false, accountFound = false, loginAttempt = false, signUpAttempt = false, loginSuccess = false, newUserRegistered = false, dataRegistered = false, enterAccDetails = false, homeScreen = true;
	public static Account accounts[] = new Account[100];
	public static int accountCounter = 0, accountNum = 0, min, mid, max;
	public static String username = "", currentLine = "", allUserPassData = "", allScheduleData = "", allAccDetailsData = "";
	public static String[] tempData = new String[1500], allUserPassDataArr, allScheduleDataArr, allAccDetailsDataArr;
	private static ImageIcon menuIcons[] = new ImageIcon[2];
	private static Point mousePos;
	private static FileWriter userAndPassWrite, accountDetailsWrite, scheduleWrite;
	private static Scanner userAndPassScan, accountDetailsScan, scheduleScan;

    public Menu() {
		timer = new Timer(10, this);	
		mousePos = MouseInfo.getPointerInfo().getLocation();
		
		menuIcons[0] = new ImageIcon("Login.jpg");	
		menuIcons[1] = new ImageIcon("SignUp.jpg");	

		for(int i = 0; i < menuIcons.length; i++){	
			Image img = menuIcons[i].getImage();
			Image newImg = img.getScaledInstance(200, 90, java.awt.Image.SCALE_SMOOTH);
			menuIcons[i] = new ImageIcon(newImg); 
			buttons[i] = new JButton(menuIcons[i]);
		}

		this.setLayout(null);
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i].setBounds(i*350+400,300,200,90);
		}
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i].addActionListener(this);
			buttons[i].setOpaque(false);
			buttons[i].setContentAreaFilled(false);
			buttons[i].setBorderPainted(true);
			buttons[i].setFocusPainted(false);
			this.add(buttons[i]);
		}
		
		back = new JButton("Back");
		back.setBounds(900, 500, 200, 90);
		back.addActionListener(this);
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		back.setBorderPainted(true);
		back.setFocusPainted(false);
		this.add(back);
	
		user = new JTextField();
		pass = new JTextField();		
		
		user.setBounds((PANEL_WIDTH/2)-150, (PANEL_HEIGHT/2)-100, 300, 60);
		pass.setBounds((PANEL_WIDTH/2)-150, (PANEL_HEIGHT/2)+20, 300, 60);
		
		user.addActionListener(this);
		pass.addActionListener(this);
		
		this.add(user);
		this.add(pass);
		
		for(int i = 0; i < accDetails.length; i++){
			accDetails[i] = new JTextField();
			accDetails[i].setBounds((PANEL_WIDTH/2)-150, (PANEL_HEIGHT/5)+(i*125), 300, 60);
			accDetails[i].addActionListener(this);
			this.add(accDetails[i]);
		}
		
		reset();
    }
	
	public void run(){			
		timer.start();	
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(205, 255, 255));
		g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		g.setColor(new Color(0, 0, 0));
		g.setFont(new Font("SansSerif: ", 1, 24));
		if(loginAttempt){
			if(!accountFound){
				g.drawString("Incorrect username or password", 35, 260);
			}
			else{
				g.drawString("Success!", 35, 260);	
			}
		}
		if(signUpAttempt){
			if(!accountFound){
				g.drawString("Works!", 35, 260);
			}
			else{
				g.drawString("Sorry, username is taken.", 35, 260);	
			}
		}
		
		g.setFont(new Font("SansSerif: ", 1, 20));
		if(signUp || login){
			g.drawString("Username", 640, 280);	
			g.drawString("Password", 640, 400);	
		}
		if(enterAccDetails){
			g.drawString("Height (kg)", 640, 140);	
			g.drawString("Weight (m)", 640, 260);	
			g.drawString("Age", 640, 390);	
			g.drawString("Gender", 640, 520);				
		}
		if(homeScreen) {
			g.setFont(new Font("SansSerif: ", 1, 60));
			g.drawString("Workout Planner", 440, 120);
		}
	}
	
	public void actionPerformed(ActionEvent ev){	
		mousePos = MouseInfo.getPointerInfo().getLocation();
		buttonChecker(ev);
		textInputChecker(ev);
		repaint();
	}
	
	public static void buttonChecker(ActionEvent ev){
		if(ev.getSource() == buttons[0]){//Login
			login = true;
			homeScreen = false;
			for(int i = 0; i < buttons.length; i++){
				buttons[i].setVisible(false);
			}
			user.setVisible(true);
			pass.setVisible(true);
			back.setVisible(true);
		}
		else if(ev.getSource() == buttons[1]){//Sign up
			signUp = true;
			homeScreen = false;
			for(int i = 0; i < buttons.length; i++){
				buttons[i].setVisible(false);
			}
			user.setVisible(true);
			pass.setVisible(true);
			back.setVisible(true);
		}
		
		if(ev.getSource() == back){
			reset();
		}
	}
	
	public static void textInputChecker(ActionEvent ev){
		if(ev.getSource() == pass){
			if(login){
				login();
			}
			else if(signUp){
				signUp();
				if(signUpAttempt && !accountFound){
					accountSetup();
				}
			}
		}
		if(ev.getSource() == accDetails[3]){
			accountDetails();
			reset();
		}
	}
	
	public static void reset(){
		homeScreen = true;
		user.setVisible(false);
		pass.setVisible(false);
		back.setVisible(false);
		user.setText("");
		pass.setText("");
		for(int i = 0; i < buttons.length; i++){
			buttons[i].setVisible(true);
		}
		login = false;
		signUp = false;
		loginAttempt = false;
		signUpAttempt = false;
		
		for(int i = 0; i < accDetails.length; i++){
			accDetails[i].setVisible(false);
		}
	}
	
	public static void accountSetup(){
		enterAccDetails = true;
		signUp = false;
		user.setVisible(false);
		pass.setVisible(false);
		back.setVisible(false);
		for(int i = 0; i < accDetails.length; i++){
			accDetails[i].setVisible(true);
		}
	}
	
	public static void login(){		
		loginAttempt = true;
		try {
			userAndPassScan = new Scanner(new File("UserAndPass.txt"));
		}
		catch (Exception error) {
			System.out.println("An error occurred:" + error);
			error.printStackTrace();
		}
		
		accountFound = false;
		currentLine = "";
		allUserPassData = "";
		while(userAndPassScan.hasNextLine()){
			currentLine = userAndPassScan.nextLine();
			allUserPassData += currentLine + "%";
		}
		allUserPassDataArr = allUserPassData.split("%");
		userAndPassScan.close();
		min = 0;
		max = allUserPassDataArr.length - 1;
		while(min <= max){
			mid = (min + max)/2;
			String userPass[] = allUserPassDataArr[mid].split(" ");	
			if(user.getText().compareTo(userPass[0]) < 0){
				max = mid -1;
			}
			else if(user.getText().compareTo(userPass[0]) > 0){
				min = mid + 1;
			}
			else if((user.getText() + " " + pass.getText()).equals(allUserPassDataArr[mid])){
				username = user.getText();
				accountFound = true;
				loginSuccess = true;
				break;
			}
		}

		userAndPassScan.close();
	}
	
	public static void signUp(){
		signUpAttempt = true;
		try {
			userAndPassScan = new Scanner(new File("UserAndPass.txt"));
			scheduleScan = new Scanner(new File("ScheduleData.txt"));
			accountDetailsScan = new Scanner(new File("AccountDetails.txt"));
		}
		catch (Exception error) {
			System.out.println("An error occurred:" + error);
			error.printStackTrace();
		}
		
		accountCounter = 0;
		accountFound = false;
		currentLine = "";
		allUserPassData = "";
		allScheduleData = "";
		allAccDetailsData = "";
		while(userAndPassScan.hasNextLine()){
			currentLine = userAndPassScan.nextLine();
			allUserPassData += currentLine + "%";
			String userPass[] = currentLine.split(" ");
			if((user.getText()).equals(userPass[0])){
				accountFound = true;
				newUserRegistered = false;
				break;
			}
			currentLine = scheduleScan.nextLine();
			allScheduleData += currentLine + "%";
			currentLine = accountDetailsScan.nextLine();
			allAccDetailsData += currentLine + "%";
			accountCounter++;
		}
		allUserPassDataArr = allUserPassData.split("%");
		userAndPassScan.close();
		allScheduleDataArr = allScheduleData.split("%");
		scheduleScan.close();
		allAccDetailsDataArr = allAccDetailsData.split("%");
		accountDetailsScan.close();

		accounts[accountCounter] = new Account();
		accounts[accountCounter].setUser(user.getText());
		accounts[accountCounter].setPass(pass.getText());
		
		if(!accountFound){
			try {
				userAndPassWrite = new FileWriter("UserAndPass.txt", false);
				BufferedWriter bUAPW = new BufferedWriter(userAndPassWrite);
				scheduleWrite = new FileWriter("ScheduleData.txt", false);
				BufferedWriter bSW = new BufferedWriter(scheduleWrite);
				accountDetailsWrite = new FileWriter("AccountDetails.txt", false);
				BufferedWriter bADW = new BufferedWriter(accountDetailsWrite);
		
				try {
					for(int i = 0; i < allUserPassDataArr.length; i++){
						tempData = allUserPassDataArr[i].split(" ");
						if(!newUserRegistered){
							if(accounts[accountCounter].getUser().compareTo(tempData[0]) < 0){
								bSW.write(accounts[accountCounter].getUser());
								bSW.newLine();
								bADW.write(accounts[accountCounter].getUser());
								bADW.newLine();
								bUAPW.write(accounts[accountCounter].getUser() + " " + accounts[accountCounter].getPass());
								bUAPW.newLine();
								accountNum = i;
								i--;
								newUserRegistered = true;
							}
							else{
								bSW.write(allScheduleDataArr[i]);
								bSW.newLine();
								bADW.write(allAccDetailsDataArr[i]);
								bADW.newLine();
								bUAPW.write(allUserPassDataArr[i]);
								bUAPW.newLine();
							}
						}		
						else{
							bSW.write(allScheduleDataArr[i]);
							bSW.newLine();
							bADW.write(allAccDetailsDataArr[i]);
							bADW.newLine();
							bUAPW.write(allUserPassDataArr[i]);
							bUAPW.newLine();
						}
					}
					if(!newUserRegistered){
						accountNum = allUserPassDataArr.length + 1;
						bSW.write(accounts[accountCounter].getUser());
						bSW.newLine();
						bADW.write(accounts[accountCounter].getUser());
						bADW.newLine();
						bUAPW.write(accounts[accountCounter].getUser() + " " + accounts[accountCounter].getPass());
						bUAPW.newLine();
					}
				bUAPW.close();
				bSW.close();
				bADW.close();
				}
				catch (IOException error) {
					System.out.println("An error occurred:" + error);
					error.printStackTrace();
				}
			}
			catch (IOException error) {
				System.out.println("An error occurred:" + error);
				error.printStackTrace();
			}
		}
	}
	
	public static void accountDetails(){
		try {
			accountDetailsScan = new Scanner(new File("AccountDetails.txt"));
		}
		catch (Exception error) {
			System.out.println("An error occurred:" + error);
			error.printStackTrace();
		}
		
		accounts[accountCounter].setHeight(Integer.parseInt(accDetails[0].getText()));
		accounts[accountCounter].setWeight(Integer.parseInt(accDetails[1].getText()));
		accounts[accountCounter].setAge(Integer.parseInt(accDetails[2].getText()));
		accounts[accountCounter].setGender(accDetails[3].getText());
		
		try {
			accountDetailsWrite = new FileWriter("AccountDetails.txt", false);
			BufferedWriter bADW = new BufferedWriter(accountDetailsWrite);
			dataRegistered = false;
			for(int i = 0; i < allAccDetailsDataArr.length; i++){
				if(i == accountNum && !dataRegistered){
					bADW.write(accounts[accountCounter].getUser() + ", " + accounts[accountCounter].getHeight() + ", " + accounts[accountCounter].getWeight() + ", " + accounts[accountCounter].getAge() + ", " + accounts[accountCounter].getGender());
					bADW.newLine();
					i--;
					dataRegistered = true;
				}
				else{
					bADW.write(allAccDetailsDataArr[i]);
					bADW.newLine();
				}
			}
			bADW.close();
		} 
		catch (IOException e) {
			System.out.println("An error occurred:" + e);
			e.printStackTrace();
		}
		accountDetailsScan.close();
		enterAccDetails = false;
	}
}