package jme.gui.descriptionwrappers;

public class ClaimDescriptionWrapper extends DescriptionWrapper{

	String text;
	
	public static ClaimDescriptionWrapper claim = new ClaimDescriptionWrapper("Claim this tile at the cost of 5 influence");
	
	public ClaimDescriptionWrapper(String fixedOutput) {
		this.text = fixedOutput;
	}
	
	@Override
	public String getDescription() {
		return text;
	}

}
