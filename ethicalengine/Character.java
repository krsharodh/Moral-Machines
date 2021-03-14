
package ethicalengine;

/**
 * Protoype for Character in a scenario
 * 
 * @author Sharodh Keelamanakudi Ragupathi(1148618)
 */

public abstract class Character {

	public enum Gender {
		MALE, FEMALE, UNKNOWN;

		/**
		 * @return String representation of gender in lower case
		 */
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	public enum BodyType {
		AVERAGE, ATHLETIC, OVERWEIGHT, UNSPECIFIED;

		/**
		 * @return String representation of bodytype in lower case
		 */
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	public enum TypeofCharacter {
		PERSON, ANIMAL;

		/**
		 * @return String representation of type of character in lower case
		 */
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	private int age;
	private Gender gender;
	private BodyType bodyType;
	private TypeofCharacter typeOfCharacter;

	/**
	 * Creates a character using default values
	 * 
	 */
	Character() {
		this.age = 0;
		this.gender = Gender.UNKNOWN;
		this.bodyType = BodyType.UNSPECIFIED;
	}

	/**
	 * Creates a Character using a age, bodytype and gender
	 * 
	 * @param age      sets the age of a character
	 * @param gender   sets the gender of a character
	 * @param bodyType sets the bodytype of a character
	 */
	Character(int age, Gender gender, BodyType bodyType) {
		if (age > 0) {
			this.age = age;
		}
		this.gender = gender;
		this.bodyType = bodyType;
	}

	/**
	 * Creates a Character using reference of another Character
	 * 
	 * @param character instance for reference
	 */
	Character(Character character) {
		this.setAge(character.getAge());
		this.setGender(character.getGender());
		this.setBodyType(character.getBodyType());
	}

	/**
	 * gets the importance of a character
	 * 
	 * @return importance of a character
	 */
	public abstract int computeImportance();

	/**
	 * gets age of a character
	 * 
	 * @return importance of a character
	 */
	public int getAge() {
		return age;
	}

	/**
	 * sets age of a Character
	 * 
	 * @param age sets the age of a character
	 */
	public void setAge(int age) {
		if (age > 0) {
			this.age = age;
		}
	}

	/**
	 * gets Gender of a Character
	 * 
	 * @return the gender of a character
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * sets gender of a Character
	 * 
	 * @param gender sets the gender of character
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * gets Bodytype of a Character
	 * 
	 * @return bodytype of character
	 */
	public BodyType getBodyType() {
		return bodyType;
	}

	/**
	 * sets Bodytype of a character
	 * 
	 * @param bodyType set bodytype of a character
	 */
	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}

	/**
	 * gets the type of character
	 * 
	 * @return Type of Character
	 */
	public TypeofCharacter getTypeOfCharacter() {
		return typeOfCharacter;
	}

	/**
	 * sets the type of Character
	 * 
	 * @param sets type of Character
	 */
	protected void setTypeOfCharacter(TypeofCharacter typeOfCharacter) {
		this.typeOfCharacter = typeOfCharacter;
	}

}
