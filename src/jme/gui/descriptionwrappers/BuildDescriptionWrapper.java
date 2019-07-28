package jme.gui.descriptionwrappers;

public class BuildDescriptionWrapper extends DescriptionWrapper{

	String text;
	
	public static BuildDescriptionWrapper build = new BuildDescriptionWrapper("Build a new Unit here");
	
	public BuildDescriptionWrapper(String fixedOutput) {
		this.text = fixedOutput;
	}
	
	@Override
	public String getDescription() {
		return text;
	}

}
