package descriptionwrappers;

public class ResourceDescriptionWrapper extends DescriptionWrapper{

	String text;
	
	public static ResourceDescriptionWrapper labor = new ResourceDescriptionWrapper("Labor represents the amount of work your civilization can get done at a time. This can be through people, animals, or machienes. Labor left over at the end of the turn is used by the people for their own needs.");
	public static ResourceDescriptionWrapper materials = new ResourceDescriptionWrapper("Materials are the aggregate of all common resources.");
	public static ResourceDescriptionWrapper influence = new ResourceDescriptionWrapper("Influence is a measure of the stregth of your leadership and beurocracy. A power structure with more influcence can convince more people to act with the same will, and sees tasks done more efficiently. One with little to no influence will find any order ignored, and risks collapse. No single government can store more than 1000 influence.");
	public static ResourceDescriptionWrapper wealth = new ResourceDescriptionWrapper("Money, jewels, toasters, anything you wouldn't eat or build a house with is wealth. Wealth won't directly help you do much, but it can often be traded for things that can. Advanced societies tend to need to pay for more things as people have higher standards of what the world owes them.");
	public static ResourceDescriptionWrapper education = new ResourceDescriptionWrapper("Education is the brains to Labor's brawn, and can also be enhanced by technology. Any leftovers not used to run your society will be used to discover new science and technology, but if the upkeep of what you already know is too much, it is possible to decay into a more primative state.");
	
	public ResourceDescriptionWrapper(String fixedOutput) {
		this.text = fixedOutput;
	}

	@Override
	public String getDescription() {
		return text;
	}
	
}
