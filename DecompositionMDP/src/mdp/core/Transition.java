package mdp.core;



/** 
* @file Transition.java 
* 
* @brief This file contains the source code for the Transition bean and Util methods
* 
* @author Ankit Mishra
*
* @date May, 1 2015
* 
**/

/** 
* @class Transition
* 
* @brief This class contains the source code for the Transition bean and Util methods
* 
**/


public class Transition 
{
	private float probability;
	private String toStateLabel;
	private String action; 
	/** 
	* @brief This is a parameterized constructor of class Transition
	* 
	**/
	public Transition(float probability, String toStateLabel, String action)
	{
		super();
		this.probability = probability;
		this.toStateLabel = toStateLabel;
		this.action =action;
	}
	/** 
	* @brief This method is a getter for the Transition probability
	* 
	**/
	public float getProbability() 
	{
		return probability;
	}
	/** 
	* @brief This method is a setter for the Transition probability
	* 
	**/
	public void setProbability(float probability) 
	{
		this.probability = probability;
	}	
	/** 
	* @brief This method is a getter for the destination State of the Transition
	* 
	**/
	public String getToState() {
		return toStateLabel;
	}
	/** 
	* @brief This method is a setter for the destination State of the Transition
	* 
	**/
	public void setToState(String toStateLabel) {
		this.toStateLabel = toStateLabel;
	}
	/** 
	* @brief This method is a getter for the Transition action
	* 
	**/
	public String getAction() {
		return action;
	}
	/** 
	* @brief This method is a setter for the Transition action
	* 
	**/
	public void setAction(String action) {
		this.action = action;
	}
	/** 
	* @brief This method converts the Transition object into a String object for displaying on the console/GUI
	* 
	**/
	@Override
	public String toString() 
	{
		return "Transition [probability=" + probability + ", toState="+ toStateLabel + ", action=" + action + "]";
	}
	/** 
	* @brief This method is used to compare two Transition objects
	* 
	**/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + Float.floatToIntBits(probability);
		result = prime * result
				+ ((toStateLabel == null) ? 0 : toStateLabel.hashCode());
		return result;
	}
	/** 
	* @brief This method is used to check if two Transition objects are equal
	* 
	**/
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Transition other = (Transition) obj;
		if (action == null) 
		{
			if (other.action != null)
			{
				return false;
			}
		} 
		else if (!action.equals(other.action))
		{
			return false;
		}
		if (Float.floatToIntBits(probability) != Float.floatToIntBits(other.probability))
		{
			return false;
		}
		if (toStateLabel == null) 
		{
			if (other.toStateLabel != null)
			{
				return false;
			}
		} 
		else if (!toStateLabel.equals(other.toStateLabel))
		{
			return false;
		}
		return true;
	}
	
	
}
