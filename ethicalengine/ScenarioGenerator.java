package ethicalengine;

/**
 * Prototype of Scenario Generator. Generates random attributes for the application
 * 
 * @author Sharodh Keelamanakudi Ragupathi(1148618)
 */

import java.util.Random;

import ethicalengine.Animal.Species;
import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Person.AgeCategory;
import ethicalengine.Person.Profession;

public class ScenarioGenerator {

	final int DEFAULT_MIN_COUNT = 1;
	final int DEFAULT_MAX_COUNT = 5;

	final int MAX_AGE = 101;

	private int passengerCountMinimum;

	private int passengerCountMaximum;

	private int pedestrianCountMinimum;

	private int pedestrianCountMaximum;

	private Random random = new Random();

	/**
	 * gets the minimum passenger count
	 * 
	 * @return min passenger count
	 */
	public int getPassengerCountMin() {
		return passengerCountMinimum;
	}

	/**
	 * sets the minimum passenger count
	 * 
	 * @param min sets min passenger count
	 */
	public void setPassengerCountMin(int min) {
		this.passengerCountMinimum = min;
	}

	/**
	 * get the maximum passenger count
	 * 
	 * @return max passenger count
	 */
	public int getPassengerCountMax() {
		return passengerCountMaximum;
	}

	/**
	 * sets maximum passenger count
	 * 
	 * @param max sets max passenger count
	 */
	public void setPassengerCountMax(int max) {
		this.passengerCountMaximum = max;
	}

	/**
	 * gets the minimum pedestrian count
	 * 
	 * @return min pedestrian count
	 */
	public int getPedestrianCountMin() {
		return pedestrianCountMinimum;
	}

	/**
	 * sets the minimum pedestrian count
	 * 
	 * @param min sets min pedestrian count
	 */
	public void setPedestrianCountMin(int min) {
		this.pedestrianCountMinimum = min;
	}

	/**
	 * gets the maximum pedestrian count
	 * 
	 * @return max pedestrian count
	 */
	public int getPedestrianCountMax() {
		return pedestrianCountMaximum;
	}

	/**
	 * sets the maximum pedestrian count
	 * 
	 * @param max sets max pedestrian count
	 */
	public void setPedestrianCountMax(int max) {
		this.pedestrianCountMaximum = max;
	}

	/**
	 * sets the default threshold values
	 */
	private void setDefaultThresholdValues() {
		this.setPassengerCountMax(this.DEFAULT_MAX_COUNT);
		this.setPassengerCountMin(this.DEFAULT_MIN_COUNT);
		this.setPedestrianCountMax(this.DEFAULT_MAX_COUNT);
		this.setPedestrianCountMin(this.DEFAULT_MIN_COUNT);
	}

	/**
	 * create scenario generator object with default values
	 */
	public ScenarioGenerator() {
		setDefaultThresholdValues();
	}

	/**
	 * creates scenario generator object with predefined seed
	 * 
	 * @param seed random generator seed
	 */
	public ScenarioGenerator(long seed) {
		this.random.setSeed(seed);
		setDefaultThresholdValues();
	}

	/**
	 * creates scneario generator object with predefined seed, min passenger count,
	 * max passenger count, min pedestrian count, max pedestrian count
	 * 
	 * @param seed                   random generator seed
	 * @param passengerCountMinimum  min passenger count
	 * @param passengerCountMaximum  max passenger count
	 * @param pedestrianCountMinimum min pedestrian count
	 * @param pedestrianCountMaximum max pedestrian count
	 */
	public ScenarioGenerator(long seed, int passengerCountMinimum, int passengerCountMaximum,
			int pedestrianCountMinimum, int pedestrianCountMaximum) {
		this.random.setSeed(seed);
		this.setPassengerCountMax(passengerCountMaximum);
		this.setPassengerCountMin(passengerCountMinimum);
		this.setPedestrianCountMax(pedestrianCountMaximum);
		this.setPedestrianCountMin(pedestrianCountMinimum);
	}

	/**
	 * gets random age
	 * 
	 * @return randome age
	 */
	private int getRandomAge() {
		return random.nextInt(this.MAX_AGE);
	}

	/**
	 * gets the random bodytype
	 * 
	 * @return random Bodytype
	 */
	private BodyType getRandomBodytype() {
		return BodyType.values()[random.nextInt(BodyType.values().length)];
	}

	/**
	 * gets random gender
	 * 
	 * @return random Gender
	 */
	private Gender getRandomGender() {
		return Gender.values()[random.nextInt(Gender.values().length)];
	}

	/**
	 * gets a random species
	 * 
	 * @return random Species
	 */
	private String getRandomSpecies() {
		return Species.values()[random.nextInt(Species.values().length)].toString();
	}

	/**
	 * generates a random person
	 * 
	 * @return random person
	 */
	public Person getRandomPerson() {
		int age = getRandomAge();
		Profession profession = Person.AgeCategoryUtil(age) == AgeCategory.ADULT
				? Profession.values()[random.nextInt(Profession.values().length - 2)]
				: Profession.NONE;
		Gender gender = getRandomGender();
		BodyType bodyType = getRandomBodytype();
		boolean isPregnant = gender == Gender.FEMALE ? random.nextBoolean() : false;
		return new Person(age, profession, gender, bodyType, isPregnant);
	}

	/**
	 * generated a random Animal
	 * 
	 * @return random animal Animal
	 */
	public Animal getRandomAnimal() {
		int age = getRandomAge();
		Gender gender = getRandomGender();
		BodyType bodyType = getRandomBodytype();
		boolean isPet = random.nextBoolean();
		String species = getRandomSpecies();
		return new Animal(age, gender, bodyType, species, isPet);
	}

	/**
	 * generates random set of characters
	 * 
	 * @param min   sets the min number of character
	 * @param max   sets the max number of character
	 * @param isYou sets if character is you
	 * @return random characters
	 */
	private Character[] generateRandomCharacters(int min, int max, boolean isYou) {

		int countOfCharacters = random.nextInt(max - min) + min;
		int randomPersonIndex = random.nextInt(countOfCharacters);

		Character[] characters = new Character[countOfCharacters];

		for (int i = 0; i < countOfCharacters; i++) {
			if (i == randomPersonIndex) {
				Person person = this.getRandomPerson();
				person.setAsYou(isYou);
				characters[i] = person;
			} else {
				characters[i] = random.nextBoolean() ? this.getRandomPerson() : this.getRandomAnimal();
			}
		}

		return characters;
	}

	/**
	 * generates a random Scenario
	 * 
	 * @return random Scenario
	 */
	public Scenario generate() {
		Scenario scene = new Scenario();

		scene.setYouInCar(random.nextBoolean());
		scene.setYouInLane(scene.hasYouInCar() ? false : random.nextBoolean());
		scene.setPassengers(generateRandomCharacters(this.getPedestrianCountMin(), this.getPassengerCountMax(),
				scene.hasYouInCar()));
		scene.setPedestrians(generateRandomCharacters(this.getPedestrianCountMin(),
				this.getPedestrianCountMax(), scene.hasYouInLane()));
		scene.setLegalCrossing(random.nextBoolean());

		return scene;
	}
}
