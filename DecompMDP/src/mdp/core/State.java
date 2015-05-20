package mdp.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/** 
* @file State.java 
* 
* @brief This file contains the source code for the State bean and Util methods
* 
* @author Ankit Mishra
*
* @date May, 1 2015
* 
**/

/** 
* @class State 
* 
* @brief This class contains the source code for the State bean and Util methods
* 
**/

 
public class State 
{
	private String label;
	private LinkedHashMap<String, Transition> transitions;
	private float initialProbability;
	private LinkedHashMap<String, Action> actionCounts;
	private String regionLabel;
	private String kernelLabel;
	/** 
	* @brief This is a partial paraterized constructor of the State class
	* 
	**/
	public State(String label, LinkedHashMap<String, Transition> transitions, float initialProbability) 
	{
		super();
		this.label = label;
		this.transitions = transitions;
		this.initialProbability = initialProbability;
		this.actionCounts=new LinkedHashMap<String, Action>();
		this.kernelLabel=new String();
		this.regionLabel=new String();
	}
	/** 
	* @brief This is a complete paraterized constructor of the State class
	* 
	**/
	public State(String label, LinkedHashMap<String, Transition> transitions,float initialProbability, LinkedHashMap<String, Action> actionCounts, String regionLabel, String kernelLabel) 
	{
		super();
		this.label = label;
		this.transitions = transitions;
		this.initialProbability = initialProbability;
		this.actionCounts = actionCounts;
		this.kernelLabel=kernelLabel;
		this.regionLabel=regionLabel;
	}
	/** 
	* @brief This is a default constructor of the State class
	* 
	**/
	public State(String label) 
	{
		super();
		this.label = label;
		this.transitions=new LinkedHashMap<String, Transition>();
		this.initialProbability=0;
		this.actionCounts=new LinkedHashMap<String, Action>();
		this.kernelLabel=new String();
		this.regionLabel=new String();
	}
	/** 
	* @brief This is a copy constructor of the State class that creates a deep copy of the argument State
	* 
	**/
	public State(State state) 
	{
		super();
		this.label = state.label;
		this.transitions = state.transitions;
		this.initialProbability = state.initialProbability;
		this.actionCounts = state.actionCounts;
		this.kernelLabel=state.kernelLabel;
		this.regionLabel=state.regionLabel;
	}
	/** 
	* @brief This method is a getter for the State label
	* 
	**/
	public String getLabel() 
	{
		return label;
	}
	/** 
	* @brief This method is a setter for the State label
	* 
	**/
	public void setLabel(String label) 
	{
		this.label = label;
	}
	/** 
	* @brief This method is a getter for the Region Label to which the State belongs
	* 
	**/	
	public String getRegionLabel() {
		return regionLabel;
	}
	/** 
	* @brief This method is a setter for the Region Label to which the State belongs
	* 
	**/
	public void setRegionLabel(String regionLabel) {
		this.regionLabel = regionLabel;
	}
	/** 
	* @brief This method is a getter for the Kernel Label to which the State belongs
	* 
	**/
	public String getKernelLabel() {
		return kernelLabel;
	}
	/** 
	* @brief This method is a setter for the Kernel Label to which the State belongs
	* 
	**/
	public void setKernelLabel(String kernelLabel) {
		this.kernelLabel = kernelLabel;
	}
	/** 
	* @brief This method is a getter for the Transition that start from this State
	* 
	**/
	public Map<String, Transition> getTransitions() {
		return transitions;
	}
	/** 
	* @brief This method is a setter for the Transition that start from this State
	* 
	**/
	public void setTransitions(LinkedHashMap<String, Transition> transitions) {
		this.transitions = transitions;
	}
	/** 
	* @brief This method checks if this State is the initial state of the MDP
	* 
	**/
	public float getInitialProbability() 
	{
		return initialProbability;
	}
	/** 
	* @brief This method sets this State as the initial state of the MDP
	* 
	**/
	public void setInital(float initialProbability) 
	{
		this.initialProbability = initialProbability;
	}
	/** 
	* @brief This method returns the map of Key=action and Value=number of Transition with that action
	* 
	**/
	public Map<String, Action> getActionCounts() {
		return actionCounts;
	}
	/** 
	* @brief This method sets the map of Key=action and Value=number of Transition with that action
	* 
	**/
	public void setActionCounts(LinkedHashMap<String, Action> actionCounts) {
		this.actionCounts = actionCounts;
	}
	/** 
	* @brief This method adds a Transition to the Transition Map of this State 
	* 
	**/
	public Transition addTransition(Transition t)
	{
		return transitions.put(t.toString(),t);
	}

