import javax.swing.JFrame;
import java.awt.*;

public class GUI extends JFrame{
	private static final int WIDTH = 1080; 
	private static final int HEIGHT = 720; 
	
	public GUI(){
		super("Myo Kart Control");
		setup();
	}
	
	private void setup(){
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
	}
}
