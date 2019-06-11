package jme.gui.descriptionwrappers;

public class ExploreDescriptionWrapper extends DescriptionWrapper{

	String text;
	
	public static ExploreDescriptionWrapper explore = new ExploreDescriptionWrapper("Explore this tile");
	
	public ExploreDescriptionWrapper(String fixedOutput) {
		this.text = fixedOutput;
	}
	
	@Override
	public String getDescription() {
		return text;
	}

}
