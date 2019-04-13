package gui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import layout.TableLayout;
import map.Coordinate;
import map.MainUIMapDisplay;
import map.World;

public class MainUI {

	public static int visionDistance = 5;
	public static JFrame GUI = new JFrame("Botciv");
	public static JPanel displayPanel = new JPanel();
	
	//should this be here?
	public static World world = new World();
	
	public static void setupGUI() {
	
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(new KeyEventDispatcher() {
		      @Override
		      public boolean dispatchKeyEvent(KeyEvent e) {
		    	  	if(e.getID() != KeyEvent.KEY_PRESSED) {
		    	  		return false;
		    	  	}
		    	  	if(e.getKeyCode() == KeyEvent.VK_UP) {
		    	  		MainUIMapDisplay.focus = MainUIMapDisplay.focus.up();
		    	  		MainUIMapDisplay.repaintDisplay();
					}
					if(e.getKeyCode() == KeyEvent.VK_DOWN) {
						MainUIMapDisplay.focus = MainUIMapDisplay.focus.down();
						MainUIMapDisplay.repaintDisplay();
					}
					if(e.getKeyCode() == KeyEvent.VK_LEFT) {
						MainUIMapDisplay.focus = MainUIMapDisplay.focus.left();
						MainUIMapDisplay.repaintDisplay();
					}
					if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						MainUIMapDisplay.focus = MainUIMapDisplay.focus.right();
						MainUIMapDisplay.repaintDisplay();
					}
		        return false;
		      }
		});
		
		GUI.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(arg0.getWheelRotation() > 0) {
					visionDistance++;
				}else if(visionDistance > 2){
					visionDistance--;
				}
				MainUIMapDisplay.repaintDisplay();
			}			
		});
		
		
		
		//resize on dragging the window
		GUI.addComponentListener(new ComponentAdapter( ) {
			public void componentResized(ComponentEvent ev) {
			  MainUIMapDisplay.resizeDisplay();
			}
		});
		
		MainUIMapDisplay.paintDisplay();
		
		double[][] size = {{0.8,0.2},{0.75,0.25}};
		GUI.setLayout(new TableLayout(size));
		GUI.add(displayPanel,"0,0");
		GUI.add(new JButton("test"),"1,0");
		GUI.add(new JButton("test"),"0,1");
		GUI.add(new JButton("test"),"1,1");
		
		GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		GUI.pack();
		GUI.setVisible(true);
	}
	
}
