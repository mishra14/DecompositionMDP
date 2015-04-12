package ese.seas.upenn.edu;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/** 
* @file PlanarSeparator.java 
* 
* @brief This file contains the source code for the BFS and DFS based MDP decomposition
* 
* @author Ankit Mishra
*
* @date May, 1 2015
* 
**/

/** 
* @class PlanarSeparator
* 
* @brief This class contains the source code for the BFS and DFS based MDP decomposition
* 
**/

public class PlanarSeparator 
{
	private static HashSet<String> seenStates;
	private static LinkedHashMap<String, LinkedHashSet<String>> layers;
	private static int layerCount;
	/** 
	* @brief This method is a getter for the seenState HashMap
	* 
	**/
	public static HashSet<String> getSeenStates() {
		return seenStates;
	}
	/** 
	* @brief This method is a setter for the seenState HashMap
	* 
	**/
	public static void setSeenStates(HashSet<String> seenStates) {
		PlanarSeparator.seenStates = seenStates;
	}
	/** 
	* @brief This method is a getter for the Layers HashMap that returns the resultant decompositon
	* 
	**/
	public static LinkedHashMap<String, LinkedHashSet<String>> getLayers() {
		return layers;
	}
	/** 
	* @brief This method is a setter for the Layers HashMap 
	* 
	**/
	public static void setLayers(LinkedHashMap<String, LinkedHashSet<String>> layers) {
		PlanarSeparator.layers = layers;
	}
	/** 
	* @brief This method is a getter for the Layer Count 
	* 
	**/
	public static int getLayerCount() {
		return layerCount;
	}
	/** 
	* @brief This method is a setter for the Layer Count 
	* 
	**/
	public static void setLayerCount(int layerCount) {
		PlanarSeparator.layerCount = layerCount;
	}
	/** 
	* @brief This method is used to reset the static variables of the PlanarSeparator class
	* 
	**/
	static void init()
	{
		seenStates=new HashSet<String>();
		layers=new LinkedHashMap<String, LinkedHashSet<String>>();	
		layerCount=0;
	}
	/** 
	* @brief This method creates BFS layers of the given MDP
	* 
	* Note - This method is currently unused.
	**/
	static void BFSLayerGeneration(String initialState, MDP mdp)
	{
		init();
		int vertexCount=mdp.getStates().size();
		int currentCount=0;
		LinkedHashSet<String> current=new LinkedHashSet<String>();
		LinkedHashSet<String> next=new LinkedHashSet<String>();
		seenStates=new HashSet<String>();
		layers = new LinkedHashMap<String, LinkedHashSet<String>>();	
		current.add(initialState);
		layers.put("l"+layerCount, current);
		layerCount++;
		next.addAll(mdp.getStates().get(initialState).getNextStates());
		while(!current.isEmpty())
		{
			//System.out.println(current);
			for(String state : current)
			{
				if(!seenStates.contains(state))
				{
					next.addAll(mdp.getStates().get(state).getNextStates());
					seenStates.add(state);
				}
			}
			next.removeAll(seenStates);
			if(!next.isEmpty())
			{	
				if((currentCount<(vertexCount/2))&&((currentCount+next.size())>(vertexCount/2)))
				{
					System.out.println("Median Layer : "+layerCount);
				}
				currentCount+=next.size();
				layers.put("l"+layerCount, next);
				//if(layers.get(layerCount).size()<5)
				layerCount++;
			}
			current=new LinkedHashSet<String>(next);
			next=new LinkedHashSet<String>();
		}
		//System.out.println(layers);
	}
	/** 
	* @brief This method creates DFS based decomposition of the given MDP
	* 
	* This method creates a decomposition by using a count based heuristic. 
	* Starting from the supplied initial State Label, the method places each newly discovered State into the current Region.<br>
	* When the State count inthe current region exceeds the total of Vertex Count / Region Count, then the Region count is incremented.<br>
	* This process is continued till all the states are explored via DFS and placed in a region. The final decomposition is placed in the Layers.
	**/	
	static void DFSDecomposition(String initialState, MDP mdp, int region)
	{
		Set<String> next=new TreeSet<String>();
		if(!mdp.getStates().containsKey(initialState))
		{
			return; 		//TODO return with error
		}
		State s=mdp.getStates().get(initialState);
		next=s.getNextStates();
		seenStates.add(initialState);
		//System.out.println(initialState);
		if(layers.containsKey("r"+region) && layers.get("r"+region).size()>mdp.getStates().size()/mdp.getRegionCount())
		{
			region++;
		}
		if(layers.containsKey("r"+region))
		{
			
			layers.get("r"+region).add(initialState);
			s.setRegionLabel("r"+region);
			s.setKernelLabel("k"+region);
			//layers.get((layers.get(region).size()>(mdp.getStates().size()/2))?region:++region).add(initialState);
		}
		else
		{
			layers.put("r"+region, new LinkedHashSet<String>());
			layers.get("r"+region).add(initialState);
			s.setRegionLabel("r"+region);
			s.setKernelLabel("k"+region);
		}
		for(String state:next)
		{
			if(!seenStates.contains(state))
			{
				DFSDecomposition(state, mdp, region);
				//System.out.println("returned from " +state);
			}
		}
		//System.out.println(layers);
	}
	/** 
	* @brief This method improves the basic DFS/BFS based decomposition of the given MDP
	* 
	* This method improves the decomposition by using an heuristic that a State should belong to the Region To and From which it has the maximum number of Transition.<br>
	* The method goes through each State in the nested Map that contains Key=State and Value=Map of {Key = Region and Value = Count of Transition to and From that Region to that State}<br>
	* Then the resultant Map is checked to return the Dominant Region of every State and if the State does not belong to that Region then its Region is changed to that Region.
	* Finally the resultant decomposition is returned to the calling method.
	**/	
	public static Map<String, LinkedHashSet<String>> improveDecomposition(MDP mdp) 
	{
		Map<String,LinkedHashMap<String,Integer>> stateReachability=new LinkedHashMap<String,LinkedHashMap<String, Integer>>();
		Map<String,LinkedHashMap<String,Integer>> stateReachabilitySmall=new LinkedHashMap<String,LinkedHashMap<String, Integer>>();
		Map<String, LinkedHashSet<String>> newRegions=new LinkedHashMap<String,  LinkedHashSet<String>>();
		//creating a deep copy of the original mdp regions
		for(Map.Entry<String, LinkedHashSet<String>> region : mdp.getRegions().entrySet())
		{
			String key=new String(region.getKey());
			LinkedHashSet<String> value=new LinkedHashSet<String>();
			for(String s : region.getValue())
			{
				value.add(new String(s));
			}
			newRegions.put(key, value);
		}
		for(Map.Entry<String, LinkedHashSet<String>> region : newRegions.entrySet())
		{
			for(String s : region.getValue())
			{
				State state = new State(mdp.getStates().get(s));
				if(!stateReachability.containsKey(s))
				{
					stateReachability.put(s, new LinkedHashMap<String, Integer>());
				}
				for(Map.Entry<String, Transition> transition : state.getTransitions().entrySet())
				{
					if(stateReachability.get(s).containsKey(mdp.getStates().get(transition.getValue().getToState()).getRegionLabel()+"TO"))
					{
						int count =stateReachability.get(s).get(mdp.getStates().get(transition.getValue().getToState()).getRegionLabel()+"TO");
						stateReachability.get(s).put(mdp.getStates().get(transition.getValue().getToState()).getRegionLabel()+"TO",++count);
					}
					else
					{
						stateReachability.get(s).put(mdp.getStates().get(transition.getValue().getToState()).getRegionLabel()+"TO",1);
					}
					
					State toState=new State(mdp.getStates().get(transition.getValue().getToState()));
					if(!stateReachability.containsKey(toState.getLabel()))
					{
						stateReachability.put(toState.getLabel(), new LinkedHashMap<String, Integer>());
					}
					if(stateReachability.get(toState.getLabel()).containsKey(state.getRegionLabel()+"FROM"))
					{
						int count =stateReachability.get(toState.getLabel()).get(state.getRegionLabel()+"FROM");
						stateReachability.get(toState.getLabel()).put(state.getRegionLabel()+"FROM",++count);
					}
					else
					{
						stateReachability.get(toState.getLabel()).put(state.getRegionLabel()+"FROM", 1);
					}
				}
			}
		}
		for(Map.Entry<String, LinkedHashMap<String,Integer>>state: stateReachability.entrySet())
		{
			LinkedHashMap<String, Integer> temp =new LinkedHashMap<String, Integer>();
			System.out.print(state.getKey()+" - Region = "+mdp.getStates().get(state.getKey()).getRegionLabel()+" ");
			for(Map.Entry<String, Integer> regionCounter : state.getValue().entrySet())
			{
				if(temp.containsKey(regionCounter.getKey().subSequence(0, 2)))
				{
					temp.put(regionCounter.getKey().subSequence(0, 2).toString(), temp.get(regionCounter.getKey().subSequence(0, 2))+regionCounter.getValue());
				}
				else
				{
					temp.put(regionCounter.getKey().subSequence(0, 2).toString(), regionCounter.getValue());
				}				
			}
			
			if(!stateReachabilitySmall.containsKey(state.getKey()))
			{
				System.out.println(temp);
				stateReachabilitySmall.put(state.getKey(), temp);
				int max=0;
				String maxRegion="";
				for(Map.Entry<String, Integer> regionCounterSmall : stateReachabilitySmall.get(state.getKey()).entrySet())
				{
					if(regionCounterSmall.getValue()>=max)
					{
						max=regionCounterSmall.getValue();
						maxRegion=regionCounterSmall.getKey();
					}
				}
				if(!maxRegion.equalsIgnoreCase(mdp.getStates().get(state.getKey()).getRegionLabel()) && !(mdp.getRegions().get(mdp.getStates().get(state.getKey()).getRegionLabel()).size()<=1))
				{	
					System.out.println("Changing region from "+mdp.getStates().get(state.getKey()).getRegionLabel()+" to "+maxRegion);
					newRegions.get(mdp.getStates().get(state.getKey()).getRegionLabel()).remove(state.getKey());
					newRegions.get(maxRegion).add(state.getKey());
					mdp.getStates().get(state.getKey()).setRegionLabel(maxRegion);
				}
			}
		}
		System.out.println("Original"+mdp.getRegions());
		System.out.println("New"+newRegions);
		//System.out.println(stateReachabilitySmall);
		return newRegions;
	}
	
}
