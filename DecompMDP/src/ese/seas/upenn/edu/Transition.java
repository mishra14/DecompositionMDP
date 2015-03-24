package ese.seas.upenn.edu;

public class Transition 
{
	private float probability;
	private String toStateLabel;
	private String action;
	
	public Transition(float probability, String toStateLabel, String action)
	{
		super();
		this.probability = probability;
		this.toStateLabel = toStateLabel;
		this.action =action;
	}
	public float getProbability() 
	{
		return probability;
	}
	public void setProbability(float probability) 
	{
		this.probability = probability;
	}	
	public String getToState() {
		return toStateLabel;
	}
	public void setToState(String toStateLabel) {
		this.toStateLabel = toStateLabel;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Override
	public String toString() 
	{
		return "Transition [probability=" + probability + ", toState="+ toStateLabel + ", action=" + action + "]\n";
	}
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
