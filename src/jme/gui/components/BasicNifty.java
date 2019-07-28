/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jme.gui.components;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import jme.gui.ButtonActions;
import jme.gui.MainUI;

/**
 *
 * @author b
 */
public class BasicNifty extends BaseAppState implements ScreenController{
    
	public static BasicNifty self = new BasicNifty();
	
	public static NiftyJmeDisplay niftyDisplay;
	
    @Override
    protected void initialize(Application app) {
        //It is technically safe to do all initialization and cleanup in the
        //onEnable()/onDisable() methods. Choosing to use initialize() and
        //cleanup() for this is a matter of performance specifics for the
        //implementor.
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
    }

    @Override
    protected void cleanup(Application app) {
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
    }

    //onEnable()/onDisable() can be used for managing things that should
    //only exist while the state is enabled. Prime examples would be scene
    //graph attachment or input listener attachment.
    @Override
    protected void onEnable() {

    	niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
    			getApplication().getAssetManager(),
    			getApplication().getInputManager(),
    			getApplication().getAudioRenderer(),
    			getApplication().getGuiViewPort());
    	
    	Nifty nifty = niftyDisplay.getNifty();
    	getApplication().getGuiViewPort().addProcessor(niftyDisplay);

    	nifty.loadStyleFile("nifty-default-styles.xml");
    	nifty.loadControlFile("nifty-default-controls.xml");
    	
    	nifty.addScreen("Main_Screen", new ScreenBuilder("Hello Nifty Screen"){{
    		controller(ButtonActions.actions); //whacky controller nonsense
    		    		
    		layer(EndHoverActionsLayer.getLayer());
    		
    		layer(new LayerBuilder("Base_Layer") {{
    			childLayoutCenter();

    			panel(new PanelBuilder("Main_Panel") {{
    				childLayoutHorizontal();
    				alignRight();
    				valignBottom();
    				
    				panel(new PanelBuilder("Bottom_Button_List"){{
    					childLayoutVertical(); 
        				valignBottom();

        				text(new TextBuilder("MAP_PLACEHOLDER") {{
        					height("*");        					
        				}});
        				
        				panel(new PanelBuilder("") {{
        					childLayoutVertical(); 
        					height("5%");
        					backgroundColor("#777f");
        					panel(DescriptionDisplay.panel());        						
        				}});
        				
        				panel(new PanelBuilder(BasicBottomPanels.BOTTOM_BUTTON_LABEL) {{
        					childLayoutVertical(); 
        					height("20%");
        					width("100%");
        					backgroundColor("#c92f");
            				panel(BasicBottomPanels.onYourTurn());
        				}});

    				}});
    				
        			panel(new PanelBuilder("Right_Button_List") {{
        				childLayoutVertical(); 
        				alignRight();
        				height("100%");
        				width("20%");
    					backgroundColor("#850f");
        				
        				panel(new PanelBuilder("Right_Panel_Holder") {{
        					childLayoutVertical(); 
        					height("80%");
        					width("100%");
        					panel(RightPanel.defaultRightPanel());
        				}});    
        				
        				panel(new PanelBuilder("CornerDisplay") {{
        					childLayoutVertical();
        					height("20%");
        					panel(CornerDisplay.panel());
        				}});
        			}});
    			}});
    		}});
    		
    	}}.build(nifty));

    	nifty.gotoScreen("Main_Screen"); // start the screen
    }

    public Screen getCurrentScreen() {
    	return niftyDisplay.getNifty().getCurrentScreen();
    }
    
    @Override
    protected void onDisable() {
        //Called when the state was previously enabled but is now disabled
        //either because setEnabled(false) was called or the state is being
        //cleaned up.
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void bind(Nifty arg0, Screen arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}
	
	public void removeChildren(Element element) {
		for(Element current: element.getChildren()) {
			current.markForRemoval();
		}
	}
	
}