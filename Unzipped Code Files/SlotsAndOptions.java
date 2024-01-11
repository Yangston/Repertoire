import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SlotsAndOptions extends JLabel{
	public int posX, posY, sizeX, sizeY, origX, origY, origSizeX, origSizeY, holding;
	public boolean held = false, taken = false, inUse = false;

	public SlotsAndOptions(){
		this(0, 0, false, false, false, 0, 0, 0, 0, 0, 0, -1);
	}
	
	public SlotsAndOptions(int posX, int posY, int sizeX, int sizeY){
		this(posX, posY, false, false, false, sizeX, sizeY, posX, posY, sizeX, sizeY, -1);
	}
	
	public SlotsAndOptions(int posX, int posY, boolean held, boolean taken, boolean inUse, int sizeX, int sizeY, int origX, int origY, int origSizeX, int origSizeY, int holding){
		this.posX = posX;
		this.posY = posY;
		this.held = held;
		this.taken = taken;
		this.inUse = inUse;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.origX = posX;
		this.origY = posY;
		this.origSizeX = sizeX;
		this.origSizeY = sizeY;
		this.holding = holding;
	}

}