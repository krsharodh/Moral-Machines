import ethicalengine.Scenario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ethicalengine.Animal;
import ethicalengine.Character;
import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Character.TypeofCharacter;
import ethicalengine.Person;
import ethicalengine.Person.Profession;

/**
 * Runs Moral Machines in different modes. Stores the core logic for saving
 * 
 * @author Sharodh Keelamanakudi Ragupathi(1148618)
 */

public class EthicalEngine {

	enum FlagTypes {
		INTERACTIVE, CONFIG, HELP, PATH, RESULTS;

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}
	
	public enum Decision {
		PEDESTRIANS, PASSENGERS;

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	final int IS_NOT_LEGAL_PENALTY = -2;
	final String WELCOME_FILE_NAME = "welcome.ascii";
	final String DECIDE_STATS_FILE_NAME = "results.log";
	final String USER_LOG_FILE_NAME = "user.log";
	final int startingLineIndex = 2;
	final int DEFAULT_RUNS = 100;

	static PrintWriter moralMachineWriter;
	static BufferedReader moralMachinesBufferReader;

	static final Scanner KEY_BOARD = new Scanner(System.in);

	private String resultsPath;
	private boolean isInteractive;
	private boolean userConsentForStoring;

	Audit audit;

	/**
	 * Default Constructor
	 */
	EthicalEngine() {
		this.resultsPath = "";
		this.isInteractive = false;
		this.userConsentForStoring = false;
	}

	/**
	 * adds the importance value of the characters passed in
	 * 
	 * @param characters accepts array of characters for which importance has to be
	 *                   found
	 * @return cumulative importance value of characters
	 */
	public int getImportance(Character[] characters) {
		int importance = 0;

		for (Character character : characters) {
			importance += character.computeImportance();
		}

		return importance;
	}

	/**
	 * decides whom to save for a given scenario
	 * 
	 * @param scenario gets the scenario for which decision has to be made
	 * @return Decision of whom to be saved
	 */
	public static Decision decide(Scenario scenario) {
		EthicalEngine operator = new EthicalEngine();

		int pedImportance = operator.getImportance(scenario.getPedestrians());
		pedImportance += scenario.isLegalCrossing() ? 0
				: (scenario.getPedestrianCount() * operator.IS_NOT_LEGAL_PENALTY);

		int passengerImportance = operator.getImportance(scenario.getPassengers());

		return pedImportance > passengerImportance ? Decision.PEDESTRIANS : Decision.PASSENGERS;
	}

	/**
	 * checks if a scenario has valid number of elements
	 * 
	 * @param scenario   The line parts of scenario for which validity has to be
	 *                   checked
	 * @param lineNumber line number of the scenario from config file
	 * @throws InvalidDataFormatException
	 */
	private void isValidScenario(String[] scenario, int lineNumber) throws InvalidDataFormatException {
		if (scenario.length != 10) {
			throw new InvalidDataFormatException(
					String.format("WARNING: invalid data format in config file in line %d", lineNumber));
		}
	}

	/**
	 * gets the gender from raw string
	 * 
	 * @param _gender    String value of gender
	 * @param lineNumber line number of the scenario from config file
	 * @return type of gender
	 */
	private Gender getGenderFromConfig(String _gender, int lineNumber) {
		try {
			for (Gender gender : Gender.values()) {
				if (gender.toString().equals(_gender)) {
					return gender;
				}
			}
			throw new InvalidCharacteristicException(
					String.format("WARNING: invalid characteristic in config file in line %d", lineNumber));
		} catch (InvalidCharacteristicException e) {
			System.out.println(e.getLocalizedMessage());
			return Gender.UNKNOWN;
		}
	}

	/**
	 * @param age        Age in string
	 * @param lineNumber line number of the scenario from config file
	 * @return age in integer
	 * @throws NumberFormatException
	 */
	private int getAgeFromConfig(String age, int lineNumber) throws NumberFormatException {
		try {
			return Integer.parseInt(age);
		} catch (NumberFormatException e) {
			System.out.println(String.format("WARNING: invalid number format in config file in line %d", lineNumber));
			return 0;
		}
	}

