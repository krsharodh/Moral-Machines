import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ethicalengine.Scenario;
import ethicalengine.ScenarioGenerator;
import ethicalengine.Character;
import ethicalengine.Character.Gender;
import ethicalengine.Character.TypeofCharacter;
import ethicalengine.Animal;
import ethicalengine.Person;
import ethicalengine.Person.Profession;

/**
 * Performs the audit of the scenario and generates a stats file
 * 
 * @author Sharodh Keelamanakudi Ragupathi(1148618)
 *
 */
public class Audit {

	final int SCENARIOS_PER_ITERATION = 3;
	final String AUDIT_TYPE_ALGORITHM = "Unspecified";
	final String AUDIT_TYPE_USER = "User";
	final String PREGNANT_PARAM_KEY = "pregnant";
	final String PET_PARAM_KEY = "pet";
	final String GREEN_PARAM_KEY = "green";
	final String RED_PARAM_KEY = "red";
	final String YOU_PARAM_KEY = "you";
	final String AGE_PARAM_KEY = "age";

	private String auditType;
	private int runs;
	private Scenario[] scenarios;
	private boolean exitProgram = false;
	private boolean isInteractive;
	private int totalAgeOfSurvivors = 0;
	private int totalSurvivors = 0;

	Map<String, Integer> traitOfAllCharacters = new HashMap<String, Integer>();
	Map<String, Integer> traitOfSafeCharacters = new HashMap<String, Integer>();

	ArrayList<Traits> survivalRatios = new ArrayList<Traits>();

	/**
	 * returns the number of times audit have been run
	 * 
	 * @return number of runs
	 */
	public int getRuns() {
		return runs;
	}

	/**
	 * increments the runs by value passed
	 * 
	 * @param runs the value to which the runs has to incremented
	 */
	public void incrementRuns(int runs) {
		this.runs += runs;
	}

	/**
	 * gets if the audit is run in interactive mode
	 * 
	 * @return if Audit is done in interactive mode
	 */
	public boolean isInteractive() {
		return isInteractive;
	}

	/**
	 * sets interactive audit mode
	 * 
	 * @param auditType type of audit
	 */
	public void setIsInteractive(boolean isInteractive) {
		this.isInteractive = isInteractive;
	}

	/**
	 * gets the name of the audit
	 * 
	 * @return audit type
	 */
	public String getAuditType() {
		return auditType;
	}

	/**
	 * sets the name of the audit
	 * 
	 * @param name for audit type
	 */
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	/**
	 * Creates a instance for audit with default values
	 */
	public Audit() {
	}

	/**
	 * Creates a instance for audit with predefined scenarios
	 * 
	 * @param scenarios On which audit runs
	 */
	public Audit(Scenario[] scenarios) {
		this.scenarios = scenarios;
	}

	/**
	 * conducts audit with specified number of runs
	 * 
	 */
	public void run() {
		this.setAuditType(isInteractive ? AUDIT_TYPE_USER : AUDIT_TYPE_ALGORITHM);
		for (int i = 0; i < scenarios.length; i++) {
			if (isInteractive && (i % SCENARIOS_PER_ITERATION == 0) && i > 0) {
				this.computerSurvivalRatios();
				printStatistic();
				checkIfUserWantsToContinue();
				if (this.exitProgram) {
					break;
				}
			}
			this.incrementRuns(1);
			EthicalEngine.Decision decision = isInteractive ? getUserDecision(scenarios[i]) : EthicalEngine.decide(scenarios[i]);

			this.addTraitBasedOnCharacters(scenarios[i].getPassengers(), decision == EthicalEngine.Decision.PASSENGERS);
			this.addTraitBasedOnCharacters(scenarios[i].getPedestrians(), decision == EthicalEngine.Decision.PEDESTRIANS);
			this.addParamBasedOnScene(scenarios[i], decision);
		}

		this.computerSurvivalRatios();
		printStatistic();
	}

	/**
	 * injects the characteristic into the hashmap based on the survival
	 * 
	 * @param key    key of the param
	 * @param isSafe is going to be saved
	 */
	private void injectIntoParam(String key, boolean isSafe) {
		if (isSafe) {
			traitOfSafeCharacters.put(key,
					traitOfSafeCharacters.get(key) != null ? traitOfSafeCharacters.get(key) + 1 : 1);
		}
		traitOfAllCharacters.put(key, traitOfAllCharacters.get(key) != null ? traitOfAllCharacters.get(key) + 1 : 1);
	}

