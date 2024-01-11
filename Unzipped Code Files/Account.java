import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Account{
	private String user, pass, gender;
	private int height, weight, age;

	public Account(){
		this("null", "null", 0, 0, 0, "");
	}
	
	public Account(String user, String pass, int height, int weight, int age, String gender){
		this.user = user;
		this.pass = pass;
		this.height = height;
		this.weight = weight;
		this.age = age;
		this.gender = gender;
	}
	
	public void setUser(String user){
		this.user = user;
	}
	
	public void setPass(String pass){
		this.pass = pass;
	}
	
	public void setGender(String gender){
		this.gender = gender;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public void setWeight(int weight){
		this.weight = weight;
	}
	
	public void setAge(int age){
		this.age = age;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getPass(){
		return pass;
	}
	
	public String getGender(){
		return gender;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public int getAge(){
		return age;
	}
}