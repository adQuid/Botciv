package jme.gui.descriptionwrappers;

public class GenericDescriptionWrapper extends DescriptionWrapper{

	String message;
	
	public GenericDescriptionWrapper(String message) {
		this.message = message;
	}
	
	@Override
	public String getDescription() {
		return message;
	}

}
