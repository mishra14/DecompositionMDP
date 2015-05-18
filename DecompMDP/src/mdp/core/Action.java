package mdp.core;

/** 
* @class Action 
* 
* @brief This class contains the source code for the Action bean
* 
**/


public class Action 
{
	private int count;
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
	
	
}
