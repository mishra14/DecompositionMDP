package ese.seas.upenn.edu;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class State 
{
	private String label;
	private LinkedHashMap<String, Transition> transitions;
	private boolean isInital;
	private LinkedHashMap<String, Integer> actionCounts;
	private String regionLabel;
	private String kernelLabel;
	
	public State(String label, LinkedHashMap<String, Transition> transitions, boolean isInital) 
	{
		super();
		this.label = label;
		this.transitions = transitions;
		this.isInital = isInital;
		this.actionCounts=new LinkedHashMap<String, Integer>();
		this.kernelLabel=new String();
		this.regionLabel=new String();
	}
	public State(String label, LinkedHashMap<String, Transition> transitions,boolean isInital, LinkedHashMap<String, Integer> actionCounts, String regionLabel, String kernelLabel) 
	{
		super();
		this.label = label;
		this.transitions = transitions;
		this.isInital = isInital;
		this.actionCounts = actionCounts;
		this.kernelLabel=kernelLabel;
		this.regionLabel=regionLabel;
	}
	public State(String label) 
	{
		super();
		this.label = label;
		this.transitions=new LinkedHashMap<String, Transition>();
		this.isInital=false;
		this.actionCounts=new LinkedHashMap<String, Integer>();
		this.kernelLabel=new String();
		this.regionLabel=new String();
	}

	public State(State state) 
	{
		super();
		this.label = state.label;
		this.transitions = state.transitions;
		this.isInital = state.isInital;
		this.actionCounts = state.actionCounts;
		this.kernelLabel=state.kernelLabel;
		this.regionLabel=state.regionLabel;
	}
	public String getLabel() 
	{
		return label;
	}

	public void setLabel(String label) 
	{
		this.label = label;
	}

	
	
	public String getRegionLabel() {
		return regionLabel;
	}
	public void setRegionLabel(String regionLabel) {
		this.regionLabel = regionLabel;
	}
	public String getKernelLabel() {
		return kernelLabel;
	}
	public void setKernelLabel(String kernelLabel) {
		this.kernelLabel = kernelLabel;
	}
	public Map<String, Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(LinkedHashMap<String, Transition> transitions) {
		this.transitions = transitions;
	}

	public boolean isInital() 
	{
		return isInital;
	}

	public void setInital(boolean isInital) 
	{
		this.isInital = isInital;
	}

	public Map<String, Integer> getActionCounts() {
		return actionCounts;
	}
	public void setActionCounts(LinkedHashMap<String, Integer> actionCounts) {
		this.actionCounts = actionCounts;
	}
	public Transition addTransition(Transition t)
	{
		return transitions.put(t.toString(),t);
	}

	@Override
	public String toString() {
		return "State [label=" + label + ", isInital=" + isInital + ", actionCounts=" + actionCounts + ", \nRegionLabel="+regionLabel+" KernelLabel="+ kernelLabel +", \ntransitions=" + printAllTransitions() + "]\n";
	}

	String printAllTransitions()
	{
		String result="";
		for(Map.Entry<String, Transition> entry : transitions.entrySet())
		{
			result+=entry.getValue().toString();
		}
		return result;
	}
	
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
	
	float getActionProbability(String actionLabel)
	{
		if(actionCounts.containsKey(actionLabel))
		{
			return (float)actionCounts.get(actionLabel)/transitions.size();
		}
		return 0;
	}
	
	Set<String> getNextStates()
	{
		Set<String> nextStates =new TreeSet<String>();
		if(!transitions.isEmpty())
		{
			for(Map.Entry<String, Transition> transition : transitions.entrySet())
			{
				nextStates.add(transition.getValue().getToState());
			}
		}
		return nextStates;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actionCounts == null) ? 0 : actionCounts.hashCode());
		result = prime * result + (isInital ? 1231 : 1237);
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((transitions == null) ? 0 : transitions.hashCode());
		return result;
	}

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
		if (isInital != other.isInital)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (transitions == null) {
			if (other.transitions != null)
				return false;
		} else if (!transitions.equals(other.transitions))
			return false;
		return true;
	}

}
