package ethicalengine;

/**
 * Prototype of a Scenario
 * 
 * @author Sharodh Keelamanakudi Ragupathi(1148618)
 */

public class Scenario {

	private Character[] passengers;
	private Character[] pedestrians;
	private boolean isLegalCrossing;
	private boolean youInCar;
	private boolean youInLane;

	/**
	 * Creates a Scenario with default values
	 */
	public Scenario() {
		this.setPassengers(null);
		this.setPedestrians(null);
		this.setLegalCrossing(false);
		this.setYouInCar(false);
		this.setYouInLane(false);
	}

	/**
	 * Creates a Scenario with predefined values of passengers, pedestrians and is
	 * legal crossing
	 * 
	 * @param passengers      array of characters as passengers
	 * @param pedestrians     array of characters as pedestrians
	 * @param isLegalCrossing legality of the scene
	 */
	public Scenario(Character[] passengers, Character[] pedestrians, boolean isLegalCrossing) {
		this.setPassengers(passengers);
		this.setPedestrians(pedestrians);
		this.setLegalCrossing(isLegalCrossing);
		this.setYouInCar(false);
		this.setYouInLane(false);
	}

	/**
	 * gets the passengers of the scenario
	 * 
	 * @return Character array of passengers
	 */
	public Character[] getPassengers() {
		return passengers;
	}

	/**
	 * sets the passengers of the scenario
	 * 
	 * @param passengers array of passengers
	 */
	public void setPassengers(Character[] passengers) {
		this.passengers = passengers;
	}

	/**
	 * gets the pedestrians of the scenario
	 * 
	 * @return Character array of pedestrians
	 */
	public Character[] getPedestrians() {
		return pedestrians;
	}

	/**
	 * sets the pedestrians of the scenario
	 * 
	 * @param pedestrians array of pedestrians
	 */
	public void setPedestrians(Character[] pedestrians) {
		this.pedestrians = pedestrians;
	}

	/**
	 * gets the legality of the scenario
	 * 
	 * @return legality of a scenario
	 */
	public boolean isLegalCrossing() {
		return isLegalCrossing;
	}

	/**
	 * sets the legality of the scenario
	 * 
	 * @param isLegalCrossing sets the legality
	 */
	public void setLegalCrossing(boolean isLegalCrossing) {
		this.isLegalCrossing = isLegalCrossing;
	}

	/**
	 * gets the passenger count
	 * 
	 * @return count of passengers
	 */
	public int getPassengerCount() {
		return this.getPassengers().length;
	}

	/**
	 * gets the pedestrian count
	 * 
	 * @return count of pedestrians
	 */
	public int getPedestrianCount() {
		return this.getPedestrians().length;
	}

	/**
	 * returns if you in lane
	 * 
	 * @return you in lane condition
	 */
	public boolean hasYouInLane() {
		return this.youInLane;
	}

	/**
	 * returns if you in car
	 * 
	 * @return you in car condition
	 */
	public boolean hasYouInCar() {
		return this.youInCar;
	}

	/**
	 * sets you in car
	 * 
	 * @param youInCar value to set you in car
	 */
	public void setYouInCar(boolean youInCar) {
		this.youInCar = youInCar;
	}

	/**
	 * sets you in lane
	 * 
	 * @param youInLane value to set you in lane
	 */
	public void setYouInLane(boolean youInLane) {
		this.youInLane = youInLane;
	}

	/**
	 * gets a formatted list of characters
	 * 
	 * @param list unformatted list of characters
	 * @return formatted list of characters as string
	 */
	private String getListFormatted(Character[] list) {
		String formattedList = "";
		for (int i = 0; i < list.length; i++) {
			formattedList += "- " + list[i];
			if (i != list.length - 1) {
				formattedList += "\n";
			}
		}
		return formattedList;
	}

	/**
	 * gets the String representation of a Scenario
	 * 
	 * @return String representation of a Scenario
	 */
	public String toString() {
		String bar = "======================================\n";

		String isLegalCrossing = this.isLegalCrossing() ? "yes" : "no";

		return String.format("%s# Scenario\n%sLegal Crossing: %s\nPassengers (%d)\n%s\nPedestrians (%d)\n%s", bar, bar,
				isLegalCrossing, this.getPassengerCount(), this.getListFormatted(getPassengers()),
				this.getPedestrianCount(), this.getListFormatted(getPedestrians()));
	}
}
