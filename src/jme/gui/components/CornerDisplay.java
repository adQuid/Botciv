package jme.gui.components;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.render.NiftyImage;

public class CornerDisplay {

	public static PanelBuilder panel() {
		return new PanelBuilder("Bottom_Button_List"){{
			childLayoutVertical(); 
			valignBottom();

			panel(new PanelBuilder("Resource_Row_1") {{		
				childLayoutHorizontal(); 
				alignRight();
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/labor.png");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/labor.png");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/materials.png");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/materials.png");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/wealth.png");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/wealth.png");
				}});
			}});

			panel(new PanelBuilder("Resource_Row_2") {{		
				childLayoutHorizontal(); 
				alignRight();
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/influence.png");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/influence.png");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/education.png");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("16%");
					filename("images/ui/education.png");
				}});
				
				text(new TextBuilder("RESOURCE_PLACEHOLDER") {{
					width("*");
				}});						
			}});
			
			control(new ButtonBuilder("Button_2", "Print Button"){{
				alignCenter();
				valignBottom();
				height("50%");
				width("100%");
				interactOnClick("printstuff()");
			}});
		}};
	}
}