	/**
	 * gets the bodytype from raw string
	 * 
	 * @param _bodyType  String value of bodytype
	 * @param lineNumber line number of the scenario from config file
	 * @return type of bodytype
	 */
	private BodyType getBodytypeFromConfig(String _bodyType, int lineNumber) {
		try {
			for (BodyType bodyType : BodyType.values()) {
				if (bodyType.toString().equals(_bodyType)) {
					return bodyType;
				}
			}
			throw new InvalidCharacteristicException(
					String.format("WARNING: invalid characteristic in config file in line %d", lineNumber));
		} catch (InvalidCharacteristicException e) {
			System.out.println(e.getLocalizedMessage());
			return BodyType.UNSPECIFIED;
		}
	}

	/**
	 * gets the profession from raw string
	 * 
	 * @param _profession String value of profession
	 * @param lineNumber  line number of the scenario from config file
	 * @return type of profession
	 */
	private Profession getProfessionFromConfig(String _profession, int lineNumber) {
		try {
			for (Profession profession : Profession.values()) {
				if (profession.toString().equals(_profession)) {
					return profession;
				}
			}
			throw new InvalidCharacteristicException(
					String.format("WARNING: invalid characteristic in config file in line %d", lineNumber));
		} catch (InvalidCharacteristicException e) {
			System.out.println(e.getLocalizedMessage());
			return Profession.UNKNOWN;
		}
	}

	/**
	 * @param _decision  string value of decision
	 * @param lineNumber line number of the scenario from config file
	 * @return type of decision
	 */
	private Decision getDecisionFromConfig(String _decision, int lineNumber) {
		try {
			if (_decision.equals("passenger"))
				return Decision.PASSENGERS;
			else if (_decision.equals("pedestrian")) {
				return Decision.PEDESTRIANS;
			}
			throw new InvalidCharacteristicException(
					String.format("WARNING: invalid characteristic in config file in line %d", lineNumber));
		} catch (InvalidCharacteristicException e) {
			System.out.println(e.getLocalizedMessage());
			return Decision.PASSENGERS;
		}
	}

	/**
	 * sets bodytype, gender and age for a character
	 * 
	 * @param character     character for which the basic characteristics has to be
	 *                      sent
	 * @param linePartsInfo traits from the config file
	 * @param lineNumber    line number of the scenario from config file
	 */
	private void setBasicCharacteristics(Character character, Map<String, String> linePartsInfo, int lineNumber) {
		if (character.getTypeOfCharacter() == TypeofCharacter.PERSON) {
			character.setBodyType(getBodytypeFromConfig(linePartsInfo.get("bodyType"), lineNumber));
		}
		character.setGender(getGenderFromConfig(linePartsInfo.get("gender"), lineNumber));
		character.setAge(this.getAgeFromConfig(linePartsInfo.get("age"), lineNumber));
	}

	/**
	 * Converts character arraylist to array
	 * 
	 * @param list Arraylist of characters
	 * @return Character array
	 */
	private Character[] getCharacterArray(ArrayList<Character> list) {
		Character[] characters = new Character[list.size()];
		for (int i = 0; i < list.size(); i++) {
			characters[i] = list.get(i);
		}
		return characters;
	}

	/**
	 * Converts Scenario arraylist to array
	 * 
	 * @param list Arraylist of scenarios
	 * @return scenario array
	 */
	private Scenario[] getScenarioArray(ArrayList<Scenario> list) {
		Scenario[] scenarios = new Scenario[list.size()];
		for (int i = 0; i < list.size(); i++) {
			scenarios[i] = list.get(i);
		}
		return scenarios;
	}

	/**
	 * gets a Person with the characteristics specified in configline
	 * 
	 * @param linePartsInfo traits from the config file
	 * @param lineIndex     line number of the scenario from config file
	 * @return Person object with specified traits
	 */
	private Person getPerson(Map<String, String> linePartsInfo, int lineIndex) {
		Person person = new Person();
		setBasicCharacteristics(person, linePartsInfo, lineIndex);

		if (!linePartsInfo.get("profession").equals("")) {
			person.setProfession(getProfessionFromConfig(linePartsInfo.get("profession"), lineIndex));
		}

		if (!linePartsInfo.get("pregnant").equals("")) {
			person.setPregnant(Boolean.parseBoolean(linePartsInfo.get("pregnant")));
		}

		if (!linePartsInfo.get("isYou").equals("")) {
			person.setAsYou(Boolean.parseBoolean(linePartsInfo.get("isYou")));
		}

		return person;
	}

