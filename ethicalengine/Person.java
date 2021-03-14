package ethicalengine;

/**
 * Protoype for Person inherited from Character
 * 
 * @author Sharodh Keelamanakudi Ragupathi(1148618)
 */

public class Person extends Character {

	public enum Profession {
		DOCTOR, CEO, CRIMINAL, PRESIDENT, POLICE, HOMELESS, UNEMPLOYED, UNKNOWN, NONE;

		final int PROFESSION_IMPORTANCE_PRESIDENT = 7;
		final int PROFESSION_IMPORTANCE_POLICE = 6;
		final int PROFESSION_IMPORTANCE_DOCTOR = 5;
		final int PROFESSION_IMPORTANCE_CEO = 4;
		final int PROFESSION_IMPORTANCE_CRIMINAL = -1;
		final int PROFESSION_IMPORTANCE_HOMELESS = 1;

		/**
		 * @return String representation of gender in lower case
		 */
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}

		/**
		 * returns the importance specified to the profession
		 * 
		 * @return importance of person
		 */
		private int getImportance() {
			if (this == Profession.PRESIDENT) {
				return PROFESSION_IMPORTANCE_PRESIDENT;
			} else if (this == Profession.POLICE) {
				return PROFESSION_IMPORTANCE_POLICE;
			} else if (this == Profession.DOCTOR) {
				return PROFESSION_IMPORTANCE_DOCTOR;
			} else if (this == Profession.CEO) {
				return PROFESSION_IMPORTANCE_CEO;
			} else if (this == Profession.CRIMINAL) {
				return PROFESSION_IMPORTANCE_CRIMINAL;
			} else if (this == Profession.HOMELESS) {
				return PROFESSION_IMPORTANCE_HOMELESS;
			} else {
				return 0;
			}
		}

	}

	public enum AgeCategory {
		BABY, CHILD, ADULT, SENIOR;

		final int AGE_IMPORTANCE_SENIOR = 1;
		final int AGE_IMPORTANCE_ADULT = 2;
		final int AGE_IMPORTANCE_CHILD = 3;
		final int AGE_IMPORTANCE_BABY = 4;

		/**
		 * returns the importance specified to the age
		 * 
		 * @return importance for age
		 */
		public int getImportance() {
			if (this == AgeCategory.SENIOR) {
				return AGE_IMPORTANCE_SENIOR;
			} else if (this == AgeCategory.ADULT) {
				return AGE_IMPORTANCE_ADULT;
			} else if (this == AgeCategory.CHILD) {
				return AGE_IMPORTANCE_CHILD;
			} else {
				return AGE_IMPORTANCE_BABY;
			}
		}

		/**
		 * @return String representation of gender in lower case
		 */
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	final int IS_PREGNANT_IMPORTANCE = 3;
	final int IS_YOU_IMPORTANCE = 5;

	final static int MIN_SENIOR_AGE = 69;
	final static int MIN_ADULT_AGE = 17;
	final static int MIN_CHILD_AGE = 5;

	private Profession profession;

	private boolean isPregnant;

	private boolean isYou;

	/**
	 * Creates a Person with default values
	 */
	public Person() {
		super();
		this.setProfession(Profession.UNKNOWN);
		this.setPregnant(false);
		this.setAsYou(false);
		this.setTypeOfCharacter(TypeofCharacter.PERSON);
	}

	/**
	 * Creates a person
	 * 
	 * @param age      Person's age
	 * @param gender   Person's Gender
	 * @param bodyType Person's Body type
	 */
	public Person(int age, Gender gender, BodyType bodyType) {
		super(age, gender, bodyType);
		this.setProfession(Profession.UNKNOWN);
		this.setPregnant(false);
		this.setAsYou(false);
		this.setTypeOfCharacter(TypeofCharacter.PERSON);
	}

	/**
	 * Creates a Person with predefined age, profession, gender, bodytype,
	 * isPregnant
	 * 
	 * @param age        Person's age
	 * @param profession Person's Profession
	 * @param gender     Person's Gender
	 * @param bodyType   Person's Body type
	 * @param isPregnant if person is preganant
	 */
	public Person(int age, Profession profession, Gender gender, BodyType bodyType, boolean isPregnant) {
		super(age, gender, bodyType);
		this.setProfession(profession);
		this.setPregnant(isPregnant);
		this.setAsYou(false);
		this.setTypeOfCharacter(TypeofCharacter.PERSON);
	}

	/**
	 * Creates a Person with predefined age, profession, gender, bodytype,
	 * isPregnant, isYou
	 * 
	 * @param age        Person's age
	 * @param profession Person's Profession
	 * @param gender     Person's Gender
	 * @param bodyType   Person's Body type
	 * @param isPregnant if person is preganant
	 * @param isYou      if person is you
	 */
	public Person(int age, Profession profession, Gender gender, BodyType bodyType, boolean isPregnant, boolean isYou) {
		super(age, gender, bodyType);
		this.setProfession(profession);
		this.setPregnant(isPregnant);
		this.setAsYou(isYou);
		this.setTypeOfCharacter(TypeofCharacter.PERSON);
	}

	/**
	 * Creates a Person with reference of another person
	 * 
	 * @param otherPerson reference object of a person
	 */
	public Person(Person otherPerson) {
		super(otherPerson);
		this.setProfession(otherPerson.getProfession());
		this.setPregnant(otherPerson.isPregnant());
		this.setAsYou(otherPerson.isYou());
		this.setTypeOfCharacter(TypeofCharacter.PERSON);
	}

	/**
	 * gets the profession of the Person
	 * 
	 * @return Profession of person
	 */
	public Profession getProfession() {
		return profession;
	}

	/**
	 * Sets the Profession of a Person
	 * 
	 * @param profession Person's profession
	 */
	public void setProfession(Profession profession) {
		this.profession = this.getAgeCategory() == AgeCategory.ADULT ? profession : Profession.NONE;
	}

	/**
	 * Gets the pregnant character for a Person
	 * 
	 * @return if person is pregnant
	 */
	public boolean isPregnant() {
		return isPregnant;
	}

	/**
	 * Sets the pregnant character for a Person
	 * 
	 * @param pregnant Person's pregnant status
	 */
	public void setPregnant(boolean pregnant) {
		this.isPregnant = this.getGender() == Gender.FEMALE ? pregnant : false;
	}

	/**
	 * Gets the isYou character for a Person
	 * 
	 * @return if is a person is you
	 */
	public boolean isYou() {
		return isYou;
	}

	/**
	 * Sets the pregnant character for a Person
	 * 
	 * @param isYou Person as you
	 */
	public void setAsYou(boolean isYou) {
		this.isYou = isYou;
	}

	/**
	 * Gets the string representation of the object
	 *
	 * @return String representation of person
	 */
	public String toString() {
		String you = isYou() ? "you " : "";
		String profession = getProfession() == Profession.NONE ? "" : getProfession() + " ";
		String pregnant = this.isPregnant() ? " pregnant" : "";

		return String.format("%s%s %s %s%s%s", you, this.getBodyType(), this.getAgeCategory(), profession,
				this.getGender(), pregnant);
	}

	/**
	 * gets the importance of the Animal
	 * 
	 * @return importance of a person
	 */
	public int computeImportance() {
		int importanceFactor = 0;

		importanceFactor += this.getAgeCategory().getImportance();
		importanceFactor += this.getProfession().getImportance();
		importanceFactor += this.isPregnant ? this.IS_PREGNANT_IMPORTANCE : 0;
		importanceFactor += this.isYou ? this.IS_YOU_IMPORTANCE : 0;

		return importanceFactor;
	}

	/**
	 * gets the ageCategory of the person
	 * 
	 * @return AgeCategory of a person
	 */
	public AgeCategory getAgeCategory() {
		return AgeCategoryUtil(this.getAge());
	}

	/**
	 * gets the ageCategory for an age
	 * 
	 * @return AgeCategory of a person
	 */
	public static AgeCategory AgeCategoryUtil(int age) {
		AgeCategory ageCategory = AgeCategory.BABY;
		if (age >= MIN_SENIOR_AGE) {
			ageCategory = AgeCategory.SENIOR;
		} else if (age >= MIN_ADULT_AGE) {
			ageCategory = AgeCategory.ADULT;
		} else if (age >= MIN_CHILD_AGE) {
			ageCategory = AgeCategory.CHILD;
		}
		return ageCategory;
	}

}