	/**
	 * extracts the various parameters of a character
	 * 
	 * @param characters traits on characters
	 * @param isSafe     is the character going to be saved
	 */
	private void addTraitBasedOnCharacters(Character[] characters, boolean isSafe) {

		for (Character character : characters) {
			Person person = null;
			Animal animal = null;

			if (character.getTypeOfCharacter() == TypeofCharacter.PERSON) {
				person = new Person(((Person) character));
				// bodytype
				this.injectIntoParam(person.getBodyType().toString(), isSafe);

				// Gender]
				if (person.getGender() != Gender.UNKNOWN) {
					this.injectIntoParam(person.getGender().toString(), isSafe);
				}

				// Person
				this.injectIntoParam(TypeofCharacter.PERSON.toString(), isSafe);

				// profession
				if (person.getProfession() != Profession.NONE)
					this.injectIntoParam(person.getProfession().toString(), isSafe);

				// pregnant
				if (person.isPregnant()) {
					this.injectIntoParam(PREGNANT_PARAM_KEY, isSafe);
				}

				// age
				if (isSafe) {
					totalAgeOfSurvivors += person.getAge();
					totalSurvivors++;
				}

				// agecategory
				this.injectIntoParam(person.getAgeCategory().toString(), isSafe);

			} else {
				animal = new Animal(((Animal) character));

				// Animal
				this.injectIntoParam(TypeofCharacter.ANIMAL.toString(), isSafe);

				// Species
				this.injectIntoParam(animal.getSpecies().toString(), isSafe);

				// Pet
				if (animal.isPet()) {
					this.injectIntoParam(PET_PARAM_KEY, isSafe);
				}
			}

		}
	}

	/**
	 * extracts the various parameters of a scene
	 * 
	 * @param scene    traits of the scene
	 * @param decision whom to save
	 */
	private void addParamBasedOnScene(Scenario scene, EthicalEngine.Decision decision) {
		// you
		if (scene.hasYouInCar() || scene.hasYouInLane()) {
			boolean isSafe = (scene.hasYouInCar() && decision == EthicalEngine.Decision.PASSENGERS)
					|| (scene.hasYouInLane() && decision == EthicalEngine.Decision.PEDESTRIANS);
			this.injectIntoParam(YOU_PARAM_KEY, isSafe);
		}

		// legality
		String key = scene.isLegalCrossing() ? GREEN_PARAM_KEY : RED_PARAM_KEY;
		int survivorCount = decision == EthicalEngine.Decision.PASSENGERS ? scene.getPassengerCount() : scene.getPedestrianCount();
		int totalCount = scene.getPassengerCount() + scene.getPedestrianCount();
		traitOfSafeCharacters.put(key,
				traitOfSafeCharacters.get(key) != null ? traitOfSafeCharacters.get(key) + survivorCount
						: survivorCount);
		traitOfAllCharacters.put(key,
				traitOfAllCharacters.get(key) != null ? traitOfAllCharacters.get(key) + totalCount : totalCount);
	}

	/**
	 * gets the decision of whom to save from user
	 * 
	 * @param scene scenario for making a decision
	 * @return user decision
	 */
	private EthicalEngine.Decision getUserDecision(Scenario scene) {
		EthicalEngine.Decision userDecision = EthicalEngine.Decision.PASSENGERS;
		System.out.println(scene);
		System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
		String decision = EthicalEngine.KEY_BOARD.nextLine();
		if (decision.equals("1") || decision.equals("passenger") || decision.equals("passengers")) {
			userDecision = EthicalEngine.Decision.PASSENGERS;
		} else if (decision.equals("2") || decision.equals("pedestrian") || decision.equals("pedestrians")) {
			userDecision = EthicalEngine.Decision.PEDESTRIANS;
		}
		return userDecision;
	}

	/**
	 * starts Initiates audit with the specified number of runs
	 * 
	 * @param number of runs for the audit
	 */
	private void startAudit(int runs) {
		Scenario[] scenarios = new Scenario[runs];
		for (int i = 0; i < runs; i++) {
			ScenarioGenerator sceneGenerator = new ScenarioGenerator();
			scenarios[i] = sceneGenerator.generate();
		}
		this.scenarios = scenarios;
		run();
	}