	/**
	 * gets a Animal with the characteristics specified in configline
	 * 
	 * @param linePartsInfo traits from the config file
	 * @param lineIndex     line number of the scenario from config file
	 * @return Animal object with specified traits
	 */
	private Animal getAnimal(Map<String, String> linePartsInfo, int lineIndex) {
		Animal animal = new Animal();
		setBasicCharacteristics(animal, linePartsInfo, lineIndex);

		if (!linePartsInfo.get("species").equals("")) {
			animal.setSpecies(linePartsInfo.get("species"));
		}

		if (!linePartsInfo.get("isPet").equals("")) {
			animal.setPet(Boolean.parseBoolean(linePartsInfo.get("isPet")));
		}

		return animal;
	}

	/**
	 * Adds the scene to the scenario arraylist by setting the passengers and
	 * pesdestrians
	 * 
	 * @param scene       The scene which has to be added to scenarios
	 * @param scenarios   The arraylist to which the scenes has to be added
	 * @param pedestrians the pedestrians of the scene
	 * @param passengers  the passengers of the scene
	 */
	private void addSceneToScenarios(Scenario scene, ArrayList<Scenario> scenarios, ArrayList<Character> pedestrians,
			ArrayList<Character> passengers) {
		scene.setPassengers(getCharacterArray(passengers));
		scene.setPedestrians(getCharacterArray(pedestrians));
		passengers.clear();
		pedestrians.clear();
		scenarios.add(scene);
	}

	/**
	 * gets a Map of different parts in a config line
	 * 
	 * @param line      Config line
	 * @param lineIndex line number of the scenario from config file
	 * @return traits Map<Attribute, Value> from the config file
	 * @throws InvalidDataFormatException
	 */
	private Map<String, String> getMapOfLineParts(String line, int lineIndex) throws InvalidDataFormatException {
		String[] lineParts = line.split(",", -1);
		String[] rowTypeParts = lineParts[0].split(":", -1);

		Map<String, String> lineData = new HashMap<String, String>();

		if (rowTypeParts[0].equals("scenario")) {
			lineData.put("rowType", rowTypeParts[0]);
			lineData.put("legality", rowTypeParts[1]);
			return lineData;
		}

		this.isValidScenario(lineParts, lineIndex);

		lineData.put("rowType", lineParts[0]);
		lineData.put("gender", lineParts[1]);
		lineData.put("age", lineParts[2]);
		lineData.put("bodyType", lineParts[3]);
		lineData.put("profession", lineParts[4]);
		lineData.put("pregnant", lineParts[5]);
		lineData.put("isYou", lineParts[6]);
		lineData.put("species", lineParts[7]);
		lineData.put("isPet", lineParts[8]);
		lineData.put("decision", lineParts[9]);

		return lineData;
	}