	/** 
	* @brief This method prints all the Transition that start from this State
	* 
	**/
	String printAllTransitions()
	{
		String result="";
		for(Map.Entry<String, Transition> entry : transitions.entrySet())
		{
			result+=entry.getValue().toString();
		}
		return result;
	}
	
	/** 
	* @brief This method returns the probability of a Transition to a particular State and with a particular Action, as specified in the argumentss
	* 
	**/
	float getProbabilityToState(String stateLabel, String acionLabel)
	{
		for(Map.Entry<String, Transition> transition : transitions.entrySet())
		{
			if((transition.getValue().getAction().equals(acionLabel)) && (transition.getValue().getToState().equals(stateLabel)))
			{
				return transition.getValue().getProbability();
			}
		}
		return 0;
	}
	
	/** 
	* @brief This methos returns of taking an action from this State 
	* 
	**/
	float getActionProbability(String actionLabel)
	{
		if(actionCounts.containsKey(actionLabel))
		{
			return (float)actionCounts.get(actionLabel).getCount()/transitions.size();
		}
		return 0;
	}
	
	/** 
	* @brief This is a default constructor of the MDP class
	* 
	**/
	public TreeSet<String> getNextStates()
	{
		TreeSet<String> nextStates =new TreeSet<String>();
		if(!transitions.isEmpty())
		{
			for(Map.Entry<String, Transition> transition : transitions.entrySet())
			{
				nextStates.add(transition.getValue().getToState());
			}
		}
		return nextStates;
	}
	/** 
	* @brief This method is used to print the data structure that holds the action counts
	* 
	**/
	public String printActionCount()
	{
		String result="[ ";
		for(Map.Entry<String, Action> action : actionCounts.entrySet())
		{
			result+=" [ "+action.getKey()+", count = "+action.getValue().getCount()+", reward = "+action.getValue().getReward()+" ] ";
		}
		result+=" ]";
		return result;
	}
	/** 
	* @brief This method converts the State object to a String for displaying on the console/GUI.
	* 
	**/
	@Override
	public String toString() 
	{
		return "State [label=" + label + ", initialProbability=" + initialProbability + ", actionCounts=" + printActionCount() + ", \nRegionLabel="+regionLabel+" KernelLabel="+ kernelLabel +", \ntransitions=" + printAllTransitions() + "]\n";
	}
	/** 
	* @brief This method is used to compare two State objects
	* 
	**/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actionCounts == null) ? 0 : actionCounts.hashCode());
		result = prime * result + Float.floatToIntBits(initialProbability);
		result = prime * result
				+ ((kernelLabel == null) ? 0 : kernelLabel.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((regionLabel == null) ? 0 : regionLabel.hashCode());
		result = prime * result
				+ ((transitions == null) ? 0 : transitions.hashCode());
		return result;
	}
	
	/** 
	* @brief This method is used to check if two State objects are equal or not
	* 
	**/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (actionCounts == null) {
			if (other.actionCounts != null)
				return false;
		} else if (!actionCounts.equals(other.actionCounts))
			return false;
		if (Float.floatToIntBits(initialProbability) != Float
				.floatToIntBits(other.initialProbability))
			return false;
		if (kernelLabel == null) {
			if (other.kernelLabel != null)
				return false;
		} else if (!kernelLabel.equals(other.kernelLabel))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (regionLabel == null) {
			if (other.regionLabel != null)
				return false;
		} else if (!regionLabel.equals(other.regionLabel))
			return false;
		if (transitions == null) {
			if (other.transitions != null)
				return false;
		} else if (!transitions.equals(other.transitions))
			return false;
		return true;
	}


	
}