	/**
	 * checks if the user wants to continue the program
	 * 
	 */
	private void checkIfUserWantsToContinue() {
		if (!this.exitProgram) {
			System.out.println("Would you like to continue? (yes/no)");
			this.exitProgram = EthicalEngine.KEY_BOARD.nextLine().equals("yes") ? false : true;
		}
	}

	/**
	 * runs the audit with the specified number of runs
	 * 
	 * @param runs number of times the audit runs
	 */
	public void run(int runs) {
		this.setAuditType(isInteractive ? AUDIT_TYPE_USER : AUDIT_TYPE_ALGORITHM);
		if (isInteractive) {
			while (!this.exitProgram) {
				startAudit(SCENARIOS_PER_ITERATION);
				checkIfUserWantsToContinue();
			}
		} else {
			startAudit(runs);
		}

	}

	/**
	 * Computes the survival ratio for the trait
	 * 
	 * @param key   trait name
	 * @param value trait occurrence
	 * @return trait of the scene
	 */
	private Traits getSurvivalRatioOfTrait(String key, float value) {
		float survivalRatio = (this.traitOfSafeCharacters.get(key) == null ? 0
				: this.traitOfSafeCharacters.get(key).floatValue()) / value;
		return new Traits(key, survivalRatio);
	}

	/**
	 * computes the survival ratio for all the attributes
	 */
	private void computerSurvivalRatios() {
		this.survivalRatios.clear();
		for (Map.Entry<String, Integer> entry : this.traitOfAllCharacters.entrySet()) {
			this.survivalRatios.add(getSurvivalRatioOfTrait(entry.getKey(), entry.getValue()));
		}
		Collections.sort(this.survivalRatios, Traits.sortByRatios);
	}

	/**
	 * Converts the audit object in a string format
	 */
	@Override
	public String toString() {
		String bar = "======================================\n";
		String banner = String.format("%s# %s Audit\n%s", bar, this.getAuditType(), bar);
		String metadata = "- % " + String.format("SAVED AFTER %d RUNS\n", this.getRuns());

		String traits = "";
		for (Traits trait : survivalRatios)
			traits += trait;

		float avgAge = (float) this.totalAgeOfSurvivors / (float) this.totalSurvivors;
		traits += String.format("--\naverage age: %.1f", avgAge);

		return banner + metadata + traits;
	}

	/**
	 * prints stats in the console
	 */
	public void printStatistic() {
		System.out.println(this);
	}

	/**
	 * prints the stats in a file
	 * 
	 * @param filepath path of the file where the audit results will be stored
	 */
	public void printToFile(String filepath) {
		String oldData;
		try {
			oldData = restoreData(filepath);
			saveDataOffline(filepath, oldData);
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * appends the stats in a file along with old data
	 * 
	 * @param filepath filepath path of the file where the audit results will be
	 *                 stored
	 * @param oldData  data present in old file
	 * @throws IOException
	 */
	private void saveDataOffline(String filepath, String oldData) throws IOException {
		EthicalEngine.moralMachineWriter = new PrintWriter(new FileOutputStream(filepath));
		EthicalEngine.moralMachineWriter.println((oldData.equals("") ? oldData : oldData + "\n") + this);
		EthicalEngine.moralMachineWriter.close();
	}

	/**
	 * gets the old data of the stats file
	 * 
	 * @param filepath filepath path of the file where the audit results will be
	 *                 stored
	 * @return data of the old file
	 * @throws IOException
	 * @throws Exception
	 */
	private String restoreData(String filepath) throws IOException {
		String oldData = "";
		File moralMachinesStats = new File(filepath);
		if (moralMachinesStats.exists()) {
			EthicalEngine.moralMachinesBufferReader = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = EthicalEngine.moralMachinesBufferReader.readLine()) != null) {
				oldData += line + "\n";
			}
		} else if (filepath.contains("/")) {
			File moralMachinesStatsParentDict = new File(moralMachinesStats.getParent());

			if (!moralMachinesStatsParentDict.isDirectory()) {
				String exeptionMsg = "ERROR: could not print results. Target directory does not exist.";
				throw new FileNotFoundException(exeptionMsg);
			}
		}

		return oldData;
	}

}