	/**
	 * Constructs a scenario array from various parts of a config file
	 * 
	 * @return array of scenarios
	 * @throws IOException
	 */
	private Scenario[] readDataFromConfigUtil() throws IOException {

		// Remove Headers
		moralMachinesBufferReader.readLine();

		// Set line number
		int lineIndex = startingLineIndex;

		// Create scene by reading the file
		Scenario scene = null;
		ArrayList<Scenario> scenarios = new ArrayList<Scenario>();
		ArrayList<Character> pedestrians = new ArrayList<Character>();
		ArrayList<Character> passengers = new ArrayList<Character>();

		String line;
		while ((line = moralMachinesBufferReader.readLine()) != null) {
			try {
				Map<String, String> linePartsInfo = getMapOfLineParts(line, lineIndex);

				if (linePartsInfo.get("rowType").equals("scenario")) {
					if (scene != null) {
						addSceneToScenarios(scene, scenarios, pedestrians, passengers);
					}
					scene = new Scenario();
					scene.setLegalCrossing(linePartsInfo.get("legality").equals("green") ? true : false);
				} else {
					Character character;
					character = linePartsInfo.get("rowType").equals("person") ? getPerson(linePartsInfo, lineIndex)
							: getAnimal(linePartsInfo, lineIndex);

					Decision decision = getDecisionFromConfig(linePartsInfo.get("decision"), lineIndex);
					if (decision == Decision.PASSENGERS) {
						passengers.add(character);
						if (!linePartsInfo.get("isYou").equals("")) {
							scene.setYouInCar(
									scene.hasYouInCar() ? true : Boolean.parseBoolean(linePartsInfo.get("isYou")));
						}
					} else {

						pedestrians.add(character);
						if (!linePartsInfo.get("isYou").equals("")) {
							scene.setYouInLane(
									scene.hasYouInLane() ? true : Boolean.parseBoolean(linePartsInfo.get("isYou")));
						}
					}

				}
			} catch (InvalidDataFormatException e) {
				System.out.println(e.getLocalizedMessage());
			}
			lineIndex++;
		}
		addSceneToScenarios(scene, scenarios, pedestrians, passengers);
		return getScenarioArray(scenarios);
	}

	/**
	 * Prints the welcome.ascii file
	 * 
	 * @throws IOException
	 */
	private void printWelcomeScreen() throws IOException {
		moralMachinesBufferReader = new BufferedReader(new FileReader(WELCOME_FILE_NAME));
		String line;
		while ((line = moralMachinesBufferReader.readLine()) != null) {
			System.out.println(line);
		}
	}

	/**
	 * Checks if the user consent is valid
	 * 
	 * @return consent of the user to store the results
	 * @throws InvalidInputException
	 */
	private boolean getUserConsentUtil() throws InvalidInputException {
		String userConsent = KEY_BOARD.nextLine();
		if (userConsent.equals("yes")) {
			userConsentForStoring = true;
			return true;
		} else if (userConsent.equals("no")) {
			userConsentForStoring = false;
			return true;
		} else {
			throw new InvalidInputException(
					"Invalid response. Do you consent to have your decisions saved to a file? (yes/no)");
		}
	}

	/**
	 * Gets the consent of the user the to save the decisions in file
	 */
	private void getUserConsent() {
		boolean validConsent = false;
		System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
		do {
			try {
				validConsent = getUserConsentUtil();
			} catch (InvalidInputException e) {
				System.out.println(e.getLocalizedMessage());
			}
		} while (!validConsent);
	}

	/**
	 * Reads the data from config file
	 * 
	 * @param filepath the file path of the config file
	 * @throws IOException
	 * @throws Exception
	 */
	private void readDataFromConfig(String filepath) throws IOException {
		File configFile = new File(filepath);
		if (configFile.exists()) {
			moralMachinesBufferReader = new BufferedReader(new FileReader(configFile));
			audit = new Audit(readDataFromConfigUtil());
			if (isInteractive) {
				this.audit.setIsInteractive(isInteractive);
				printWelcomeScreen();
				getUserConsent();
			}
			audit.run();
			storeAndPrint();
			quitFromApp();
		} else {
			String exeptionMsg = "ERROR: could not find config file.";
			throw new FileNotFoundException(exeptionMsg);
		}
	}

	/**
	 * Quits from the application by getting a consent from the user
	 * 
	 * @throws IOException
	 */
	private void quitFromApp() throws IOException {
		System.out.println("That's all. Press Enter to quit.");
		System.in.read();
		System.exit(0);
	}

	/**
	 * run moral machines with random scenarios based on flag of interactive mode
	 * 
	 */
	public void runRandomScenarios() {
		audit = new Audit();
		audit.setIsInteractive(isInteractive);
		audit.run(DEFAULT_RUNS);
		storeAndPrint();
	}

	/**
	 * Stores and prints the audit results
	 * 
	 */
	private void storeAndPrint() {
		if (userConsentForStoring || !isInteractive)
			audit.printToFile(this.resultsPath + (isInteractive ? USER_LOG_FILE_NAME : DECIDE_STATS_FILE_NAME));
	}

