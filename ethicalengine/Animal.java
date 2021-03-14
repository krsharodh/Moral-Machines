package ethicalengine;

/**
 * Protoype for Animal inherited from Character
 * 
 * @author Sharodh Keelamanakudi Ragupathi(1148618)
 */

public class Animal extends Character {

	final int IS_PET_IMPORTANCE = 2;

	private String species;

	private boolean isPet;

	public enum Species {
		CAT, BIRD, DOG;

		/**
		 * @return String representation of gender in lower case
		 */
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	final Species DEFAULT_SPECIES = Species.DOG;

	/**
	 * Creates a Animal with default values
	 */
	public Animal() {
		super();
		this.species = DEFAULT_SPECIES.toString();
		this.isPet = false;
		this.setTypeOfCharacter(TypeofCharacter.ANIMAL);
	}

	/**
	 * Creates a Animal with predefined species
	 * 
	 * @param Species used to the species of the animal
	 */
	public Animal(String species) {
		super();
		this.species = species;
		this.isPet = false;
		this.setTypeOfCharacter(TypeofCharacter.ANIMAL);
	}

	/**
	 * Creates a animal with predefined age, gender, bodytype, species, isPet
	 * 
	 * @param age      used to set the age of the animal
	 * @param gender   used to set the gender of the animal
	 * @param bodyType used to set the body bodytype of the animal
	 * @param species  used to set the species of the animal
	 * @param isPet    used to set animal as pet
	 */
	public Animal(int age, Gender gender, BodyType bodyType, String species, boolean isPet) {
		super(age, gender, bodyType);
		this.species = species;
		this.isPet = isPet;
		this.setTypeOfCharacter(TypeofCharacter.ANIMAL);
	}

	/**
	 * Create a animal with a reference of another animal
	 * 
	 * @param otherAnimal used to create animal with another animal instance
	 */
	public Animal(Animal otherAnimal) {
		super(otherAnimal);
		this.species = otherAnimal.getSpecies();
		this.isPet = otherAnimal.isPet();
		this.setTypeOfCharacter(otherAnimal.getTypeOfCharacter());
	}

	/**
	 * gets the value of species
	 * 
	 * @return Species of animal
	 */
	public String getSpecies() {
		return species;
	}

	/**
	 * sets the value of species
	 * 
	 * @param species used to set the species of the animal
	 */
	public void setSpecies(String species) {
		this.species = species;
	}

	/**
	 * gets data of pet attribute
	 * 
	 * @return returns if a animal is pet
	 */
	public boolean isPet() {
		return isPet;
	}

	/**
	 * sets value of pet attribute for an animal
	 * 
	 * @param isPet sets the animal as a pet
	 */
	public void setPet(boolean isPet) {
		this.isPet = isPet;
	}

	/**
	 * represents Animal in the form of string
	 * 
	 * @return string representation of animal
	 */
	public String toString() {
		String isPet = isPet() ? " is pet" : "";

		return String.format("%s%s", this.getSpecies(), isPet);
	}

	/**
	 * gets the importance of the Animal
	 * 
	 * @return importance of animal
	 */
	public int computeImportance() {
		return this.isPet() ? this.IS_PET_IMPORTANCE : 0;
	}
}
