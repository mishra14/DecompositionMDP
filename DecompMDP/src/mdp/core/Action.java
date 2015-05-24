package mdp.core;

/** 
* @file Action.java 
* 
* @brief This file contains the source code for the Action bean and Util methods
* 
* @author Ankit Mishra
*
* @date May, 1 2015
* 
**/


/** 
* @class Action 
* 
* @brief This class contains the source code for the Action bean
* 
**/


public class Action 
{
	private int count;				//TODO - find the purpose of this variable
	private float reward;
	
	public Action(int count, float reward) 
	{
		super();
		this.count = count;
		this.reward = reward;
	}
	
	public Action()
	{
		super();
		this.count=0;
		this.reward=0;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getReward() {
		return reward;
	}

	public void setReward(float reward) {
		this.reward = reward;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + Float.floatToIntBits(reward);
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
		Action other = (Action) obj;
		if (count != other.count)
			return false;
		if (Float.floatToIntBits(reward) != Float.floatToIntBits(other.reward))
			return false;
		return true;
	}
	
	
}