	/**
	 * Prints the help message for running moral machines
	 */
	private void printHelpMessage() {
		System.out.println("Ethical Engine - COMP90041 - Final Project\n");
		System.out.println("Usage: java EthicalEngine [arguments]\n");
		System.out.println("Arguments:");
		System.out.println(String.format("\t%-20sOptional: path to config file", "-c or --config"));
		System.out.println(String.format("\t%-20sPrint Help (this message) and exit", "-h or --help"));
		System.out.println(String.format("\t%-20sOptional: path to results log file", "-r or --results"));
		System.out.println(String.format("\t%-20sOptional: launches interactive mode", "-i or --interactive"));
	}

	/**
	 * classifies the flag passed
	 * 
	 * @param flag the flag that has to be classified
	 * @return type of flag
	 */
	private FlagTypes classifyFlag(String flag) {
		FlagTypes type = FlagTypes.PATH;
		if (flag.equals("-i") || flag.equals("--interactive")) {
			type = FlagTypes.INTERACTIVE;
		} else if (flag.equals("-h") || flag.equals("--help")) {
			type = FlagTypes.HELP;
		} else if (flag.equals("-c") || flag.equals("--config")) {
			type = FlagTypes.CONFIG;
		} else if (flag.equals("-r") || flag.equals("--results")) {
			type = FlagTypes.RESULTS;
		}
		return type;
	}

	/**
	 * gets the config file path
	 * 
	 * @param rawPath ore to extract config file from
	 * @return extracted filepath of config file
	 * @throws FileNotFoundException
	 */
	private String getPath(String rawPath) throws FileNotFoundException {
		File moralMachinesStats = new File(rawPath);
		if (moralMachinesStats.exists()) {
			return rawPath;
		} else {
			String exeptionMsg = "ERROR: could not find config file.";
			throw new FileNotFoundException(exeptionMsg);
		}
	}

	/**
	 * sets path of the results file
	 * 
	 * @param filepath the filepath for results
	 * @throws FileNotFoundException
	 */
	private void setResultsPath(String filepath) throws FileNotFoundException {
		File file = new File(filepath);
		if (!file.isDirectory()) {
			String exeptionMsg = "ERROR: could not print results. Target directory does not exist.";
			throw new FileNotFoundException(exeptionMsg);
		}
		this.resultsPath = !file.isDirectory() ? "" : filepath;
	}

	/**
	 * runs the moral machine application based on the supplied flags
	 * 
	 * @param args commanline arguments
	 */
	public void runMoralMachines(String[] args) {
		try {
			int i = 0;
			String configPath = "";

			boolean printHelpMessage = false;
			boolean shouldBeConfigPath = false;
			boolean shouldBeResultPath = false;

			while (i < args.length) {
				FlagTypes type = classifyFlag(args[i]);
				if (shouldBeConfigPath || shouldBeResultPath) {
					if (type == FlagTypes.PATH) {
						if (shouldBeConfigPath) {
							configPath = getPath(args[i]);
							shouldBeConfigPath = false;
						}
						if (shouldBeResultPath) {
							this.setResultsPath(args[i]);
							shouldBeResultPath = false;
						}
					} else {
						printHelpMessage();
						System.exit(0);
					}
				}

				if (type == FlagTypes.CONFIG) {
					shouldBeConfigPath = true;
				}

				if (type == FlagTypes.INTERACTIVE) {
					this.isInteractive = true;
				}

				if (type == FlagTypes.HELP) {
					printHelpMessage = true;
				}

				if (type == FlagTypes.RESULTS) {
					shouldBeResultPath = true;
				}

				i++;
			}

			if (printHelpMessage)
				printHelpMessage();

			if (configPath != "") {
				readDataFromConfig(configPath);
			} else {
				if (isInteractive) {
					printWelcomeScreen();
					getUserConsent();
				}

				runRandomScenarios();
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
			System.exit(0);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	/**
	 * entry point for moral machines execution
	 * 
	 * @param args commandline arguments
	 */
	public static void main(String[] args) {
		EthicalEngine controller = new EthicalEngine();
		controller.runMoralMachines(args);
		KEY_BOARD.close();
	}

}
