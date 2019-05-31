package jme.gui.components;

import de.lessvoid.nifty.builder.HoverEffectBuilder;
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
					width("15%");
					filename("images/ui/labor.png");
					interactOnMouseOver("updateDescription(labor)");
				}});
				text(new TextBuilder("laborText") {{
					height("100%");
					width("18%");
					text("1234");
					font("Interface/Fonts/Default.fnt");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("15%");
					filename("images/ui/materials.png");
					interactOnMouseOver("updateDescription(materials)");
				}});
				text(new TextBuilder("laborText") {{
					height("100%");
					width("18%");
					text("1234");
					font("Interface/Fonts/Default.fnt");
					
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("15%");
					filename("images/ui/wealth.png");
					interactOnMouseOver("updateDescription(wealth)");
				}});
				text(new TextBuilder("laborText") {{
					height("100%");
					width("18%");
					text("1234");
					font("Interface/Fonts/Default.fnt");
				}});
			}});

			panel(new PanelBuilder("Resource_Row_2") {{		
				childLayoutHorizontal(); 
				alignRight();
				image(new ImageBuilder("Image") {{
					height("100%");
					width("15%");
					filename("images/ui/influence.png");
					interactOnMouseOver("updateDescription(influence)");
				}});
				text(new TextBuilder("laborText") {{
					height("100%");
					width("18%");
					text("1234");
					font("Interface/Fonts/Default.fnt");
				}});
				image(new ImageBuilder("Image") {{
					height("100%");
					width("15%");
					filename("images/ui/education.png");
					interactOnMouseOver("updateDescription(education)");
				}});
				text(new TextBuilder("laborText") {{
					height("100%");
					width("18%");
					text("1234");
					font("Interface/Fonts/Default.fnt");
				}});
				
				text(new TextBuilder("RESOURCE_PLACEHOLDER") {{
					width("*");
				}});						
			}});
			
			panel(new PanelBuilder("Corner_Buttons") {{		
				childLayoutVertical(); 
				alignRight();
				height("50%");

				panel(new PanelBuilder("Option_Buttons") {{		
					childLayoutHorizontal(); 
					alignRight();
					height("50%");
					control(new ButtonBuilder("Empire_Options_Button", "Empire Options"){{
						alignCenter();
						valignBottom();
						height("100%");
						width("50%");
					}});
					control(new ButtonBuilder("Game_Options_Button", "Game Options"){{
						alignCenter();
						valignBottom();
						height("100%");
						width("50%");
					}});

				}});
				
				panel(new PanelBuilder("End_Turn_Buttons") {{		
					childLayoutHorizontal(); 
					alignRight();
					height("50%");
					control(new ButtonBuilder("Clear_Turn_Button", "Clear Turn"){{
						alignCenter();
						valignBottom();
						height("100%");
						width("50%");
						interactOnClick("clearTurn()");
					}});
					control(new ButtonBuilder("Commit_Turb_Button", "Commit Turn"){{
						alignCenter();
						valignBottom();
						height("100%");
						width("50%");
						interactOnClick("commitTurn()");
					}});
				}});
			}});
		}};
	}
}
