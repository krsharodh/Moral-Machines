
/**
 * Protoype for parameters in a scenario
 * 
 * @author Sharodh Keelamanakudi Ragupathi(1148618)
 */

import java.util.Comparator;

public class Traits {
	private String trait;
	private float survivalRatio;

	/**
	 * gets the name of the parameter
	 * 
	 * @return trait name
	 */
	public String getTrait() {
		return trait;
	}

	/**
	 * sets the name of the parameter
	 * 
	 * @param name sets the name of the trait
	 */
	public void setTrait(String name) {
		this.trait = name;
	}

	/**
	 * gets the value associated with the attribute
	 * 
	 * @return survival ratio
	 */
	public float getSurvivalRatio() {
		return survivalRatio;
	}

	/**
	 * sets the value associated with the attibute
	 * 
	 * @param value sets the survival ratio
	 */
	public void setSurvivalRatio(float value) {
		this.survivalRatio = value;
	}

	/**
	 * create a ParamOfScene with default values
	 * 
	 */
	public Traits() {
		this.trait = "";
		this.survivalRatio = 0f;
	}

	/**
	 * creates a ParamOfScene with predefined name and value
	 * 
	 * @param name  sets the name of the trait
	 * @param value sets the survival ratio
	 */
	public Traits(String name, float value) {
		this.trait = name;
		this.survivalRatio = value;
	}

	/**
	 * gets the string format of the object
	 *
	 * @return String representation of the trait
	 */
	@Override
	public String toString() {
		return String.format("%s: %.1f\n", this.getTrait(), this.getSurvivalRatio());
	}

	/**
	 * Comparator to sort the ParamOfScene arraylist
	 */
	public static Comparator<Traits> sortByRatios = new Comparator<Traits>() {
		/**
		 * Sorts the arraylist based on the survival ratios
		 * 
		 * @param param1 survival ratio 1
		 * @param param2 survival ratio 2
		 */
		@Override
		public int compare(Traits param1, Traits param2) {
			return (param2.getSurvivalRatio() < param1.getSurvivalRatio() ? -1
					: (param2.getSurvivalRatio() == param1.getSurvivalRatio() ? 0 : 1));
		}
	};
}
