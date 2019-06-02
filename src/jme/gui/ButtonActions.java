package jme.gui;

import java.util.HashMap;
import java.util.Map;

import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import descriptionwrappers.DescriptionWrapper;
import descriptionwrappers.ResourceDescriptionWrapper;
import jme.gui.components.DescriptionDisplay;

public class ButtonActions implements ScreenController{

	public static ButtonActions actions = new ButtonActions();
	
	public Map<String,DescriptionWrapper> wrappers = new HashMap<String,DescriptionWrapper>();
	
	{
		wrappers.put("labor", ResourceDescriptionWrapper.labor);
		wrappers.put("materials", ResourceDescriptionWrapper.materials);
		wrappers.put("wealth", ResourceDescriptionWrapper.wealth);
		wrappers.put("influence", ResourceDescriptionWrapper.influence);
		wrappers.put("education", ResourceDescriptionWrapper.education);
	}
	
	public void printstuff() {
		System.out.println("is this how we do this?");
	}

	public void updateDescription(String key) {
		String text = wrappers.get(key).getDescription();
		
		Element elementToFill = MainUI.nifty.getCurrentScreen().findElementById(DescriptionDisplay.descriptionID);
	    elementToFill.getRenderer(TextRenderer.class).setText(text);
	}
	
	public void clearDescription() {
		Element elementToFill = MainUI.nifty.getCurrentScreen().findElementById(DescriptionDisplay.descriptionID);
	    elementToFill.getRenderer(TextRenderer.class).setText(MainUI.getGame().getTurnName());
	}
	
	public void clearTurn() {
		MainUI.clearTurn();
	}
	
	public void commitTurn() {
		MainUI.commitTurn();
	}
	
	@Override
	public void bind(Nifty arg0, Screen arg1) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub		
	}	

}
