import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Workouts{
	public String name, muscleGroup, difficulty, description;
	public int num;

	public Workouts(){
		this("", "", "", "", 0);
	}
	
	public Workouts(String name, String muscleGroup, String difficulty, String description, int num){
		this.name = name;
		this.muscleGroup = muscleGroup;
		this.difficulty = difficulty;
		this.description = description;
		this.num = num;
	}
}